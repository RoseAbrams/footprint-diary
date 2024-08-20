package se.roseabrams.footprintdiary.entries.steam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntryCategory;

public class SteamStoreEvent extends SteamEvent {

    public final long TRANSACTION_ID;
    private final String[] ITEMS;
    public final Type TYPE;
    public final String PAYMENT_METHOD;
    public final float PAYMENT_TOTAL;
    public final float WALLET_CHANGE;
    public final float WALLET_BALANCE;

    public SteamStoreEvent(DiaryDate dd, long transactionId, String[] items, Type type, String paymentMethod,
            float paymentTotal, float walletChange, float walletBalance) {
        super(DiaryEntryCategory.STEAM, dd);
        assert dd != null;

        assert items != null && items.length != 0;
        assert type != null;
        assert !Float.isNaN(paymentTotal);
        assert paymentMethod != null;

        TRANSACTION_ID = transactionId;
        ITEMS = items;
        TYPE = type;
        PAYMENT_METHOD = paymentMethod.intern();
        PAYMENT_TOTAL = paymentTotal;
        WALLET_CHANGE = walletChange;
        WALLET_BALANCE = walletBalance;
    }

    @Override
    public String getStringSummary() {
        StringBuilder output = new StringBuilder();
        for (String i : ITEMS) {
            output.append(i).append(", ");
        }
        return output.substring(0, output.length() - 2);
    }

    public static enum Type {
        STORE_PURCHASE, MARKET_TRANSACTION, IN_GAME_PURCHASE, GIFT_PURCHASE, REFUND;
    }

    public static SteamStoreEvent[] createFromHtml(File purchaseHistory) throws IOException {
        ArrayList<SteamStoreEvent> output = new ArrayList<>(1000);
        org.jsoup.nodes.Document d = Jsoup.parse(purchaseHistory);
        Elements tableRows = d.select("tbody > tr.wallet_table_row");
        for (int i = 0; i < tableRows.size(); i++) {
            Element tableRow = tableRows.get(i);

            String tableRowLink = tableRow.attr("onclick");
            String tranIdS = tableRowLink.substring(tableRowLink.indexOf("transid=") + 8, 18);
            long tranId = Long.parseLong(tranIdS);

            String dateS = tableRow.selectFirst("td.wht_date").text();
            DiaryDate date = new DiaryDate(Short.parseShort(dateS.substring(dateS.indexOf(",") + 2)),
                    DiaryDate.parseMonthName(dateS.substring(dateS.indexOf(" ") + 1, dateS.indexOf(","))),
                    Byte.parseByte(dateS.substring(0, 2).trim()));

            Elements itemE = tableRow.select("td.wht_items");
            Elements itemsE = itemE.select("div");
            String[] items;
            if (itemsE.size() == 0) {
                items = new String[1];
                items[0] = itemE.text().intern();
            } else {
                items = new String[itemsE.size()];
                for (int j = 0; j < itemsE.size(); j++) {
                    items[j] = itemsE.get(j).text().intern();
                }
            }

            Element typeE = tableRow.selectFirst("td.wht_type");
            String typeS = typeE.firstElementChild().text();
            Type type;
            if (typeS.contains("Market Transaction"))
                type = Type.MARKET_TRANSACTION;
            else if (typeS.equals("Purchase"))
                type = Type.STORE_PURCHASE;
            else if (typeS.contains("In-Game"))
                type = Type.IN_GAME_PURCHASE;
            else if (typeS.equals("Gift Purchase"))
                type = Type.GIFT_PURCHASE;
            else if (typeS.equals("Refund"))
                type = Type.REFUND;
            else
                throw new IllegalArgumentException();

            //Element paymentMethodE = typeE.selectFirst("div.wht_payment");
            Element paymentMethodE = typeE.lastElementChild();
            String paymentMethod = paymentMethodE.text();

            Element totalE = tableRow.selectFirst("td.wht_total");
            float total = parseCurrency(totalE.text());

            Element walletChangeE = tableRow.selectFirst("td.wht_wallet_change");
            float walletChange = walletChangeE.text().isEmpty() ? 0 : parseCurrency(walletChangeE.text());

            Element walletBalanceE = tableRow.selectFirst("td.wht_wallet_balance");
            float walletBalance = walletBalanceE.text().isEmpty() ? 0 : parseCurrency(walletBalanceE.text());

            SteamStoreEvent s = new SteamStoreEvent(date, tranId, items, type, paymentMethod, total, walletChange,
                    walletBalance);
            output.add(s);
        }

        return output.toArray(new SteamStoreEvent[output.size()]);
    }

    public static float parseCurrency(String s) {
        if (s.startsWith("$"))
            s = s.substring(1);
        return Float.parseFloat(s.substring(0, s.indexOf(",") + 3)
                .replace(',', '.').replace("--", "00"));
    }
}
