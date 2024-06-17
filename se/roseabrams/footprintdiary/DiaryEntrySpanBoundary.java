package se.roseabrams.footprintdiary;

import se.roseabrams.footprintdiary.common.Metainfo;

public class DiaryEntrySpanBoundary extends DiaryEntry implements Metainfo {

    public final boolean IS_START; // otherwise start
    public final String DESCRIPTION;

    public DiaryEntrySpanBoundary(DiaryDate dd, boolean isEnd, String description) {
        super(DiaryEntryCategory.SPAN_BOUNDARY, dd);
        IS_START = isEnd;
        DESCRIPTION = description;
    }

    @Override
    public String getStringSummary() {
        return DESCRIPTION + (IS_START ? " START" : " END");
    }
}
