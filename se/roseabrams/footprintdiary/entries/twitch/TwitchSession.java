package se.roseabrams.footprintdiary.entries.twitch;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.common.CustomCountable;

public class TwitchSession extends TwitchWatchEvent implements CustomCountable {
    // combine all watchminutes and chatmessages for a single channel and single day

    public final String[] STREAM_CATEGORIES;

    public TwitchSession(DiaryDate dd, String channel, String[] streamCategories, int watchtimeMinutes) {
        super(dd, channel, watchtimeMinutes);
        STREAM_CATEGORIES = streamCategories;
    }

    @Override
    public String getStringSummary() {
        return "watched " + CHANNEL + " for " + WATCHTIME_MINUTES + " minutes";
    }

    @Override
    public int getCustomCount() {
        return WATCHTIME_MINUTES;
    }
}
