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

    public final String BODY;
    public final Type TYPE;
    public final String TIMELINE;
    public final String APP;

    public FacebookPost(DiaryDateTime dd, String body, Type type, String timeline, String app) {
        super(dd);
        assert body != null;// && (!body.isBlank() || type != Type.TEXT); // it's simply blank when sharing other's text posts
        assert type != null;
        assert timeline != null && !timeline.isBlank();
        BODY = body;
        TYPE = type;
        TIMELINE = timeline.intern();
        APP = app;
    }

    @Override
    public String getStringSummary() {
        return BODY;
    }

    public static enum Type {
        TEXT, PHOTO, VIDEO, LINK, COMMENT, NOTE, CHECKIN, PAGE, EVENT, MEMORY, RECOMMENDATION, ALBUM, LIFE_EVENT;

        public static Type parse(String s) {
            switch (s) {
                case "post":
                    return TEXT;
                case "GIF":
                    return PHOTO;
                case "live_video":
                    return VIDEO;
                default:
                    return valueOf(s.toUpperCase());
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
            Type type = null;
            String body = null;
            String timeline;
            String app = null;
            if (description.endsWith("timeline.") || description.contains(" in ")) {
                // other timeline
                int opIndexStart;
                if (description.contains("wrote on ")) {
                    opIndexStart = description.indexOf("wrote on ") + "wrote on ".length();
                    type = Type.TEXT;
                } else {
                    opIndexStart = description.indexOf(" to ") + " to ".length();
                    type = Type.parse(description.substring(description.indexOf("shared a ") + "shared a ".length(),
                            opIndexStart - " to ".length()));
                }
                timeline = description.substring(opIndexStart, description.lastIndexOf("'s"));
            } else {
                // own timeline
                timeline = PersonalConstants.FACEBOOK_NAME;
                if (description.isEmpty())
                    type = Type.TEXT; // TODO not always textpost
                else if (description.contains(" via ")) {
                    type = Type.TEXT;
                    app = description.substring(description.lastIndexOf(" ") + 1, description.length() - 1);
                } else if (description.contains(" recommends "))
                    type = Type.RECOMMENDATION;
                else if (description.startsWith("Started") || description.startsWith("Left")) {
                    type = Type.LIFE_EVENT;
                    body = description;
                } else if (description.equals("Moved"))
                    type = Type.LIFE_EVENT;
                else if (description.contains(" from the playlist "))
                    type = Type.VIDEO;
                else if (description.contains(" was with "))
                    type = Type.CHECKIN; // TODO specify person(s) in some field?
                else
                    type = Type.parse(
                            description.substring(description.lastIndexOf(" ") + 1, description.length() - 1));
            }
            // TODO preserve newlines (gotta solve div jungle)
            // TODO remove alttext of media, like albumname or mediadesc
            if (body == null) {
                body = postE.selectFirst("div._2ph_._a6-p").text();
                if (body.contains("Updated "))
                    if (body.startsWith("Updated "))
                        body = "";
                    else
                        body = body.substring(0, body.lastIndexOf("Updated ") - 1);
            }
            String dateS = postE.selectFirst("div._3-94._a6-o div._a72d").text();

            Element mediaE = postE.selectFirst("img");
            if (mediaE == null)
                mediaE = postE.selectFirst("video");

            if (mediaE != null) {
                media = mediaE.attr("src");
                media = postFile.getParent() + media.substring(media.indexOf("/media/"));
            } else
                media = null;

            FacebookPost f;
            if (media != null || (type == Type.PHOTO || type == Type.VIDEO))
                f = new FacebookMediaPost(parseDate(dateS), body, type, timeline, app, media);
            else
                f = new FacebookPost(parseDate(dateS), body, type, timeline, app);
            // TODO FacebookLinkPost
            output.add(f);
        }
        return output.toArray(new FacebookPost[output.size()]);
    }
}
