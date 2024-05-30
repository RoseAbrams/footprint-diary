package se.roseabrams.footprintdiary.entries.spotify;

import java.net.URL;

import org.json.JSONObject;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntrySource;
import se.roseabrams.footprintdiary.JsonableEnum;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.interfaces.Audio;
import se.roseabrams.footprintdiary.interfaces.RemoteResource;

public class SpotifyPlayback extends DiaryEntry implements Audio, RemoteResource {

    public final SpotifyTrack SONG;
    public final DiaryDateTime TIMESTAMP;
    public final String PLATFORM;
    public final int PLAYTIME;
    public final String COUNTRY;
    public final String IP; // make better type
    public final String AGENT;
    public final StartReason START_REASON;
    public final EndReason END_REASON;
    public final boolean SHUFFLE;
    public final boolean SKIPPED;
    public final boolean OFFLINE;
    public final DiaryDateTime OFFLINE_START;
    public final boolean INCOGNITO;
    public static final int MY_USERNAME = 1144943067;

    public SpotifyPlayback(DiaryDateTime dd) {
        super(DiaryEntrySource.SPOTIFY, dd);
    }

    @Override
    public String getPathToResource() {
        return SONG.getUri().toString();
    }

    @Override
    public String getStringSummary() {
        return SONG.toString() + " (" + getPlaytimeString() + ")";
    }

    public String getPlaytimeString() {
        return (PLAYTIME / 60000) + ":" + ((PLAYTIME % 60000) / 1000);
    }

    @Override
    public URL getURLOfResource() {
        return SONG.getUrl();
    }

    public static enum StartReason {
        APPLOAD, BACKBTN, CLICKROW, CLICKSIDE, FWDBTN, PLAYBTN, POPUP, REMOTE, TRACKDONE, TRACKERROR, UNKNOWN, URIOPEN;
    }

    public static enum EndReason {
        BACKBTN, CLICKROW, CLICKSIDE, ENDPLAY, FWDBTN, LOGOUT, POPUP, REMOTE, TRACKDONE, TRACKERROR, UNEXPECTED_EXIT,
        UNEXPECTED_EXIT_WHILE_PAUSED, UNKNOWN, URIOPEN;
    }

    public static SpotifyPlayback createFromJson(JSONObject p) {
        DiaryDateTime dd = new DiaryDateTime(p.getString("ts"));
        int username = Integer.parseInt(p.getString("username"));
        assert username == MY_USERNAME; // might as well ascertain
        String platform = p.getString("platform");
        int playtime = p.getInt("ms_played");
        String country = p.getString("conn_country");
        String ip = p.getString("ip_addr_decrypted");
        String agent = p.getString("user_agent_decrypted");
        if (agent.equals("unknown")) {
            agent = null;
        }
        StartReason sr = Util.findJsonInEnum(p.getString("reason_start"), StartReason.values());
        EndReason er = Util.findJsonInEnum(p.getString("reason_end"), EndReason.values());
        boolean shuffle = p.getBoolean("shuffle");
        boolean skipped = p.getBoolean("skipped");
        boolean offline = p.getBoolean("offline");
        DiaryDateTime offlineStart = new DiaryDateTime(p.getString("offline_timestamp"));
        boolean incognito_mode = p.getBoolean("incognito_mode");
    }
}
