package se.roseabrams.footprintdiary.entries.apple;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.common.Webpage;

public class CalendarEvent extends DiaryEntry {

    public final String SUMMARY;
    public final String DESCRIPTION;
    public final String LOCATION;
    public final DiaryDate CREATED_DATE;
    public final DiaryDate START_DATE;
    public final DiaryDate END_DATE;
    public final int SEQUENCE;
    public final Webpage URL;

    public CalendarEvent(DiaryDate createdDate, DiaryDate startDate, DiaryDate endDate,
            String summary, String description, String location, int sequence, Webpage url) {
        super(DiaryEntryCategory.CALENDAR, startDate);

        assert startDate != null;
        assert endDate != null;
        assert summary != null;

        SUMMARY = summary;
        DESCRIPTION = description;
        LOCATION = location;
        CREATED_DATE = createdDate;
        START_DATE = startDate;
        END_DATE = endDate;
        SEQUENCE = sequence;
        URL = url;
    }

    @Override
    public String getStringSummary() {
        if (START_DATE.equals(END_DATE, false))
            return SUMMARY + " (" + START_DATE.reduce() + " – " + END_DATE.reduce() + ")";
        else
            return SUMMARY + " (" + START_DATE.reduce() + ")";
    }

    public static CalendarEvent[] createFromIcs(File calendarIcs) throws IOException {
        ArrayList<CalendarEvent> output = new ArrayList<>(1000);
        List<String> icsLines = Util.readFileLines(calendarIcs);

        boolean currentIsEvent = false;
        boolean currentIsAlarm = false;
        String summary = null;
        String description = null;
        String location = null;
        String createdDateS = null;
        String startDateS = null;
        String endDateS = null;
        String sequenceS = null;
        String urlS = null;
        for (String icsLine : icsLines) {
            if (icsLine.equals("BEGIN:VEVENT")) {
                assert !currentIsEvent;
                currentIsEvent = true;
                summary = null;
                description = null;
                location = null;
                createdDateS = null;
                startDateS = null;
                endDateS = null;
                sequenceS = null;
                urlS = null;
                continue;
            } else if (icsLine.equals("END:VEVENT")) {
                assert currentIsEvent;
                currentIsEvent = false;
                if (startDateS == null || endDateS == null) {
                    System.err.println(
                            "CalendarEvent \"" + summary + "\" lacks one or both dates (" + startDateS + " – "
                                    + endDateS + "). This entry will be scrapped!");
                    continue;
                }
                if (createdDateS == null)
                    System.err.println(
                            "CalendarEvent \"" + summary + "\" lacks createdDate, will default to null");
                CalendarEvent c = new CalendarEvent(createdDateS != null ? parseDate(createdDateS) : null,
                        parseDate(startDateS), parseDate(endDateS), summary, description, location,
                        Integer.parseInt(sequenceS), urlS == null || urlS.isEmpty() ? null : new Webpage(urlS));
                output.add(c);
                continue;
            } else if (icsLine.equals("BEGIN:VALARM")) {
                currentIsAlarm = true;
                continue;
            } else if (icsLine.equals("END:VALARM")) {
                currentIsAlarm = false;
                continue;
            } else if (icsLine.startsWith("CREATED:")) {
                createdDateS = icsLine.substring(icsLine.indexOf(":") + 1);
                if (createdDateS.contains(":"))
                    createdDateS = createdDateS.substring(createdDateS.indexOf(":") + 1);
                continue;
            } else if (icsLine.startsWith("DTSTART;")) {
                startDateS = icsLine.substring(icsLine.indexOf(";") + 1);
                if (startDateS.contains(":"))
                    startDateS = startDateS.substring(startDateS.indexOf(":") + 1);
                continue;
            } else if (icsLine.startsWith("DTEND;")) {
                endDateS = icsLine.substring(icsLine.indexOf(";") + 1);
                if (endDateS.contains(":"))
                    endDateS = endDateS.substring(endDateS.indexOf(":") + 1);
                continue;
            } else if (icsLine.startsWith("SUMMARY:")) {
                summary = icsLine.substring(icsLine.indexOf(":") + 1);
                continue;
            } else if (icsLine.startsWith("DESCRIPTION:") && !currentIsAlarm) {
                description = icsLine.substring(icsLine.indexOf(":") + 1);
                description = description.replace("\\n", "\n").replace("\\,", ",");
                continue;
            } else if (icsLine.startsWith("LOCATION:")) {
                location = icsLine.substring(icsLine.indexOf(":") + 1);
                location = location.replace("\\n", "\n").replace("\\,", ",");
                continue;
            } else if (icsLine.startsWith("SEQUENCE:")) {
                sequenceS = icsLine.substring(icsLine.indexOf(":") + 1);
                continue;
            } else if (icsLine.startsWith("URL;")) {
                urlS = icsLine.substring(icsLine.indexOf(":") + 1);
                if (urlS.startsWith("sms:"))
                    urlS = null; // TODO make proper solution
                continue;
            }
        }
        return output.toArray(new CalendarEvent[output.size()]);
    }

    private static DiaryDate parseDate(String s) {
        switch (s.length()) {
            case 8:
                return new DiaryDate(Short.parseShort(s.substring(0, 4)), Byte.parseByte(s.substring(4, 6)),
                        Byte.parseByte(s.substring(6, 8)));
            case 15:
            case 16:
                return new DiaryDateTime(Short.parseShort(s.substring(0, 4)), Byte.parseByte(s.substring(4, 6)),
                        Byte.parseByte(s.substring(6, 8)), Byte.parseByte(s.substring(9, 11)),
                        Byte.parseByte(s.substring(11, 13)), Byte.parseByte(s.substring(13, 15)));
            default:
                throw new AssertionError();
        }
    }
}
