package se.roseabrams.footprintdiary.interfaces;

public interface MoneyTransaction { // banking, credit cards, purchases, etc...

    public float getAmount();

    public Currency getCurrency();

    public boolean moneySent(); // as opposed to received

    public static enum Currency {
        SEK, USD, EUR
    }
}
