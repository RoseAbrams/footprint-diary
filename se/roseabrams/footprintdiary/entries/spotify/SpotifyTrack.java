package se.roseabrams.footprintdiary.entries.spotify;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class SpotifyTrack {
    public final String ID;
    public final String NAME;
    public final String ALBUM;
    public final String ARTIST;
    private static final ArrayList<SpotifyTrack> CACHE = new ArrayList<>(1000);
    private static final SpotifyTrack NULL_TRACK = new SpotifyTrack(null, null, null, null);

    private SpotifyTrack(String id, String name, String album, String artist) {
        ID = id;
        NAME = name;
        ALBUM = album.intern();
        ARTIST = artist.intern();
    }

    public URI getUri() {
        try {
            return new URI("spotify:track:" + ID);
        } catch (URISyntaxException e) {
            throw new Error(e);
        }
    }

    public URL getUrl() {
        try {
            return new URL("https://open.spotify.com/track/" + ID);
        } catch (MalformedURLException e) {
            throw new Error(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SpotifyTrack && ID.equals(((SpotifyTrack) o).ID);
    }

    public String toString() {
        return ARTIST + " – " + NAME;
    }

    public static SpotifyTrack create(String id, String name, String album, String artist) {
        if (id == null)
            return NULL_TRACK;
        assert !name.isBlank() && id.length() == 62;
        assert name != null && !name.isBlank();
        assert album != null && !album.isBlank();
        assert artist != null && !artist.isBlank();

        for (SpotifyTrack t : CACHE) {
            if (id.equals(t.ID))
                return t;
        }

        SpotifyTrack t = new SpotifyTrack(id, name, album, artist);
        CACHE.add(t);
        return t;
    }
}
