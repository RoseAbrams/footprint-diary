package se.roseabrams.footprintdiary.entries.twitch;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntryCategory;

abstract class TwitchWatchEvent extends TwitchEvent {

    public final int WATCHTIME_MINUTES;

    TwitchWatchEvent(DiaryDate dd, String channel, int watchtimeMinutes) {
        super(DiaryEntryCategory.TWITCH_PLAYBACK, dd, channel);
        assert watchtimeMinutes > 0;
        WATCHTIME_MINUTES = watchtimeMinutes;
    }
}
