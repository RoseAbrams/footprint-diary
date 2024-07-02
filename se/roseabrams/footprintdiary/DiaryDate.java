package se.roseabrams.footprintdiary;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class DiaryDate implements Serializable, Comparable<DiaryDate> {

    public final short YEAR;
    public final byte MONTH;
    public final byte DAY;
    public transient GregorianCalendar detailedDate = null;
    public static final DiaryDate MIN = new DiaryDate((short) 2000, (byte) 1, (byte) 1);
    public static final DiaryDate MAX = new DiaryDate((short) 2025, (byte) 12, (byte) 31);

    public DiaryDate(short year, byte month, byte day) {
        assert (MIN == null || MAX == null) || (year >= MIN.YEAR && year < MAX.YEAR);
        assert month >= 1 && month <= 12;
        assert day >= 1 && month <= 31;

        YEAR = year;
        MONTH = month;
        DAY = day;
    }

    public DiaryDate(String dateString) {
        this(Short.parseShort(dateString.substring(0, 4)), Byte.parseByte(dateString.substring(5, 7)),
                Byte.parseByte(dateString.substring(8, 10)));
    }

    GregorianCalendar getDetailedDate() {
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

    public DiaryDate reduce() {
        return this.clone();
    }

    @Override
    public DiaryDate clone() {
        return new DiaryDate(YEAR, MONTH, DAY);
    }

    @Override
    public int compareTo(DiaryDate d2) {
        int comparedYear = Short.compare(YEAR, d2.YEAR);
        int comparedMonth = Byte.compare(MONTH, d2.MONTH);
        int comparedDay = Byte.compare(DAY, d2.DAY);

        if (comparedYear != 0)
            return comparedYear;
        if (comparedMonth != 0)
            return comparedMonth;
        return comparedDay;
    }

    @Override
    @Deprecated // better to explicitly specify strictness
    public boolean equals(Object obj) {
        return obj instanceof DiaryDate d2 && equals(d2, true);
    }

    public boolean equals(DiaryDate obj, boolean strict) {
        return compareTo(obj) == 0 && (strict ? !(obj instanceof DiaryDateTime) : true);
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean leadingZeroes) {
        if (leadingZeroes)
            return YEAR + "-" + (MONTH < 10 ? "0" + MONTH : MONTH) + "-" + (DAY < 10 ? "0" + DAY : DAY);
        else
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
