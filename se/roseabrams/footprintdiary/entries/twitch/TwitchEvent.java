package se.roseabrams.footprintdiary.entries.twitch;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;

abstract class TwitchEvent extends DiaryEntry {

    public final String CHANNEL;

    TwitchEvent(DiaryEntryCategory category, DiaryDate dd, String channel) {
        super(category, dd);
        assert !channel.isBlank();
        CHANNEL = channel.intern();
    }
}
