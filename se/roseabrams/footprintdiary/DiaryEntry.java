package se.roseabrams.footprintdiary;

import java.io.Serializable;
import se.roseabrams.footprintdiary.interfaces.DiaryEntryData;

public abstract class DiaryEntry implements DiaryEntryData, Serializable {
    public final EntrySource SOURCE;
    public final DiaryDate DATE;
    public static final short CUTOFF_YEAR = 2023;

    public DiaryEntry(EntrySource source, DiaryDate dd) {
        assert source != null;

        SOURCE = source;
        DATE = dd;
    }

    public boolean equals(Object obj) {
        return obj instanceof DiaryEntry && this.getClass().equals(obj.getClass())
                && DATE.equals(((DiaryEntry) obj).DATE);
    }

    @Override
    public String toString() {
        return getStringSummary();
    }
}