package se.roseabrams.footprintdiary.entries.facebook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.roseabrams.footprintdiary.DiaryDateTime;

public class FacebookPost extends FacebookWallEvent {
    /*
     * "your_posts__check_ins__photos_and_videos_1.html",
     * "group_posts_and_comments.html"
     */

    public final String TEXT;
    public final Type TYPE;

    public FacebookPost(DiaryDateTime dd, String text, Type type) {
        super(dd);
        TEXT = text;
        TYPE = type;
    }

    @Override
    public String getStringSummary() {
        return TEXT;
    }

    public static enum Type {
        POST, PHOTO, VIDEO, LINK
    }

    public static FacebookPost[] createFromHtml(File postFile) throws IOException {
        ArrayList<FacebookPost> output = new ArrayList<>();
        Document d = Jsoup.parse(postFile);
        Elements postsE = d.select("div._a706 > div._3-95._a6-g");
        for (Element postE : postsE) {
            String typeES = postE.selectFirst("div._a6-h._a6-i").text();
            String typeS = typeES.substring(typeES.lastIndexOf(" ") + 1);
            String text = postE.selectFirst("div._2ph_._a6-p").text();
            text = text.substring(0, text.lastIndexOf("Updated "));
            String dateS = postE.selectFirst("div._3-94._a6-o div._a72d").text();
            ...// media handling
            output.add(new FacebookPost(parseDate(dateS), text, Type.valueOf(typeS.toUpperCase())));
        }
        return output.toArray(new FacebookPost[output.size()]);
    }
}
