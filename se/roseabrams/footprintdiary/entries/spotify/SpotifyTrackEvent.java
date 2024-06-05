package se.roseabrams.footprintdiary.entries.spotify;

import java.net.URL;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.interfaces.Audio;
import se.roseabrams.footprintdiary.interfaces.RemoteResource;

public abstract class SpotifyTrackEvent extends DiaryEntry implements Audio, RemoteResource {

    public final SpotifyTrack TRACK;

    public SpotifyTrackEvent(DiaryDate dd, SpotifyTrack track) {
        super(DiaryEntryCategory.SPOTIFY, dd);
        TRACK = track;
    }

    @Override
    public String getPathToResource() {
        return TRACK.getUri().toString();
    }

    @Override
    public URL getUrlOfResource() {
        return TRACK.getUrl();
    }
}
