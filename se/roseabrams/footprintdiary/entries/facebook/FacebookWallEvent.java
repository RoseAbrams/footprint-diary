package se.roseabrams.footprintdiary.entries.facebook;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;

public abstract class FacebookWallEvent extends DiaryEntry {

    public FacebookWallEvent(DiaryDate dd) {
        super(DiaryEntryCategory.FACEBOOK_WALL, dd);
    }

    /*
     * TODO future ingest:
     * profile_update_history.html
     * pages_you've_liked.html
     */

    static DiaryDateTime parseDate(String dateS) {
        boolean pm = dateS.substring(dateS.length() - 2).equals("pm");

        short year = Short.parseShort(dateS.substring(dateS.indexOf(",") + 2, dateS.indexOf(",") + 6));
        byte month = DiaryDate.parseMonthName(dateS.substring(0, 3));
        byte day = Byte.parseByte(dateS.substring(dateS.indexOf(" ") + 1, dateS.indexOf(",")));
        byte hour = Byte.parseByte(dateS.substring(dateS.indexOf(":") - 2, dateS.indexOf(":")).trim());
        if (pm) {
            if (hour != 12)
                hour += 12;
        } else if (hour == 12)
            hour = 0;
        byte minute = Byte.parseByte(dateS.substring(dateS.indexOf(":") + 1, dateS.indexOf(":") + 3));
        byte second = Byte.parseByte(dateS.substring(dateS.lastIndexOf(":") + 1, dateS.lastIndexOf(":") + 3));

        return new DiaryDateTime(year, month, day, hour, minute, second);
    }
}
