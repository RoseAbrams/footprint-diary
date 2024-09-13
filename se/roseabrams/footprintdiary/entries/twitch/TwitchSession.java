package se.roseabrams.footprintdiary.entries.twitch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.common.CustomCountable;

public class TwitchSession extends TwitchWatchEvent implements CustomCountable {

    private String[] streamCategories = new String[0];
    private int watchtimeMinutes = 0;

    public TwitchSession(DiaryDate dd, String channel) {
        super(dd, channel);
    }

    public List<String> getCategories() {
        return Arrays.asList(streamCategories);
    }

    @Override
    public int getWatchtimeMinutes() {
        return watchtimeMinutes;
    }

    @Override
    public String getStringSummary() {
        return "watched " + CHANNEL + " for " + watchtimeMinutes + " minutes";
    }

    @Override
    public int getCustomCount() {
        return watchtimeMinutes;
    }

    public static List<TwitchSession> createSessions(File watchFile) throws IOException {
        ArrayList<TwitchSession> output = new ArrayList<>(1000);
        List<TwitchPlayback> t = TwitchPlayback.createFromCsv(watchFile);
        for (TwitchPlayback p : t) {
            TwitchSession foundS = null;
            for (TwitchSession iS : output) {
                if (p.DATE.equals(iS.DATE, false)
                        && p.getChannelOrHosted().equals(iS.CHANNEL)) {
                    foundS = iS;
                    break;
                }
            }
            if (foundS == null) {
                foundS = new TwitchSession(p.DATE, p.CHANNEL);
                output.add(foundS);
            }
            foundS.streamCategories = Arrays.copyOf(foundS.streamCategories,
                    foundS.streamCategories.length + 1);
            foundS.streamCategories[foundS.streamCategories.length - 1] = p.STREAM_CATEGORY;
            foundS.watchtimeMinutes += p.WATCHTIME_MINUTES;
        }
        return output;
    }
}
