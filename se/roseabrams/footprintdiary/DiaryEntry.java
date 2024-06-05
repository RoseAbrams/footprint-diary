package se.roseabrams.footprintdiary;

import java.io.Serializable;

import se.roseabrams.footprintdiary.interfaces.DiaryEntryData;

public abstract class DiaryEntry implements DiaryEntryData, Serializable {
    public final DiaryEntryCategory CATEGORY;
    public final DiaryDate DATE;

    public DiaryEntry(DiaryEntryCategory category, DiaryDate dd) {
        assert category != null;
        assert dd != null;

        CATEGORY = category;
        DATE = dd;
    }

    public boolean equals(Object obj) {
        return obj instanceof DiaryEntry && getClass().equals(obj.getClass())
                && DATE.equals(((DiaryEntry) obj).DATE, true);
    }

    @Override
    public final String toString() {
        return getStringSummary();
    }

    public String csvIndex() {
        return csvIndex(new StringBuilder(50)).toString();
    }

    public StringBuilder csvIndex(StringBuilder s) {
        return s.append(DATE.toString(true)).append(Util.DELIM).append(CATEGORY).append(Util.DELIM)
                .append(getClass().getName()).append(Util.DELIM).append('\"').append(getStringSummary()).append('\"');
    }
}