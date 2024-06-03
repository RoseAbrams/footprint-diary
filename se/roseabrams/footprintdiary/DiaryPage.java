package se.roseabrams.footprintdiary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import se.roseabrams.footprintdiary.interfaces.CustomCounted;

public class DiaryPage extends Diary implements Serializable {
    private final HashMap<DiaryEntryCategory, ArrayList<DiaryEntry>> E = new HashMap<>();
    public final DiaryDate DATE;

    public DiaryPage(DiaryDate date) {
        DATE = date;
    }

    public void add(DiaryEntry e) {
        if (!DATE.equals(e.DATE, false))
            throw new IllegalArgumentException(
                    "DiaryEntry don't belong on this DiaryPage - expected " + DATE + ", got " + e.DATE);
        if (!E.containsKey(e.CATEGORY))
            E.putIfAbsent(e.CATEGORY, new ArrayList<>());
        E.get(e.CATEGORY).add(e);
    }

    public String csv() {
        StringBuilder output = new StringBuilder(DATE.toString());
        for (DiaryEntryCategory s : DiaryEntryCategory.valuesCustomOrder()) {
            output.append(Util.DELIM);
            ArrayList<DiaryEntry> d = E.get(s);
            if (d == null) {
                output.append(0);
            } else if (d.get(0) instanceof CustomCounted) {
                output.append(((CustomCounted) d.get(0)).getCustomCount());
            } else {
                output.append(d.size());
            }
        }
        return output.toString();
    }

    public String prose() {
        StringBuilder output = new StringBuilder(250);
        output.append("Dear diary,").append(Util.NEWLINE);
        if (E.isEmpty()) {
            output.append("I have nothing to write today.").append(Util.NEWLINE);
        } else {
            output.append("today I did ");
            if (E.size() == 1) {
                output.append("just one thing.");
            } else {
                output.append(E.size()).append(" different things.");
            }
            output.append(Util.NEWLINE).append(Util.NEWLINE);
            for (DiaryEntryCategory s : DiaryEntryCategory.valuesCustomOrder()) {
                if (E.containsKey(s)) {
                    output.append(s.describeInProse(E.get(s))).append(Util.NEWLINE);
                }
            }
        }
        output.append(Util.NEWLINE).append("See you tomorrow.");
        return output.toString();
    }
}
