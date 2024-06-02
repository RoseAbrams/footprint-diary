package se.roseabrams.footprintdiary.entries.reddit;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;

public abstract class RedditEvent extends DiaryEntry {

    public RedditEvent(DiaryDate dd) {
        super(DiaryEntryCategory.REDDIT, dd);
    }
}
