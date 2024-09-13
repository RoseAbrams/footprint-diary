package se.roseabrams.footprintdiary.entries.twitch;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntryCategory;

abstract class TwitchWatchEvent extends TwitchEvent {

    TwitchWatchEvent(DiaryDate dd, String channel) {
        super(DiaryEntryCategory.TWITCH_PLAYBACK, dd, channel);
    }

    public abstract int getWatchtimeMinutes();
}
