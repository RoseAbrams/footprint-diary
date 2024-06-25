package se.roseabrams.footprintdiary.entries.youtube;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;

public abstract class YouTubeEvent extends DiaryEntry {

    public YouTubeEvent(DiaryDate dd) {
        super(DiaryEntryCategory.YOUTUBE, dd);
    }
}