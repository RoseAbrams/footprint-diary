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
        DiaryDate start = DiaryDate.MAX;
        DiaryDate end = DiaryDate.MIN;
        for (DiaryEntry entry : entries) {
            if (entry.DATE.compareTo(start) < 0) {
                start = entry.DATE.reduce();
            }
            if (entry.DATE.compareTo(end) > 0) {
                start = entry.DATE.reduce();
            }
        }
        DiaryDateSpan dds = new DiaryDateSpan(start, end);
        DiaryEntrySpanBoundary startB = new DiaryEntrySpanBoundary(start, true, description);
        DiaryEntrySpanBoundary endB = new DiaryEntrySpanBoundary(end, false, description);
        DiaryEntrySpanBoundary[] output = { startB, endB };
        return output;
    }
}
