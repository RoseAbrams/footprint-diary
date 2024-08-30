package se.roseabrams.footprintdiary.entries.banking;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.common.MoneyTransaction;

public class BankEvent extends DiaryEntry implements MoneyTransaction {

    public final float AMOUNT;
    public final float BALANCE;
    public final String FROM_ACCOUNT;
    public final String TO_ACCOUNT;
    public final String DESCRIPTION;
    public final Currency CURRENCY;

    public BankEvent(DiaryDate date, float amount, String fromAccount, String toAccount, String description,
            float balance, Currency currency) {
        super(DiaryEntryCategory.FINANCE, date);
        AMOUNT = amount;
        BALANCE = balance;
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

    public static List<BankEvent> createFromCsv(File transactionsFile) throws IOException {
        ArrayList<BankEvent> output = new ArrayList<>();
        List<String> transations = Util.readFileLines(transactionsFile);
        for (String transaction : transations) {
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

            BankEvent b = new BankEvent(date, amount, fromAccount, toAccount, description, balance, currency);
            output.add(b);
            s.close();
        }
        return output;
    }
}
