package se.roseabrams.footprintdiary.entries.spotify;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentContainer;

public abstract class SpotifyTrackEvent extends DiaryEntry implements ContentContainer {

    public final SpotifyTrack TRACK;

    public SpotifyTrackEvent(DiaryDate dd, SpotifyTrack track) {
        super(DiaryEntryCategory.SPOTIFY, dd);
        TRACK = track;
    }

    @Override
    public Content getContent() {
        return TRACK;
    }

    @Override
    public String getStringSummary() {
        return TRACK != null ? TRACK.toString() : "[unknown track]";
    }
}
