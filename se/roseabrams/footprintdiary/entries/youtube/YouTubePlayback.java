package se.roseabrams.footprintdiary.entries.youtube;

import se.roseabrams.footprintdiary.DiaryDate;

public class YouTubePlayback extends YouTubeVideoEvent {

    public final boolean IS_AD;

    public YouTubePlayback(DiaryDate dd, YouTubeVideo video, boolean isAd) {
        super(dd, video);
        IS_AD = isAd;
    }

    @Override
    public String getStringSummary() {
        return VIDEO.toString();
    }
}
