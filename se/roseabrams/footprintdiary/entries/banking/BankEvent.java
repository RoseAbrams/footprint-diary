package se.roseabrams.footprintdiary.entries.banking;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.common.MoneyTransaction;

public class BankEvent extends DiaryEntry implements MoneyTransaction {

    public final float AMOUNT;
    public final float BALANCE;
    public final Type TYPE;
    public final String FROM_ACCOUNT;
    public final String TO_ACCOUNT;
    public final String DESCRIPTION;
    public final Currency CURRENCY;

    public BankEvent(DiaryDate date, float amount, float balance, Type type, String fromAccount, String toAccount,
            String description, Currency currency) {
        super(DiaryEntryCategory.FINANCE, date);
        AMOUNT = amount;
        BALANCE = balance;
        TYPE = type;
        FROM_ACCOUNT = fromAccount.isBlank() ? null : fromAccount.intern();
        TO_ACCOUNT = toAccount.isBlank() ? null : toAccount.intern();
        assert FROM_ACCOUNT != null || TO_ACCOUNT != null;
        DESCRIPTION = description;
        CURRENCY = currency;
    }

    @Override
    public String getStringSummary() {
        return AMOUNT + " " + CURRENCY + " (" + DESCRIPTION + ")";
    }

    @Override
    public float getAmount() {
        return AMOUNT;
    }

    @Override
    public Currency getCurrency() {
        return CURRENCY;
    }

    @Override
    public boolean moneySent() {
        return AMOUNT < 0;
    }

    public static enum Type {
        CARD, SWISH, BANKGIRO, PLUSGIRO, AUTOGIRO, ATM, BANKING_FEE, BANKING_TRANSFER;

        static Type parseDesc(String desc) {
            String[] split = desc.split(" ");
            switch (split[0]) {
                case "Kortköp":
                    return CARD;
                case "Betalning":
                    switch (split[1]) {
                        case "BG":
                            return BANKGIRO;
                        case "PG":
                            return PLUSGIRO;
                        default:
                            throw new AssertionError();
                    }
                case "Bankgiroinsättning":
                    return BANKGIRO;
                case "Autogiro":
                    return AUTOGIRO;
                case "Swish":
                    return SWISH;
                case "Kontantuttag":
                case "Kontantinsättning":
                    return ATM;
                case "Vardagspaket":
                    return BANKING_FEE;
                case "Överföring":
                    return BANKING_TRANSFER;
                case "Lön":
                case "A-KASSA":
                case "FK/PMYND":
                    return null; // unspecified known
                default:
                    return null; // unspecified unknown
            }
        }
    }

    public static List<BankEvent> createFromCsv(File transactionsFile) throws IOException {
        ArrayList<BankEvent> output = new ArrayList<>(5000);
        List<String> transactions = Util.readFileLines(transactionsFile);
        for (String transaction : transactions) {
            Scanner s = new Scanner(transaction);
            s.useDelimiter(";");
            String dateS = s.next();
            if (dateS.contains("Bokföringsdag") || dateS.contains("Reserverat"))
                continue;
            DiaryDate date = new DiaryDate(dateS);
            String amountS = s.next();
            float amount = Float.parseFloat(amountS.replace(',', '.'));
            String fromAccount = s.next();
            String toAccount = s.next();
            assert fromAccount.isBlank() || toAccount.isBlank();
            String toAccountName = s.next();
            assert toAccountName.isBlank(); // not sure what this should contain... "egen notering" maybe?
            String description = s.next();
            String balanceS = s.next();
            float balance = Float.parseFloat(balanceS.replace(',', '.'));
            String currencyS = s.next();
            Currency currency = Currency.valueOf(currencyS);

            Type type = Type.parseDesc(description);
            String descTrimmed = null;
            String[] split = description.split(" ");
            if (type == null || split.length == 1)
                toAccount = null;
            else {
                switch (type) {
                    case AUTOGIRO:
                    case BANKING_TRANSFER:
                        toAccount = truncateSplit(split, 1);
                        break;
                    case BANKING_FEE:
                        toAccount = null;
                        descTrimmed = truncateSplit(split, 1);
                        break;
                    case CARD:
                    case SWISH:
                        toAccount = truncateSplit(split, 2);
                        break;
                    case BANKGIRO:
                    case PLUSGIRO:
                        if (amount < 0)
                            toAccount = truncateSplit(split, 2);
                        else
                            toAccount = null;
                        break;
                    default:
                        s.close();
                        throw new AssertionError();
                }
            }

            BankEvent b = new BankEvent(date, amount, balance, type, fromAccount, toAccount,
                    descTrimmed != null ? descTrimmed : description, currency);
            output.add(b);
            s.close();
        }
        return output;
    }

    private static final int BUFFER_SIZE = 1000;

    public static List<BankEvent> createFromAzureJson(File transactionsFolder) throws IOException {
        ArrayList<BankEvent> output = new ArrayList<>(10000);
        for (File transactionsFile : transactionsFolder.listFiles()) {
            String fromAccount = transactionsFile.getName().substring(0, transactionsFile.getName().indexOf(" ("));
            JSONObject transactionsFileJ = Util.readJsonObjectFile(transactionsFile);
            JSONArray tables = transactionsFileJ.getJSONObject("analyzeResult").getJSONArray("tables");
            for (Object tableO : tables) {
                JSONObject table = (JSONObject) tableO;
                if (table.getInt("columnCount") != 4)
                    continue; // table contains non-transaction data
                String[][] tableBuffer = new String[BUFFER_SIZE][4];

                for (Object cellO : table.getJSONArray("cells")) {
                    JSONObject cell = (JSONObject) cellO;
                    if (cell.has("kind") && cell.getString("kind").equals("columnHeader"))
                        continue;
                    int column = cell.getInt("columnIndex");
                    int row = cell.getInt("rowIndex");
                    assert row < BUFFER_SIZE;
                    tableBuffer[row][column] = cell.getString("content");
                }

                for (String[] row : tableBuffer) {
                    String dateS = row[0];
                    String desc = row[1];
                    String amountS = row[2].replace(",", ".");
                    String balanceS = row[3].replace(",", ".");
                    float amount = Float.parseFloat(amountS.replace("-", ""));
                    if (amountS.endsWith("-"))
                        amount = -amount;
                    float balance = Float.parseFloat(balanceS.replace("-", ""));
                    if (balanceS.endsWith("-"))
                        balance = -balance;

                    Type type = Type.parseDesc(desc);
                    String toAccount;
                    String descTrimmed = null;
                    String[] split = desc.split(" ");
                    if (type == null || split.length == 1)
                        toAccount = null;
                    else {
                        switch (type) {
                            case AUTOGIRO:
                                toAccount = truncateSplit(split, 1);
                                break;
                            case BANKING_FEE:
                                toAccount = null;
                                descTrimmed = truncateSplit(split, 1);
                                break;
                            case CARD:
                            case SWISH:
                                toAccount = truncateSplit(split, 2);
                                break;
                            case BANKGIRO:
                            case PLUSGIRO:
                                if (amount < 0)
                                    toAccount = truncateSplit(split, 2);
                                else
                                    toAccount = null;
                                break;
                            default:
                                throw new AssertionError();
                        }
                    }

                    DiaryDate date;
                    if (type == Type.CARD) {
                        dateS = desc.substring(8, 14);
                        date = new DiaryDate(
                                Short.parseShort("20" + dateS.substring(0, 2)),
                                Byte.parseByte(dateS.substring(2, 4)),
                                Byte.parseByte(dateS.substring(4, 6)));
                    } else
                        date = new DiaryDate(dateS);

                    BankEvent b = new BankEvent(date, amount, balance, type, fromAccount, toAccount,
                            descTrimmed != null ? descTrimmed : desc, Currency.SEK);
                    output.add(b);
                }
            }
        }
        return output;
    }

    private static String truncateSplit(String[] split, int startIndex) {
        StringBuilder output = new StringBuilder();
        for (int i = startIndex; i < split.length; i++) {
            output.append(split[i]);
        }
        return output.toString();
    }
}
