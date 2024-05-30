package se.roseabrams.footprintdiary;

import java.util.ArrayList;
import java.util.HashMap;

public class DiaryPage extends Diary {
    private final HashMap<DiaryEntrySource, ArrayList<DiaryEntry>> E = new HashMap<>();
    public final DiaryDate DATE;
    private static final ArrayList<DiaryEntry> EMPTY_LIST = new ArrayList<>(0);

    public DiaryPage(DiaryDate date) {
        DATE = date;
    }

    public void add(DiaryEntry e) {
        if (!DATE.equals(e.DATE, false))
            throw new IllegalArgumentException(
                    "DiaryEntry don't belong on this DiaryPage - expected " + DATE + ", got " + e.DATE);
        if (!E.containsKey(e.SOURCE)) {
            E.putIfAbsent(e.SOURCE, new ArrayList<>());
        }
        E.get(e.SOURCE).add(e);
    }

    public String csv(String delim) {
        StringBuilder output = new StringBuilder(DATE.toString());
        for (DiaryEntrySource s : DiaryEntrySource.values()) {
            output.append(delim + E.getOrDefault(s, EMPTY_LIST).size());
        }
        return output.toString();
    }

    public String prose(String newline) {
        StringBuilder output = new StringBuilder(250);
        output.append("Dear diary,").append(newline).append("today I did ").append(E.size())
                .append(" different things.").append(newline).append(newline);
        for (DiaryEntrySource s : DiaryEntrySource.values()) {
            if (E.containsKey(s)) {
                ArrayList<DiaryEntry> es = E.get(s);
                // where to put the logic and stringspecs?
                output.append(newline);
            }
        }
        output.append(newline).append("See you tomorrow.");
        return output.toString();
    }
}
