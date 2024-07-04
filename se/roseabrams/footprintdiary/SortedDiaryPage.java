package se.roseabrams.footprintdiary;

import java.util.ArrayList;
import java.util.HashMap;
import se.roseabrams.footprintdiary.common.CustomCountable;

public class SortedDiaryPage extends DiaryPage {

    private final HashMap<DiaryEntryCategory, ArrayList<DiaryEntry>> E = new HashMap<>();

    public SortedDiaryPage(DiaryDate date) {
        super(date);
    }

    public boolean add(DiaryEntry e) {
        if (!DATE.equals(e.DATE, false))
            throw new IllegalArgumentException(
                    "DiaryEntry don't belong on this DiaryPage - expected " + DATE + ", got " + e.DATE);
        if (!E.containsKey(e.CATEGORY))
            E.putIfAbsent(e.CATEGORY, new ArrayList<>());
        E.get(e.CATEGORY).add(e);
        return true;
    }

    public DiaryEntry randomEntry() {
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
        for (ArrayList<DiaryEntry> es : E.values()) {
            for (DiaryEntry e : es) {
                e.indexCsv(output, ",");
                output.append("\n");
            }
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
            output.append(proseIteration());
        }
        output.append("\n").append("See you tomorrow.");
        return output.toString();
    }

    protected StringBuilder proseIteration() {
        StringBuilder output = new StringBuilder(300);
        for (DiaryEntryCategory c : DiaryEntryCategory.valuesCustomOrder()) {
            if (E.containsKey(c) && c.enabled()) {
                output.append(c.describeInProse(E.get(c))).append("\n");
            }
        }
        return output;
    }
}
