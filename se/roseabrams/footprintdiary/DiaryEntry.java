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
                && DATE.equals(((DiaryEntry) obj).DATE, true)
                && getStringSummary().equals(((DiaryEntry) obj).getStringSummary());
    }

    @Override
    public final String toString() {
        return getStringSummary();
    }

    public final String detailedCsv(String delim) {
        return detailedCsv(new StringBuilder(50), delim).toString();
    }

    public abstract StringBuilder detailedCsv(StringBuilder s, String delim);

    public final String indexCsv(String delim) {
        return indexCsv(new StringBuilder(50), delim).toString();
    }

    public StringBuilder indexCsv(StringBuilder s, String delim) {
        return s.append(DATE.toString(true)).append(delim).append(CATEGORY).append(delim).append(getClass().getName())
                .append(delim).append('\"').append(getStringSummary()).append('\"');
    }
}