package se.roseabrams.footprintdiary;

import java.io.Serializable;
import java.util.Date;

public class DiaryDate implements Serializable, Comparable<DiaryDate> {
    public final short YEAR;
    public final byte MONTH;
    public final byte DAY;
    public final byte HOUR;
    public final byte MINUTE;
    public final byte SECOND;
    public transient Date detailedDate = null;
    public static final byte UNDEFINED_FLAG = -1;

    public DiaryDate(short year, byte month, byte day) {
        this(year, month, day, UNDEFINED_FLAG, UNDEFINED_FLAG, UNDEFINED_FLAG);
    }

    public DiaryDate(short year, byte month, byte day, byte hour, byte minute, byte second) {
        assert year >= 2000 && year < 2025;
        assert month >= 1 && month <= 12;
        assert day >= 1 && month <= 31;
        assert (hour >= 0 && hour < 24) || hour == UNDEFINED_FLAG;
        assert (minute >= 0 && minute < 60) || minute == UNDEFINED_FLAG;
        assert (second >= 0 && second < 60) || second == UNDEFINED_FLAG;

        this.YEAR = year;
        this.MONTH = month;
        this.DAY = day;
        this.HOUR = hour;
        this.MINUTE = minute;
        this.SECOND = second;
    }

    public Date getDetailedDate() {
        if (detailedDate != null)
            return detailedDate;
        if (hasSecondPrecision()) {
            detailedDate = new Date(YEAR - 1900, MONTH - 1, DAY, HOUR, MINUTE, SECOND);
        } else if (hasMinutePrecision()) {
            detailedDate = new Date(YEAR - 1900, MONTH - 1, DAY, HOUR, MINUTE);
        } else {
            detailedDate = new Date(YEAR - 1900, MONTH - 1, DAY);
        }
        return detailedDate;
    }

    public String toStringWithTime() {
        if (!hasMinutePrecision()) {
            throw new UnsupportedOperationException("This object has no time precision.");
        }
        return HOUR + ":" + MINUTE + (hasSecondPrecision() ? ":" + SECOND : "");
    }

    public boolean hasMinutePrecision() {
        return HOUR != UNDEFINED_FLAG && MINUTE != UNDEFINED_FLAG;
    }

    public boolean hasSecondPrecision() {
        return hasMinutePrecision() && SECOND != UNDEFINED_FLAG;
    }

    @Override
    public int compareTo(DiaryDate dd) {
        int comparedYear = Short.compare(YEAR, dd.YEAR);
        int comparedMonth = Short.compare(MONTH, dd.MONTH);
        int comparedDay = Short.compare(DAY, dd.DAY);
        int comparedHour = Short.compare(HOUR, dd.HOUR);
        int comparedMinute = Short.compare(MINUTE, dd.MINUTE);
        int comparedSecond = Short.compare(SECOND, dd.SECOND);

        if (comparedYear != 0) {
            return comparedYear;
        }
        if (comparedMonth != 0) {
            return comparedMonth;
        }
        if (comparedDay != 0 || !hasMinutePrecision()) {
            return comparedDay;
        }
        if (comparedHour != 0) {
            return comparedMonth;
        }
        if (comparedMinute != 0 || !hasSecondPrecision()) {
            return comparedMonth;
        }
        return comparedSecond;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DiaryDate && compareTo((DiaryDate) obj) == 0;
    }

    @Override
    public String toString() {
        return YEAR + "-" + MONTH + "-" + DAY;
    }
}
