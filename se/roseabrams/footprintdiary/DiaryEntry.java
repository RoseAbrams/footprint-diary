package se.roseabrams.footprintdiary;

import java.io.Serializable;

import se.roseabrams.footprintdiary.interfaces.DiaryEntryData;

public abstract class DiaryEntry implements DiaryEntryData, Serializable {
    public final DiaryEntryCategory SOURCE;
    public final DiaryDate DATE;

    public DiaryEntry(DiaryEntryCategory source, DiaryDate dd) {
        assert source != null;
        assert dd != null;

        SOURCE = source;
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