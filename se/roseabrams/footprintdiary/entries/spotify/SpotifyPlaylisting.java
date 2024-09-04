package se.roseabrams.footprintdiary.entries.spotify;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.Util;

public class SpotifyPlaylisting extends SpotifyTrackEvent {

    public final SpotifyPlaylist PLAYLIST;

    public SpotifyPlaylisting(DiaryDate dd, SpotifyTrack song, SpotifyPlaylist playlist) {
        super(dd, song);
        PLAYLIST = playlist;
    }

    @Override
    public String getStringSummary() {
        return super.toString() + " --> " + PLAYLIST;
    }

    public static List<SpotifyPlaylisting> createFromJson(File playlistFile) throws IOException {
        ArrayList<SpotifyPlaylisting> output = new ArrayList<>(5000);

        JSONObject playlistsO = Util.readJsonObjectFile(playlistFile);
        JSONArray playlists = playlistsO.getJSONArray("playlists");

        for (Object playlistO : playlists) {
            JSONObject playlist = (JSONObject) playlistO;
            String playlistName = playlist.getString("name");
            DiaryDate modified = new DiaryDate(playlist.getString("lastModifiedDate"));
            String desc = Util.jsonStringNullsafe(playlist, "description");

            SpotifyPlaylist pl = new SpotifyPlaylist(playlistName, desc, modified);

            JSONArray items = playlist.getJSONArray("items");
            for (Object itemO : items) {
                JSONObject item = (JSONObject) itemO;
                DiaryDate added = new DiaryDate(item.getString("addedDate"));
                SpotifyTrack t;
                if (!item.isNull("track")) {
                    JSONObject track = item.getJSONObject("track");
                    String name = track.getString("trackName");
                    String artist = track.getString("artistName");
                    String album = track.getString("albumName");
                    String id = track.getString("trackUri");
                    t = SpotifyTrack.getOrCreate(id.substring(14), name, album, artist);
                } else if (!item.isNull("localTrack")) {
                    t = null; // probably not worth it, very little data
                } else {
                    throw new AssertionError();
                }

                output.add(new SpotifyPlaylisting(added, t, pl));
            }
        }

        return output;
    }
}
