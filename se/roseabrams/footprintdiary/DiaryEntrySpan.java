package se.roseabrams.footprintdiary;

public class DiaryEntrySpan {

    public final DiaryEntrySpanBoundary EARLIEST;
    public final DiaryEntrySpanBoundary LATEST;

    private DiaryEntrySpan(DiaryDate earliest, DiaryDate latest, String description) {
        EARLIEST = new DiaryEntrySpanBoundary(earliest, false, description);
        LATEST = new DiaryEntrySpanBoundary(latest, true, description);
    }

    public static DiaryEntrySpan create(DiaryEntry[] entries, String description) {
        DiaryDate earliest = DiaryDate.MAX;
        DiaryDate latest = DiaryDate.MIN;
        for (DiaryEntry entry : entries) {
            if (entry.DATE.compareTo(earliest) < 0) {
                earliest = entry.DATE.reduce();
            }
            if (entry.DATE.compareTo(latest) > 0) {
                earliest = entry.DATE.reduce();
            }
        }
        return new DiaryEntrySpan(earliest, latest, description);
    }
}
