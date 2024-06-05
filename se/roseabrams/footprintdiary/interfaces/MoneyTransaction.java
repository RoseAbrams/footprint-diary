package se.roseabrams.footprintdiary.interfaces;

public interface MoneyTransaction { // banking, credit cards, purchases, etc...

    public float getAmount();

    public Currency getCurrency();

    public static enum Currency {
        SEK, USD, EUR
    }
}
