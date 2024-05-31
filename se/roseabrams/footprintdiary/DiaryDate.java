package se.roseabrams.footprintdiary;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class DiaryDate implements Serializable, Comparable<DiaryDate> {
    public final short YEAR;
    public final byte MONTH;
    public final byte DAY;
    public transient GregorianCalendar detailedDate = null;

    public DiaryDate(short year, byte month, byte day) {
        assert year >= 2000 && year < 2025;
        assert month >= 1 && month <= 12;
        assert day >= 1 && month <= 31;

        this.YEAR = year;
        this.MONTH = month;
        this.DAY = day;
    }

    public DiaryDate(String dateString) {
        this(Short.parseShort(dateString.substring(0, 4)), Byte.parseByte(dateString.substring(5, 6)),
                Byte.parseByte(dateString.substring(7, 8)));
    }

    public GregorianCalendar getDetailedDate() {
        if (detailedDate != null)
            return detailedDate;
        detailedDate = new GregorianCalendar(YEAR, MONTH - 1, DAY);
        return detailedDate;
    }

    public int differenceDays(DiaryDate d2) {
        return differenceDays(this, d2);
    }

    public static int differenceDays(DiaryDate d1, DiaryDate d2) {
        return (int) TimeUnit.MILLISECONDS
                .toDays(d2.getDetailedDate().getTime().getTime() - d1.getDetailedDate().getTime().getTime());
    }

    @Override
    public int compareTo(DiaryDate d2) {
        int comparedYear = Short.compare(YEAR, d2.YEAR);
        int comparedMonth = Short.compare(MONTH, d2.MONTH);
        int comparedDay = Short.compare(DAY, d2.DAY);

        if (comparedYear != 0) {
            return comparedYear;
        }
        if (comparedMonth != 0) {
            return comparedMonth;
        }
        return comparedDay;
    }

    @Override
    @Deprecated // Use below method to explicitly specify strictness
    public boolean equals(Object obj) {
        return equals((DiaryDate) obj, true);
    }

    public boolean equals(DiaryDate obj, boolean strict) {
        return compareTo(obj) == 0 && (strict ? !(obj instanceof DiaryDateTime) : true);
    }

    @Override
    public String toString() { // TODO leading zeroes for fixed length?
        return YEAR + "-" + MONTH + "-" + DAY;
    }

    public static byte parseMonthName(String monthName) {
        if (monthName.length() > 3)
            monthName = monthName.substring(0, 3);
        switch (monthName.toLowerCase()) {
            case "jan":
                return 1;
            case "feb":
                return 2;
            case "mar":
                return 3;
            case "apr":
                return 4;
            case "may":
                return 5;
            case "jun":
                return 6;
            case "jul":
                return 7;
            case "aug":
                return 8;
            case "sep":
                return 9;
            case "oct":
                return 10;
            case "nov":
                return 11;
            case "dec":
                return 12;
            default:
                throw new IllegalArgumentException("Not valid month: " + monthName);
        }
    }
}
