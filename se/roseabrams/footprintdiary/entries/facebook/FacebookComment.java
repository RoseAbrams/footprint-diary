package se.roseabrams.footprintdiary.entries.facebook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.PersonalConstants;

public class FacebookComment extends FacebookWallEvent {

    public final String BODY;
    public final String PARENT_OP;

    public FacebookComment(DiaryDateTime date, String body, String op) {
        super(date);
        assert body != null;
        assert op == null || !op.isBlank();
        BODY = body;
        PARENT_OP = op != null ? op.intern() : null;
    }

    @Override
    public String getStringSummary() {
        return BODY;
    }

    public static List<FacebookComment> createFromHtml(File commentsFile) throws IOException {
        ArrayList<FacebookComment> output = new ArrayList<>();
        Document d = Jsoup.parse(commentsFile);
        Elements commentsE = d.select("div._a706 > div._3-95._a6-g");
        for (Element commentE : commentsE) {
            String description = commentE.selectFirst("div._a6-h._a6-i").text();
            String op;
            if (description.contains(" own "))
                op = PersonalConstants.FACEBOOK_NAME;
            else if (description.endsWith("commented.") || description.endsWith("comment."))
                op = null; // sometimes just empty, i guess OP was deleted
            else if (description.contains(" your "))
                op = null; // what??? weird edge case
            else {
                int opIndexStart = description.indexOf("commented on ") + "commented on ".length();
                int opIndexEnd = description.lastIndexOf("'s ");
                op = description.substring(opIndexStart, opIndexEnd);
            }
            Element bodyQ = commentE.selectFirst("div._2ph_._a6-p");
            String body = bodyQ != null ? bodyQ.text() : "";
            String dateS = commentE.selectFirst("div._3-94._a6-o  div._a72d").text();
            FacebookComment f = new FacebookComment(parseDate(dateS), body, op);
            output.add(f);
        }
        return output;
    }
}
