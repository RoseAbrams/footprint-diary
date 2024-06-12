package se.roseabrams.footprintdiary.entries.facebook;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;

public abstract class FacebookWallEvent extends DiaryEntry {

    public FacebookWallEvent(DiaryDate dd) {
        super(DiaryEntryCategory.FACEBOOK_WALL, dd);
    }
}
