package se.roseabrams.footprintdiary.entries.chrome;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentContainer;
import se.roseabrams.footprintdiary.common.Webpage;

/// seems to only go back to quite recently (one year?), so probably of little use for long-term diarying
public class WebHistory extends DiaryEntry implements ContentContainer {

    public final Webpage WEBPAGE;
    public final NavigationReason REASON;

    public WebHistory(DiaryDate dd, Webpage w, NavigationReason reason) {
        super(DiaryEntryCategory.WEB_HISTORY, dd);
        WEBPAGE = w;
        REASON = reason;
    }

    @Override
    public String getStringSummary() {
        return WEBPAGE.TITLE;
    }

    @Override
    public Content getContent() {
        return WEBPAGE;
    }

    public static enum NavigationReason {
        AUTO_BOOKMARK, AUTO_SUBFRAME, AUTO_TOPLEVEL, FORM_SUBMIT, GENERATED, LINK, MANUAL_SUBFRAME, RELOAD, TYPED;
    }

    @SuppressWarnings("unused")
    public static WebHistory[] createFromJson(File historyFile) throws IOException {
        ArrayList<WebHistory> output = new ArrayList<>(200000);
        JSONObject pageVisitsO = Util.readJsonObjectFile(historyFile);
        JSONArray pageVisits = new JSONArray(pageVisitsO.getJSONArray("Browser History"));
        for (Object pageVisitO : pageVisits) {
            JSONObject pageVisit = (JSONObject) pageVisitO;
            String favicon = pageVisit.getString("favicon_url");
            String pageTransitionS = pageVisit.getString("page_transition");
            String title = pageVisit.getString("title");
            JSONObject ptoken = pageVisit.getJSONObject("ptoken");
            String url = pageVisit.getString("url");
            String clientId = pageVisit.getString("client_id");
            long timestampI = pageVisit.getLong("time_usec");

            DiaryDateTime timestamp = new DiaryDateTime(timestampI);
            NavigationReason reason = NavigationReason.valueOf(pageTransitionS);
            Webpage w = new Webpage(url, title);

            WebHistory wh = new WebHistory(timestamp, w, reason);
            output.add(wh);
        }
        return output.toArray(new WebHistory[output.size()]);
    }
}
