package se.roseabrams.footprintdiary.entries.tinder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.Util;

public class TinderSwipe extends DiaryEntry {

    public final long ID;
    public final String NAME;
    public final SwipeResult RESULT;
    public final SwipeReason REASON;

    public TinderSwipe(DiaryDateTime date, long id, String name, SwipeResult swipeResult, SwipeReason swipeReason) {
        super(DiaryEntryCategory.TINDER, date);
        ID = id;
        NAME = name;
        RESULT = swipeResult;
        REASON = swipeReason;
    }

    @Override
    public String getStringSummary() {
        return "swiped " + RESULT.toString() + (REASON != null ? " (because " + REASON.toString() + ")" : "")
                + " on " + NAME;
    }

    public static enum SwipeResult {
        LEFT, RIGHT, SUPER;
    }

    public static enum SwipeReason {
        APPEARANCE, PERSONALITY, BOTH, HORNY, CURIOUS;
    }

    public static List<TinderSwipe> createFromJson(File tinderJson) throws IOException {
        ArrayList<TinderSwipe> output = new ArrayList<>();
        JSONArray swipes = Util.readJsonArrayFile(tinderJson);
        for (Object swipeO : swipes) {
            JSONObject swipe = (JSONObject) swipeO;
            if (swipe.getString("name").isEmpty() || swipe.getString("swipe_result").equals("UNDEFINED")
                    || !swipe.getString("agent").isEmpty())
                continue;

            long id = swipe.getLong("timestamp");
            String name = swipe.getString("name");
            String swipeResultS = swipe.getString("swipe_result");
            String swipeReasonS = swipe.getString("swipe_reason");

            DiaryDateTime date = new DiaryDateTime(id);
            SwipeResult swipeResult = SwipeResult.valueOf(swipeResultS);
            SwipeReason swipeReason = swipeReasonS.equals("UNSPECIFIED") ? null : SwipeReason.valueOf(swipeReasonS);

            TinderSwipe t = new TinderSwipe(date, id, name, swipeResult, swipeReason);
            output.add(t);
        }
        return output;
    }
}
