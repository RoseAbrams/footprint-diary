package se.roseabrams.footprintdiary.entries.spotify;

import java.io.Serializable;

import se.roseabrams.footprintdiary.DiaryDate;

public class SpotifyPlaylist implements Serializable {

    public final String NAME;
    public final String DESCRIPTION;
    public final DiaryDate LAST_MODIFIED;
    // curiously no username with this data

    public SpotifyPlaylist(String name, String desc, DiaryDate modified) {
        NAME = name;
        DESCRIPTION = desc;
        LAST_MODIFIED = modified;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SpotifyPlaylist s2 && NAME.equals(s2.NAME);
    }
}
