package se.roseabrams.footprintdiary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import se.roseabrams.footprintdiary.common.CustomCountable;

public class DiaryPage extends Diary implements Serializable {

    private final ArrayList<DiaryEntry> A = new ArrayList<>();
    private final HashMap<DiaryEntryCategory, ArrayList<DiaryEntry>> E = new HashMap<>();
    public final DiaryDate DATE;

    public DiaryPage(DiaryDate date) {
        DATE = date;
    }

    public void add(DiaryEntry e) {
        if (!DATE.equals(e.DATE, false))
            throw new IllegalArgumentException(
                    "DiaryEntry don't belong on this DiaryPage - expected " + DATE + ", got " + e.DATE);
        A.add(e);
        if (!E.containsKey(e.CATEGORY))
            E.putIfAbsent(e.CATEGORY, new ArrayList<>());
        E.get(e.CATEGORY).add(e);
    }

    public DiaryEntry randomEntry() {
        return A.get((int) (A.size() * Math.random()));
    }

    @Override
    public String toString() {
        return DATE + " (" + E.size() + ")";
    }

    public String sumsCsv() {
        StringBuilder output = new StringBuilder(50);
        output.append(DATE.toString());
        for (DiaryEntryCategory s : DiaryEntryCategory.valuesCustomOrder()) {
            output.append(",");
            ArrayList<DiaryEntry> d = E.get(s);
            if (d == null) {
                output.append(0);
            } else if (d.get(0) instanceof CustomCountable) {
                output.append(((CustomCountable) d.get(0)).getCustomCount());
                // } else if (d.get(0) instanceof Message) {
                // TODO count of all messages or just isByMe()?
            } else {
                output.append(d.size());
            }
        }
        return output.toString();
    }

    public String indexCsv() {
        StringBuilder output = new StringBuilder(1000);
        for (DiaryEntry e : A) {
            e.indexCsv(output, ",");
            output.append("\n");
        }
        return output.toString();
    }

    public String prose() {
        StringBuilder output = new StringBuilder(300);
        output.append("(").append(DATE.toString(true)).append(")\n\n\n");
        output.append("Dear diary,").append("\n");
        if (E.isEmpty()) {
            output.append("I have nothing to write today.").append("\n");
        } else {
            output.append("today I did ");
            if (E.size() == 1) {
                output.append("just one thing.");
            } else {
                output.append(E.size()).append(" different things.");
            }
            output.append("\n").append("\n");
            for (DiaryEntryCategory c : DiaryEntryCategory.valuesCustomOrder()) {
                if (E.containsKey(c) && c.enabled()) {
                    output.append(c.describeInProse(E.get(c))).append("\n");
                }
            }
        }
        output.append("\n").append("See you tomorrow.");
        return output.toString();
    }
}
