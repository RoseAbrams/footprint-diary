package se.roseabrams.footprintdiary.entries.spotify;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntrySource;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.interfaces.Audio;
import se.roseabrams.footprintdiary.interfaces.RemoteResource;

public class SpotifyPlayback extends DiaryEntry implements Audio, RemoteResource {

    public final SpotifyTrack SONG;
    // public final int USERNAME;
    public final String PLATFORM;
    public final int PLAYTIME;
    public final String COUNTRY;
    public final String IP; // TODO make better type
    public final String AGENT;
    public final StartReason START_REASON;
    public final EndReason END_REASON;
    public final boolean SHUFFLE;
    public final boolean SKIPPED;
    public final boolean OFFLINE;
    public final DiaryDateTime OFFLINE_START;
    public final boolean INCOGNITO;
    public static final int MY_USERNAME = 1144943067;

    public SpotifyPlayback(DiaryDateTime dd, SpotifyTrack song, String platform, int playtime,
            String country, String ip, String agent, StartReason startReason, EndReason endReason, boolean shuffle,
            boolean skipped, boolean offline, DiaryDateTime offlineStart, boolean incognito) {
        super(DiaryEntrySource.SPOTIFY, dd);
        SONG = song;
        PLATFORM = platform.intern();
        PLAYTIME = playtime;
        COUNTRY = country.intern();
        IP = ip.intern();
        AGENT = agent.intern();
        START_REASON = startReason;
        END_REASON = endReason;
        SHUFFLE = shuffle;
        SKIPPED = skipped;
        OFFLINE = offline;
        OFFLINE_START = offlineStart;
        INCOGNITO = incognito;
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

    @Override
    public boolean equals(Object o) {
        return o instanceof SpotifyPlayback && SONG.equals(((SpotifyPlayback) o).SONG);
    }

    public static enum StartReason {
        APPLOAD, BACKBTN, CLICKROW, CLICKSIDE, FWDBTN, PLAYBTN, POPUP, REMOTE, TRACKDONE, TRACKERROR, UNKNOWN, URIOPEN;
    }

    public static enum EndReason {
        BACKBTN, CLICKROW, CLICKSIDE, ENDPLAY, FWDBTN, LOGOUT, POPUP, REMOTE, TRACKDONE, TRACKERROR, UNEXPECTED_EXIT,
        UNEXPECTED_EXIT_WHILE_PAUSED, UNKNOWN, URIOPEN;
    }

    public static SpotifyPlayback[] createAllFromJson(File streamingFile) throws IOException {
        ArrayList<SpotifyPlayback> output = new ArrayList<>();

        JSONObject streamsO = Util.loadJsonFile(streamingFile, 1000000);
        JSONArray streams = new JSONArray(streamsO);
        for (Object stream : streams) {
            SpotifyPlayback p = createFromJson((JSONObject) stream);
            output.add(p);
        }

        return output.toArray(new SpotifyPlayback[output.size()]);
    }

    public static SpotifyPlayback createFromJson(JSONObject o) {
        DiaryDateTime dd = new DiaryDateTime(o.getString("ts"));
        int username = Integer.parseInt(o.getString("username"));
        assert username == MY_USERNAME; // might as well ascertain
        String platform = o.getString("platform");
        int playtime = o.getInt("ms_played");
        String country = o.getString("conn_country");
        String ip = o.getString("ip_addr_decrypted");
        String track = o.getString("master_metadata_track_name");
        String album = o.getString("master_metadata_album_artist_name");
        String artist = o.getString("master_metadata_album_album_name");
        String id = o.getString("spotify_track_uri");
        String agent = o.getString("user_agent_decrypted");
        if (agent.equals("unknown"))
            agent = null;
        StartReason sr = Util.findJsonInEnum(o.getString("reason_start"), StartReason.values());
        EndReason er = Util.findJsonInEnum(o.getString("reason_end"), EndReason.values());
        boolean shuffle = o.getBoolean("shuffle");
        boolean skipped = o.getBoolean("skipped");
        boolean offline = o.getBoolean("offline");
        DiaryDateTime offlineStart = new DiaryDateTime(o.getString("offline_timestamp"));
        boolean incognito_mode = o.getBoolean("incognito_mode");

        SpotifyTrack t = SpotifyTrack.create(id.substring(14), track, album, artist);
        SpotifyPlayback p = new SpotifyPlayback(dd, t, platform, playtime, country, ip, agent, sr, er, shuffle, skipped,
                offline, offlineStart, incognito_mode);
        return p;
    }
}
