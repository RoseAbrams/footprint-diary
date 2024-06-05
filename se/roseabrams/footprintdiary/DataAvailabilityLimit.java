package se.roseabrams.footprintdiary;

import se.roseabrams.footprintdiary.interfaces.Metainfo;

public class DataAvailabilityLimit extends DiaryEntry implements Metainfo {

    public final boolean IS_END; // otherwise start

    public DataAvailabilityLimit(DiaryDate dd, boolean isEnd) {
        super(DiaryEntryCategory.METADATA, dd);
        IS_END = isEnd;
    }

    @Override
    public String getStringSummary() {
    }
}
