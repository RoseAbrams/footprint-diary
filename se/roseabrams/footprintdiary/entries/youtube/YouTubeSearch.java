package se.roseabrams.footprintdiary.entries.youtube;

import se.roseabrams.footprintdiary.DiaryDateTime;

public class YouTubeSearch extends YouTubeEvent {

    public final String SEARCH_TERM;

    public YouTubeSearch(DiaryDateTime date, String searchTerm) {
        super(date);
        assert searchTerm != null && !searchTerm.isBlank();
        SEARCH_TERM = searchTerm;
    }

    @Override
    public String getStringSummary() {
        return "\"" + SEARCH_TERM + "\"";
    }
}
