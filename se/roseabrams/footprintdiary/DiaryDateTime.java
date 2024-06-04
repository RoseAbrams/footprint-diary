package se.roseabrams.footprintdiary;

import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;

public class DiaryDateTime extends DiaryDate {
    public final byte HOUR;
    public final byte MINUTE;
    public final byte SECOND;

    public DiaryDateTime(short year, byte month, byte day, byte hour, byte minute, byte second) {
        super(year, month, day);

        assert hour >= 0 && hour < 24;
        assert minute >= 0 && minute < 60;
        assert second >= 0 && second < 60;

        this.HOUR = hour;
        this.MINUTE = minute;
        this.SECOND = second;
    }

    public DiaryDateTime(String dateString) {
        this(Short.parseShort(dateString.substring(0, 4)), Byte.parseByte(dateString.substring(5, 7)),
                Byte.parseByte(dateString.substring(8, 10)), Byte.parseByte(dateString.substring(11, 13)),
                Byte.parseByte(dateString.substring(14, 16)), Byte.parseByte(dateString.substring(17, 19)));
    }

    public DiaryDateTime(long unixMs) {
        this(new Date(unixMs).toInstant().atZone(ZoneId.systemDefault()).toString());
    }

    public DiaryDateTime(GregorianCalendar c) {
        this((short) c.get(GregorianCalendar.YEAR), (byte) c.get(GregorianCalendar.MONTH),
                (byte) c.get(GregorianCalendar.DAY_OF_MONTH), (byte) c.get(GregorianCalendar.HOUR),
                (byte) c.get(GregorianCalendar.MINUTE), (byte) c.get(GregorianCalendar.SECOND));
    }

    public GregorianCalendar getDetailedDate() {
        if (detailedDate != null)
            return detailedDate;
        detailedDate = new GregorianCalendar(YEAR, MONTH - 1, DAY, HOUR, MINUTE, SECOND);
        return detailedDate;
    }

    public DiaryDate getReduced() {
        return new DiaryDate(YEAR, MONTH, DAY);
    }

    @Override
    public int compareTo(DiaryDate d2) {
        int comparedSuper = super.compareTo(d2);
        if (comparedSuper != 0)
            return comparedSuper;

        if (!(d2 instanceof DiaryDateTime))
            return 0;

        int comparedHour = Byte.compare(HOUR, ((DiaryDateTime) d2).HOUR);
        int comparedMinute = Byte.compare(MINUTE, ((DiaryDateTime) d2).MINUTE);
        int comparedSecond = Byte.compare(SECOND, ((DiaryDateTime) d2).SECOND);

        if (comparedHour != 0) {
            return comparedHour;
        }
        if (comparedMinute != 0) {
            return comparedMinute;
        }
        return comparedSecond;
    }

    @Override
    public boolean equals(DiaryDate obj, boolean strict) {
        return compareTo(obj) == 0 && (strict ? obj instanceof DiaryDateTime : true);
    }

    @Override
    public String toString(boolean leadingZeroes) {
        if (leadingZeroes) {
            return super.toString() + "T" + (HOUR < 10 ? "0" + HOUR : HOUR) + ":"
                    + (MINUTE < 10 ? "0" + MINUTE : MINUTE) + ":" + (SECOND < 10 ? "0" + SECOND : SECOND);
        } else {
            return super.toString() + "T" + HOUR + ":" + MINUTE + ":" + SECOND;
        }
    }
}
