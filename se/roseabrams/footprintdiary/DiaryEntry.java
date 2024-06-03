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
        return obj instanceof DiaryEntry && this.getClass().equals(obj.getClass())
                && DATE.equals(((DiaryEntry) obj).DATE, true);
    }

    @Override
    public String toString() {
        return getStringSummary();
    }
}