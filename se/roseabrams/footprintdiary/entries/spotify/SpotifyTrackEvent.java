package se.roseabrams.footprintdiary.entries.spotify;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.content.Content;
import se.roseabrams.footprintdiary.interfaces.ContentOwner;

public abstract class SpotifyTrackEvent extends DiaryEntry implements ContentOwner {

    public final SpotifyTrack TRACK;

    public SpotifyTrackEvent(DiaryDate dd, SpotifyTrack track) {
        super(DiaryEntryCategory.SPOTIFY, dd);
        TRACK = track;
    }

    @Override
    public Content getContent() {
        return TRACK;
    }
}
