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

    public DiaryDate(GregorianCalendar c) {
        this((short) c.get(GregorianCalendar.YEAR), (byte) c.get(GregorianCalendar.MONTH),
                (byte) c.get(GregorianCalendar.DAY_OF_MONTH));
    }

    public GregorianCalendar getDetailedDate() {
        if (detailedDate != null)
            return detailedDate;
        detailedDate = new GregorianCalendar(YEAR, MONTH, DAY);
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
    public String toString() {
        return YEAR + "-" + MONTH + "-" + DAY;
    }
}
