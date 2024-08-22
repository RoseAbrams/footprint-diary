package se.roseabrams.footprintdiary;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class DiaryDate implements Serializable, Comparable<DiaryDate> {

    public final short YEAR;
    public final byte MONTH;
    public final byte DAY;
    public transient GregorianCalendar detailedDate = null;
    public static final short YEAR_MIN = 2000;
    public static final short YEAR_LOW = 2005;
    public static final short YEAR_MAX = 2050;
    public static final short YEAR_HIGH = 2025;
    private static final boolean DEFAULT_STRICTNESS = true;

    public DiaryDate(short year, byte month, byte day) {
        assert year >= YEAR_MIN && year < YEAR_MAX;
        assert month >= 1 && month <= 12;
        assert day >= 1 && day <= daysInMonth(year, month);

        YEAR = year;
        MONTH = month;
        DAY = day;

        if (year < YEAR_LOW || year > YEAR_HIGH)
            System.err.println("DiaryDate " + this + " is far from the expeted daterange. Is it correctly ingested?");
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

    int differenceDays(DiaryDate d2) {
        return differenceDays(this, d2);
    }

    static int differenceDays(DiaryDate d1, DiaryDate d2) {
        return (int) TimeUnit.MILLISECONDS
                .toDays(d2.getDetailedDate().getTime().getTime() - d1.getDetailedDate().getTime().getTime());
    }

    public DiaryDate reduce() {
        return this.clone();
    }

    protected DiaryDate yesterday() {
        return createValidated(YEAR, MONTH, (byte) (DAY - 1));
    }

    protected DiaryDate tomorrow() {
        return createValidated(YEAR, MONTH, (byte) (DAY + 1));
    }

    @Override
    public DiaryDate clone() {
        return new DiaryDate(YEAR, MONTH, DAY);
    }

    @Override
    @Deprecated // better to explicitly specify strictness
    public int compareTo(DiaryDate d2) {
        return compareTo(d2, DEFAULT_STRICTNESS);
    }

    public int compareTo(DiaryDate d2, boolean strict) {
        int comparedYear = Short.compare(YEAR, d2.YEAR);
        int comparedMonth = Byte.compare(MONTH, d2.MONTH);
        int comparedDay = Byte.compare(DAY, d2.DAY);

        if (comparedYear != 0)
            return comparedYear;
        if (comparedMonth != 0)
            return comparedMonth;
        if (comparedDay != 0)
            return comparedDay;

        if (!strict)
            return 0;

        if (this instanceof DiaryDateTime dt1) {
            if (d2 instanceof DiaryDateTime dt2) {
                int comparedHour = Byte.compare(dt1.HOUR, dt2.HOUR);
                int comparedMinute = Byte.compare(dt1.MINUTE, dt2.MINUTE);
                int comparedSecond = Byte.compare(dt1.SECOND, dt2.SECOND);

                if (comparedHour != 0)
                    return comparedHour;
                if (comparedMinute != 0)
                    return comparedMinute;
                return comparedSecond;
            } else
                // intent: if two DiaryDates are non-strictly equal but have differing precision, strict comparison returns that more precision is greater
                return 1;
        } else if (d2 instanceof DiaryDateTime)
            return -1;
        else
            return 0;
    }

    @Override
    @Deprecated // better to explicitly specify strictness
    public boolean equals(Object obj) {
        return obj instanceof DiaryDate d2 && equals(d2, DEFAULT_STRICTNESS);
    }

    public boolean equals(DiaryDate obj, boolean strict) {
        return compareTo(obj, strict) == 0;
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
            case "maj":
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
            case "okt":
                return 10;
            case "nov":
                return 11;
            case "dec":
                return 12;
            default:
                throw new IllegalArgumentException("Not valid month: " + monthName);
        }
    }

    protected static DiaryDate createValidated(short year, byte month, byte day) {
        while (month > 12) {
            year++;
            month -= 12;
        }
        while (month < 1) {
            year--;
            month += 12;
        }

        while (day > daysInMonth(year, month)) {
            month++;
            while (month > 12) {
                year++;
                month -= 12;
            }
            day -= daysInMonth(year, (byte) (month - 1));
        }
        while (day < 1) {
            month--;
            while (month < 1) {
                year--;
                month += 12;
            }
            day += daysInMonth(year, month);
        }
        return new DiaryDate(year, month, day);
    }

    private static byte daysInMonth(short year, byte month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return (byte) (year % 4 == 0 ? 29 : 28); // gregorian anomalies aren't important
            default:
                throw new AssertionError();
        }
    }
}
