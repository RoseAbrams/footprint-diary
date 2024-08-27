package se.roseabrams.footprintdiary.entries.facebook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.PersonalConstants;

public class FacebookPost extends FacebookWallEvent {
    /*
     * "your_posts__check_ins__photos_and_videos_1.html",
     * "group_posts_and_comments.html"
     */

    public final String TEXT;
    public final Type TYPE;
    public final String TIMELINE;

    public FacebookPost(DiaryDateTime dd, String text, Type type, String timeline) {
        super(dd);
        assert text != null && !text.isBlank() && type != null && timeline != null && !timeline.isBlank();
        TEXT = text;
        TYPE = type;
        TIMELINE = timeline.intern();
    }

    @Override
    public String getStringSummary() {
        return TEXT;
    }

    public static enum Type {
        TEXT, PHOTO, VIDEO, LINK;

        public static Type parse(String s) {
            switch (s.toUpperCase()) {
                case "POST":
                    return TEXT;
                default:
                    return valueOf(s);
            }
        }
    }

    public static FacebookPost[] createFromHtml(File postFile) throws IOException {
        ArrayList<FacebookPost> output = new ArrayList<>();
        Document d = Jsoup.parse(postFile);
        Elements postsE = d.select("div._a706 > div._3-95._a6-g");
        for (Element postE : postsE) {
            Element descriptionE = postE.selectFirst("div._a6-h._a6-i");
            String description = descriptionE != null ? descriptionE.text() : "";
            String media;
            String typeS;
            Type type;
            String timeline;
            if (description.endsWith("timeline.")) {
                // other timeline
                int opIndexStart;
                if (description.contains("wrote on ")) {
                    opIndexStart = description.indexOf("wrote on ") + "wrote on ".length();
                    typeS = "POST";
                } else {
                    opIndexStart = description.indexOf(" to ") + " to ".length();
                    typeS = description.substring(description.indexOf("shared a ") + "shared a ".length(),
                            opIndexStart - " to ".length());
                }
                timeline = description.substring(opIndexStart, description.lastIndexOf("'s'"));
            } else {
                // own timeline
                timeline = PersonalConstants.FACEBOOK_NAME;
                typeS = description.substring(description.lastIndexOf(" ") + 1);
            }
            String text = postE.selectFirst("div._2ph_._a6-p").text();
            text = text.substring(0, text.lastIndexOf("Updated "));
            String dateS = postE.selectFirst("div._3-94._a6-o div._a72d").text();

            Element mediaE = postE.selectFirst("img");
            if (mediaE == null)
                mediaE = postE.selectFirst("video");

            media = mediaE.attr("src");
            media = postFile.getParent() + media.substring(media.indexOf("/media/"));

            type = Type.parse(typeS);
            assert type == Type.TEXT || media != null;
            if (media == null) {
                output.add(new FacebookPost(parseDate(dateS), text, type, timeline));
            } else {
                output.add(new FacebookMediaPost(parseDate(dateS), text, type, timeline, media));
            }
        }
        return output.toArray(new FacebookPost[output.size()]);
    }
}
