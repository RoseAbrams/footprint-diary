package se.roseabrams.footprintdiary.common;

public interface MoneyTransaction { // banking, credit cards, purchases, etc...

    public float getAmount(); // intent: no negative value, instead use moneySent() to determine direction

    public Currency getCurrency();

    public boolean moneySent(); // as opposed to received

    public static enum Currency {
        SEK, USD, EUR
    }
}
