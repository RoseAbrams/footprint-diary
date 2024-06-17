package se.roseabrams.footprintdiary.entries.facebook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.roseabrams.footprintdiary.DiaryDateTime;

public class FacebookComment extends FacebookWallEvent {
    /* "comments.html", "your_comments_in_groups.html" */

    public final String TEXT;
    public final String PARENT_OP;

    public FacebookComment(DiaryDateTime date, String text, String op) {
        super(date);
        assert text != null && !text.isBlank() && op != null && !op.isBlank();
        TEXT = text;
        PARENT_OP = op.intern();
    }

    @Override
    public String getStringSummary() {
        return TEXT;
    }

    public static FacebookComment[] createFromHtml(File commentsFile) throws IOException {
        ArrayList<FacebookComment> output = new ArrayList<>();
        Document d = Jsoup.parse(commentsFile);
        Elements commentsE = d.select("div._a706 > div._3-95._a6-g");
        for (Element commentE : commentsE) {
            String opES = commentE.selectFirst("div._a6-h._a6-i").text();
            int opIndexStart = opES.indexOf("commented on ") + "commented on ".length();
            int opIndexEnd = opES.lastIndexOf("'s ");
            String op = opES.substring(opIndexStart, opIndexEnd);
            String text = commentE.selectFirst("div._2ph_._a6-p").text();
            String dateS = commentE.selectFirst("div._3-94._a6-o > div._a72d").text();
            output.add(new FacebookComment(parseDate(dateS), text, op));
        }
        return output.toArray(new FacebookComment[output.size()]);
    }
}
