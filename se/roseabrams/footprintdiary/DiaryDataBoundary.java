package se.roseabrams.footprintdiary;

import java.util.List;

public class DiaryDataBoundary extends DiaryEntry {

    public final boolean IS_START;
    public final DiaryIngestCategory INGEST_CATEGORY;

    DiaryDataBoundary(DiaryDate date, boolean isStart, DiaryIngestCategory ingestCategory) {
        super(DiaryEntryCategory.DATA_BOUNDARY, date);
        IS_START = isStart;
        INGEST_CATEGORY = ingestCategory;
    }

    @Override
    public String getStringSummary() {
        return "METAINFO: data " + (IS_START ? "start" : "end") + " for " + INGEST_CATEGORY;
    }

    static DiaryDataBoundary[] createForList(List<DiaryEntry> de, DiaryIngestCategory c) {
        DiaryDate earliest = null;
        DiaryDate latest = null;
        for (DiaryEntry e : de) {
            if (earliest == null || earliest.compareTo(e.DATE, false) > 0)
                earliest = e.DATE;
            else if (latest == null || latest.compareTo(e.DATE, false) < 0)
                latest = e.DATE;
        }
        DiaryDataBoundary[] output = {
                new DiaryDataBoundary(earliest.reduce(), true, c),
                new DiaryDataBoundary(latest.reduce(), false, c)
        };
        return output;
    }
}
