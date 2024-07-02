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
    public final DiaryDateTime CREATED_DATE;
    public final DiaryDate START_DATE;
    public final DiaryDate END_DATE;
    public final int SEQUENCE;
    public final Webpage URL;

    public CalendarEvent(DiaryDateTime createdDate, DiaryDateTime startDate, DiaryDateTime endDate,
            String summary, String description, String location, int sequence, Webpage url) {
        super(DiaryEntryCategory.PHONE_CALENDAR, startDate);

        assert summary != null;

        SUMMARY = summary;
        DESCRIPTION = description;
        LOCATION = location;
        CREATED_DATE = createdDate;
        START_DATE = startDate.shouldReduce() ? startDate.reduce() : startDate;
        END_DATE = endDate.shouldReduce() ? endDate.reduce() : endDate;
        SEQUENCE = sequence;
        URL = url;
    }

    @Override
    public String getStringSummary() {
        if (START_DATE.equals(END_DATE, false)) {
            return SUMMARY + " (" + START_DATE.reduce() + "-" + END_DATE.reduce() + ")";
        } else {
            return SUMMARY + " (" + START_DATE.reduce() + ")";
        }
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
            } else if (icsLine.equals("END:VEVENT")) {
                assert currentIsEvent;
                currentIsEvent = false;
                CalendarEvent c = new CalendarEvent(new DiaryDateTime(createdDateS), new DiaryDateTime(startDateS),
                        new DiaryDateTime(endDateS), summary, description, location, Integer.parseInt(sequenceS),
                        new Webpage(urlS));
                output.add(c);
            } else if (icsLine.equals("BEGIN:VALARM")) {
                currentIsAlarm = true;
            } else if (icsLine.equals("END:VALARM")) {
                currentIsAlarm = false;
            } else if (icsLine.startsWith("CREATED:")) {
                createdDateS = icsLine.substring(icsLine.indexOf(":") + 1);
            } else if (icsLine.startsWith("DTSTART;")) {
                startDateS = icsLine.substring(icsLine.indexOf(";") + 1);
            } else if (icsLine.startsWith("DTEND;")) {
                endDateS = icsLine.substring(icsLine.indexOf(";") + 1);
            } else if (icsLine.startsWith("SUMMARY:")) {
                summary = icsLine.substring(icsLine.indexOf(":") + 1);
            } else if (icsLine.startsWith("DESCRIPTION:") && !currentIsAlarm) {
                description = icsLine.substring(icsLine.indexOf(":") + 1);
            } else if (icsLine.startsWith("LOCATION:")) {
                location = icsLine.substring(icsLine.indexOf(":") + 1);
            } else if (icsLine.startsWith("SEQUENCE:")) {
                sequenceS = icsLine.substring(icsLine.indexOf(":") + 1);
            } else if (icsLine.startsWith("URL;")) {
                urlS = icsLine.substring(icsLine.indexOf(":") + 1);
            }
        }
        return output.toArray(new CalendarEvent[output.size()]);
    }
}
