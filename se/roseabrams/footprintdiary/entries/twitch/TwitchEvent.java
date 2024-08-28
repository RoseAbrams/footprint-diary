package se.roseabrams.footprintdiary.entries.twitch;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;

public abstract class TwitchEvent extends DiaryEntry {

    public TwitchEvent(DiaryDate dd) {
        super(DiaryEntryCategory.TWITCH, dd);
    }
}
