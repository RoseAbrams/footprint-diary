package se.roseabrams.footprintdiary;

public class DiaryEntrySpanBoundary extends DiaryEntry {

    public final boolean IS_START; // otherwise end
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
