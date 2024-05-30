package se.roseabrams.footprintdiary.entries.spotify;

import se.roseabrams.footprintdiary.DiaryDate;

public class SpotifyPlaylist {
    public final String NAME;
    public final String DESCRIPTION;
    public final DiaryDate LAST_MODIFIED;

    public SpotifyPlaylist(String name, String desc, DiaryDate modified) {
        NAME = name;
        DESCRIPTION = desc;
        LAST_MODIFIED = modified;
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
