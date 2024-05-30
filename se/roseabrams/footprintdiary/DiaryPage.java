package se.roseabrams.footprintdiary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DiaryPage extends Diary implements Serializable {
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
        if (!E.containsKey(e.SOURCE))
            E.putIfAbsent(e.SOURCE, new ArrayList<>());
        E.get(e.SOURCE).add(e);
    }

    public String csv() {
        StringBuilder output = new StringBuilder(DATE.toString());
        for (DiaryEntrySource s : DiaryEntrySource.values()) {
            output.append(Util.DELIM).append(E.getOrDefault(s, EMPTY_LIST).size());
        }
        return output.toString();
    }

    public String prose() {
        StringBuilder output = new StringBuilder(250);
        output.append("Dear diary,").append(Util.NEWLINE).append("today I did ").append(E.size())
                .append(" different things.").append(Util.NEWLINE).append(Util.NEWLINE);
        for (DiaryEntrySource s : DiaryEntrySource.values()) {
            assert E.containsKey(s);
            ArrayList<DiaryEntry> es = E.get(s);
            output.append(s.prose(es));
            output.append(Util.NEWLINE);
        }
        output.append(Util.NEWLINE).append("See you tomorrow.");
        return output.toString();
    }
}
