package se.roseabrams.footprintdiary;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

public class DiaryDateTime extends DiaryDate {

    public final byte HOUR;
    public final byte MINUTE;
    public final byte SECOND;

    public DiaryDateTime(short year, byte month, byte day, byte hour, byte minute, byte second) {
        super(year, month, day);

        if (hour < 0 || hour >= 24)
            throw new DiaryDateException("Invalid hour: " + year);
        if (minute < 0 || minute >= 60)
            throw new DiaryDateException("Invalid minute: " + year);
        if (second < 0 || second >= 60)
            throw new DiaryDateException("Invalid second: " + year);

        HOUR = hour;
        MINUTE = minute;
        SECOND = second;
    }

    public DiaryDateTime(DiaryDate date, Byte hour, Byte minute, Byte second) {
        this(date.YEAR, date.MONTH, date.DAY, hour, minute, second);
    }

    public DiaryDateTime(String dateString) {
        this(Short.parseShort(dateString.substring(0, 4)), Byte.parseByte(dateString.substring(5, 7)),
                Byte.parseByte(dateString.substring(8, 10)), Byte.parseByte(dateString.substring(11, 13)),
                Byte.parseByte(dateString.substring(14, 16)), Byte.parseByte(dateString.substring(17, 19)));
    }

    public DiaryDateTime(java.util.Date javaDate) {
        this(javaDate.getTime());
    }

    public DiaryDateTime(long unixMs) {
        this(new Date(unixMs > 10000000000L ? unixMs : unixMs * 1000).toInstant().atZone(ZoneId.systemDefault()));
    }

    public DiaryDateTime(GregorianCalendar c) {
        this((short) c.get(GregorianCalendar.YEAR), (byte) c.get(GregorianCalendar.MONTH),
                (byte) c.get(GregorianCalendar.DAY_OF_MONTH), (byte) c.get(GregorianCalendar.HOUR),
                (byte) c.get(GregorianCalendar.MINUTE), (byte) c.get(GregorianCalendar.SECOND));
    }

    public DiaryDateTime(ZonedDateTime zdt) {
        this((short) zdt.getYear(), (byte) zdt.getMonth().getValue(), (byte) zdt.getDayOfMonth(),
                (byte) zdt.getHour(), (byte) zdt.getMinute(), (byte) zdt.getSecond());
    }

    @Override
    GregorianCalendar getDetailedDate() {
        if (detailedDate != null)
            return detailedDate;
        detailedDate = new GregorianCalendar(YEAR, MONTH - 1, DAY, HOUR, MINUTE, SECOND);
        return detailedDate;
    }

    public boolean shouldReduce() {
        return HOUR == 0 && MINUTE == 0 && SECOND == 0;
    }

    @Override
    public DiaryDate reduce() {
        return super.clone();
    }

    @Override
    public DiaryDateTime clone() {
        return new DiaryDateTime(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
    }

    @Override
    public String toString(boolean leadingZeroes) {
        if (leadingZeroes)
            return super.toString(leadingZeroes) + "T" + (HOUR < 10 ? "0" + HOUR : HOUR) + ":"
                    + (MINUTE < 10 ? "0" + MINUTE : MINUTE) + ":" + (SECOND < 10 ? "0" + SECOND : SECOND);
        else
            return super.toString(leadingZeroes) + "T" + HOUR + ":" + MINUTE + ":" + SECOND;
    }
}
