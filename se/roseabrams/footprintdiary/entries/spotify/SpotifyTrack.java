package se.roseabrams.footprintdiary.entries.spotify;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import se.roseabrams.footprintdiary.common.ContentType;
import se.roseabrams.footprintdiary.common.Webpage;

public class SpotifyTrack extends Webpage {

    public final String ID;
    public final String NAME;
    public final String ALBUM;
    public final String ARTIST;
    private static final ArrayList<SpotifyTrack> CACHE = new ArrayList<>(10000);

    private SpotifyTrack(String id, String name, String album, String artist) {
        super("https://open.spotify.com/track/" + id, ContentType.AUDIO);
        ID = id;
        NAME = name;
        ALBUM = album.intern();
        ARTIST = artist.intern();
    }

    public URI getUri() {
        try {
            return new URI("spotify:track:" + ID);
        } catch (URISyntaxException e) {
            throw new AssertionError(e); // data checked at superconstructor so should never happen
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
            return null; // should not occur under current setup
        assert !id.isBlank() && id.length() == 62;
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
