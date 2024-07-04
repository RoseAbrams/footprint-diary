package se.roseabrams.footprintdiary.entries.steam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.Util;

public class SteamStoreEvent extends SteamEvent {

    private final String[] ITEMS;
    public final Type TYPE;
    public final String PAYMENT_METHOD;
    public final float PAYMENT_TOTAL;
    public final float WALLET_CHANGE;
    public final float WALLET_BALANCE;

    public SteamStoreEvent(DiaryDate dd, String[] items, Type type, String paymentMethod, float paymentTotal,
            float walletChange, float walletBalance) {
        super(DiaryEntryCategory.STEAM, dd);
        assert dd != null;

        assert items != null;
        assert type != null;
        assert !Float.isNaN(paymentTotal);
        assert paymentMethod != null;

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
        return output.substring(0, output.length() - 3);
    }

    public static enum Type {
        STORE_PURCHASE, MARKET_TRANSACTION, IN_GAME_PURCHASE, GIFT_PURCHASE, REFUND;
    }

    @Deprecated // remake this with Jsoup
    public static SteamStoreEvent[] createFromXml(File purchaseHistory) throws IOException {
        ArrayList<SteamStoreEvent> output = new ArrayList<>(1000);
        org.w3c.dom.Document d = Util.readXmlFile(purchaseHistory);
        NodeList tableRows = d.getDocumentElement().getLastChild().getChildNodes();
        for (int i = 0; i < tableRows.getLength(); i++) {
            Node row = tableRows.item(i);
            NodeList cells = row.getChildNodes();
            assert cells.getLength() == 6;
            DiaryDate date = null;
            String[] items = null;
            Type type = null;
            String paymentMethod = null;
            float total = Float.NaN;
            float walletChange = Float.NaN;
            float walletBalance = Float.NaN;
            for (int j = 0; j < cells.getLength(); j++) {
                Node cell = cells.item(i);
                switch (j) {
                    case 0:
                        String dateS = cell.getTextContent();
                        date = new DiaryDate(Short.parseShort(dateS.substring(8)),
                                DiaryDate.parseMonthName(dateS.substring(dateS.indexOf(" ") + 1, dateS.indexOf(","))),
                                Byte.parseByte(dateS.substring(0, 2)));
                        break;
                    case 1:
                        NodeList itemsN = cell.getChildNodes();
                        items = new String[itemsN.getLength()];
                        for (int k = 0; k < items.length; k++) {
                            items[k] = itemsN.item(k).getTextContent();
                        }
                        break;
                    case 2:
                        NodeList types = cell.getChildNodes();
                        String typeS = types.item(0).getTextContent();
                        if (typeS.contains("Market Transaction"))
                            type = Type.MARKET_TRANSACTION;
                        else if (typeS.equals("Purchase"))
                            type = Type.STORE_PURCHASE;
                        else if (typeS.contains("In-Game"))
                            type = Type.IN_GAME_PURCHASE;
                        else if (typeS.equals("Gift Purchase"))
                            type = Type.GIFT_PURCHASE;
                        else
                            throw new IllegalArgumentException();
                        Node typePaymentN = types.item(1);
                        assert typePaymentN != null; // looks like it, let's make sure
                        paymentMethod = typePaymentN.getTextContent();
                        break;
                    case 3:
                        total = parseCurrency(cell.getTextContent());
                        break;
                    case 4:
                        if (cell.getTextContent().isBlank())
                            walletChange = 0;
                        else
                            walletChange = parseCurrency(cell.getTextContent());
                        break;
                    case 5:
                        if (cell.getTextContent().isBlank() || cell.getTextContent().contains(",--"))
                            walletBalance = 0;
                        else
                            walletBalance = parseCurrency(cell.getTextContent());
                        break;
                    default:
                        throw new AssertionError();
                }
            }
            output.add(new SteamStoreEvent(date, items, type, paymentMethod, total, walletChange, walletBalance));
        }

        return output.toArray(new SteamStoreEvent[output.size()]);
    }

    @Deprecated // super-rough draft based on above, TODO should probably delete and start over
    public static SteamStoreEvent[] createFromHtml(File purchaseHistory) throws IOException {
        ArrayList<SteamStoreEvent> output = new ArrayList<>(1000);
        org.jsoup.nodes.Document d = Jsoup.parse(purchaseHistory);
        Elements tableRows = d.select("tbody > tr.wallet_table_row");
        for (int i = 0; i < tableRows.size(); i++) {
            Element row = tableRows.get(i);
            Elements cells = row.children();
            assert cells.size() == 6;
            DiaryDate date = null;
            String[] items = null;
            Type type = null;
            String paymentMethod = null;
            float total = Float.NaN;
            float walletChange = Float.NaN;
            float walletBalance = Float.NaN;
            for (int j = 0; j < cells.size(); j++) {
                Element cell = cells.get(j);
                switch (j) {
                    case 0:
                        String dateS = cell.text();
                        date = new DiaryDate(Short.parseShort(dateS.substring(dateS.indexOf(",") + 2)),
                                DiaryDate.parseMonthName(dateS.substring(dateS.indexOf(" ") + 1, dateS.indexOf(","))),
                                Byte.parseByte(dateS.substring(0, 2).trim()));
                        break;
                    case 1:
                        Elements itemsN = cell.children();
                        if (itemsN.size() == 0) {
                            items = new String[1];
                            items[0] = cell.text();
                        } else {
                            items = new String[itemsN.size()];
                            for (int k = 0; k < items.length; k++) {
                                if (itemsN.get(k).text().startsWith("Gift sent to"))
                                    continue;
                                items[k] = itemsN.get(k).text();
                            }
                        }
                        break;
                    case 2:
                        Elements types = cell.children();
                        String typeS = types.get(0).text();
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
                        Element typePaymentN = types.get(1);
                        assert typePaymentN != null; // looks like it, let's make sure
                        paymentMethod = typePaymentN.text();
                        break;
                    case 3:
                        total = parseCurrency(cell.text());
                        break;
                    case 4:
                        if (cell.text().isBlank())
                            walletChange = 0;
                        else
                            walletChange = parseCurrency(cell.text());
                        break;
                    case 5:
                        if (cell.text().isBlank())
                            walletBalance = 0;
                        else
                            walletBalance = parseCurrency(cell.text());
                        break;
                    default:
                        throw new AssertionError();
                }
            }
            output.add(new SteamStoreEvent(date, items, type, paymentMethod, total, walletChange, walletBalance));
        }

        return output.toArray(new SteamStoreEvent[output.size()]);
    }

    public static float parseCurrency(String s) {
        return Float.parseFloat(s.substring(0, s.indexOf(",") + 3)
                .replace(',', '.').replace("--", "00"));
    }
}
