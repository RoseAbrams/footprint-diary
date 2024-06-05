package se.roseabrams.footprintdiary;

public class DataAvailabilitySpan {

    public final DataAvailabilityLimit EARLIEST;
    public final DataAvailabilityLimit LATEST;
    public final String DESCRIPTION;
    ... // not sure about how to do this best yet

    private DataAvailabilitySpan(DiaryDate earliest, DiaryDate latest, String description) {
        EARLIEST = new DataAvailabilityLimit(earliest, false);
        LATEST = new DataAvailabilityLimit(latest, true);
        DESCRIPTION = description;
    }

    public static DataAvailabilitySpan create(DiaryEntry[] entries, String description) {
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
        return new DataAvailabilitySpan(earliest, latest, description);
    }
}
