package se.roseabrams.footprintdiary.entries.spotify;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.Util;

public class SpotifyPlayback extends SpotifyTrackEvent {

    // public final int USERNAME;
    public final String PLATFORM;
    public final int PLAYTIME; // milliseconds
    public final String COUNTRY;
    public final String IP; // can make into better type?
    public final String AGENT;
    public final StartReason START_REASON;
    public final EndReason END_REASON;
    public final boolean SHUFFLE;
    public final boolean SKIPPED;
    public final boolean OFFLINE;
    public final DiaryDateTime OFFLINE_START;
    public final boolean INCOGNITO;

    public SpotifyPlayback(DiaryDateTime dd, SpotifyTrack track, String platform, int playtime,
            String country, String ip, String agent, StartReason startReason, EndReason endReason, boolean shuffle,
            boolean skipped, boolean offline, DiaryDateTime offlineStart, boolean incognito) {
        super(dd, track);
        PLATFORM = platform.intern();
        PLAYTIME = playtime;
        COUNTRY = country.intern();
        IP = ip.intern();
        AGENT = agent != null ? agent.intern() : null;
        START_REASON = startReason;
        END_REASON = endReason;
        SHUFFLE = shuffle;
        SKIPPED = skipped;
        OFFLINE = offline;
        OFFLINE_START = offlineStart;
        INCOGNITO = incognito;
    }

    @Override
    public String getStringSummary() {
        return super.toString() + " (" + getPlaytimeString() + ")";
    }

    public String getPlaytimeString() {
        return (PLAYTIME / 60000) + ":" + ((PLAYTIME % 60000) / 1000);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SpotifyPlayback s2 && TRACK.equals(s2.TRACK);
    }

    public static enum StartReason {
        APPLOAD, BACKBTN, CLICKROW, CLICKSIDE, FWDBTN, PLAYBTN, POPUP, REMOTE, TRACKDONE, TRACKERROR, UNKNOWN, URIOPEN;
    }

    public static enum EndReason {
        BACKBTN, CLICKROW, CLICKSIDE, ENDPLAY, FWDBTN, LOGOUT, POPUP, REMOTE, TRACKDONE, TRACKERROR, UNEXPECTED_EXIT,
        UNEXPECTED_EXIT_WHILE_PAUSED, UNKNOWN, URIOPEN;
    }

    public static SpotifyPlayback[] createAllFromJson(File streamingFile) throws IOException {
        ArrayList<SpotifyPlayback> output = new ArrayList<>(50000);

        JSONArray streams = Util.readJsonArrayFile(streamingFile);
        for (Object stream : streams) {
            SpotifyPlayback p = createFromJson((JSONObject) stream);
            output.add(p);
        }

        return output.toArray(new SpotifyPlayback[output.size()]);
    }

    public static SpotifyPlayback createFromJson(JSONObject o) {
        DiaryDateTime dd = new DiaryDateTime(o.getString("ts"));
        int username = Integer.parseInt(o.getString("username"));
        assert username == PersonalConstants.SPOTIFY_USERNAME; // might as well be sure
        String platform = o.getString("platform");
        int playtime = o.getInt("ms_played");
        String country = o.getString("conn_country");
        String ip = o.getString("ip_addr_decrypted");
        String id = Util.jsonStringNullsafe(o, "spotify_track_uri");
        if (id != null)
            id = id.substring(14);
        String track = Util.jsonStringNullsafe(o, "master_metadata_track_name");
        String album = Util.jsonStringNullsafe(o, "master_metadata_album_album_name");
        String artist = Util.jsonStringNullsafe(o, "master_metadata_album_artist_name");
        String agent = o.getString("user_agent_decrypted");
        if (agent.equals("unknown"))
            agent = null;
        StartReason sr = Util.findJsonInEnum(o.getString("reason_start"), StartReason.values());
        EndReason er = Util.findJsonInEnum(o.getString("reason_end"), EndReason.values());
        boolean shuffle = o.getBoolean("shuffle");
        boolean skipped = Util.jsonBooleanNullsafe(o, "skipped");
        boolean offline = o.getBoolean("offline");
        // apparently mixture of seconds or milliseconds???
        long offlineStartL = o.getLong("offline_timestamp");
        DiaryDateTime offlineStart;
        if (offlineStartL == 0 || offlineStartL == 1) {
            offlineStart = null;
        } else if (offlineStartL < 1000000000000L) {
            offlineStart = new DiaryDateTime(offlineStartL * 1000);
        } else {
            offlineStart = new DiaryDateTime(offlineStartL);
        }
        boolean incognito_mode = o.getBoolean("incognito_mode");

        SpotifyTrack t = SpotifyTrack.create(id, track, album, artist);
        SpotifyPlayback p = new SpotifyPlayback(dd, t, platform, playtime, country, ip, agent, sr, er, shuffle, skipped,
                offline, offlineStart, incognito_mode);
        return p;
    }
}
