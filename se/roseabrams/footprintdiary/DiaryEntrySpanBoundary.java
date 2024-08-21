package se.roseabrams.footprintdiary;

public class DiaryEntrySpanBoundary extends DiaryEntry {

    public final boolean IS_START; // otherwise end
    public final String DESCRIPTION;

    public DiaryEntrySpanBoundary(DiaryDate dd, boolean isStart, String description) {
        super(DiaryEntryCategory.SPAN_BOUNDARY, dd);
        IS_START = isStart;
        DESCRIPTION = description;
    }

    @Override
    public String getStringSummary() {
        return DESCRIPTION + (IS_START ? " START" : " END");
    }

    public static DiaryEntrySpanBoundary[] create(DiaryEntry[] entries, String description) {
        DiaryDate start = DiaryDate.YEAR_MAX;
        DiaryDate end = DiaryDate.YEAR_MIN;
        for (DiaryEntry entry : entries) {
            if (entry.DATE.compareTo(start, false) < 0) {
                start = entry.DATE;
            }
            if (entry.DATE.compareTo(end, false) > 0) {
                start = entry.DATE;
            }
        }
        DiaryDateSpan dds = new DiaryDateSpan(start.reduce(), end.reduce());
        DiaryEntrySpanBoundary startB = new DiaryEntrySpanBoundary(start.reduce(), true, description);
        DiaryEntrySpanBoundary endB = new DiaryEntrySpanBoundary(end.reduce(), false, description);
        DiaryEntrySpanBoundary[] output = { startB, endB };
        return output;
    }
}
