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

public class FacebookPost extends FacebookWallEvent {

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

    public static List<FacebookPost> createFromHtml(File postFile) throws IOException {
        ArrayList<FacebookPost> output = new ArrayList<>();
        Document d = Jsoup.parse(postFile);
        Elements postsE = d.select("div._a706 > div._3-95._a6-g");
        for (Element postE : postsE) {
            Element descriptionE = postE.selectFirst("div._a6-h._a6-i");
            String desc = descriptionE != null ? descriptionE.text() : "";
            String media;
            Type type = null;
            String body = null;
            String timeline;
            String app = null;
            String linkS = null;
            if (desc.endsWith("timeline.") || desc.contains(" in ")) {
                // other timeline
                int opIndexStart;
                if (desc.contains("wrote on ")) {
                    opIndexStart = desc.indexOf("wrote on ") + "wrote on ".length();
                    type = Type.TEXT;
                } else {
                    opIndexStart = desc.indexOf(" to ") + " to ".length();
                    type = Type.parse(desc.substring(desc.indexOf("shared a ") + "shared a ".length(),
                            opIndexStart - " to ".length()));
                }
                timeline = desc.substring(opIndexStart, desc.lastIndexOf("'s"));
            } else {
                // own timeline
                timeline = PersonalConstants.FACEBOOK_NAME;
                if (desc.isEmpty())
                    type = Type.TEXT; // TODO not always textpost
                else if (desc.contains(" via ")) {
                    type = Type.TEXT;
                    app = desc.substring(desc.lastIndexOf(" ") + 1, desc.length() - 1);
                } else if (desc.contains(" recommends "))
                    type = Type.RECOMMENDATION;
                else if (desc.startsWith("Started") || desc.startsWith("Left")) {
                    type = Type.LIFE_EVENT;
                    body = desc;
                } else if (desc.equals("Moved"))
                    type = Type.LIFE_EVENT;
                else if (desc.contains(" from the playlist "))
                    type = Type.VIDEO;
                else if (desc.contains(" was with "))
                    type = Type.CHECKIN; // TODO specify person(s) in some field?
                else
                    type = Type.parse(desc.substring(desc.lastIndexOf(" ") + 1, desc.length() - 1));
            }

            Element bodyE = postE.selectFirst("div._2ph_._a6-p");
            Element imageQ = bodyE.selectFirst("div._a7nf");
            assert (imageQ == null) == (bodyE.selectFirst("img") == null && bodyE.selectFirst("video") == null);
            if (imageQ != null)
                imageQ.attr("display", "none");

            if (body == null) {
                body = textWithNewlines(bodyE).toString();
                if (body.contains("Updated "))
                    if (body.startsWith("Updated "))
                        body = "";
                    else
                        body = body.substring(0, body.lastIndexOf("Updated ") - 1);
                // TODO remove metadata classed as "Place:" and "Address:"
            }

            Element mediaE = postE.selectFirst("img"); // TODO what about more than one picture/video in post?
            if (mediaE == null)
                mediaE = postE.selectFirst("video");

            if (mediaE != null) {
                media = mediaE.attr("src");
                media = postFile.getParent() + media.substring(media.indexOf("/media/"));
            } else
                media = null;

            if (media == null && (type == Type.TEXT || type == Type.LINK)) {
                Elements linkQ = new Elements();
                postE.select("a").forEach(l -> {
                    if (l.select("img").isEmpty() && l.select("div").isEmpty())
                        linkQ.add(l);
                });
                if (linkQ.size() > 0) {
                    if (linkQ.size() > 1)
                        System.err.println("Multiple links in post! Will assuming first one is main");
                    //assert linkQ.get(0).text().equals(linkQ.get(0).attr("href")); // only URL-encode differs them afaict
                    linkS = linkQ.get(0).attr("href");
                    body = body.replace(linkS, "");
                }
            }
            if (linkS != null && type != Type.LINK)
                type = Type.LINK;

            body = body.trim();
            String dateS = postE.selectFirst("div._3-94._a6-o div._a72d").text();
            FacebookPost f;
            if (media != null || (type == Type.PHOTO || type == Type.VIDEO))
                f = new FacebookMediaPost(parseDate(dateS), body, type, timeline, app, media);
            else if (type == Type.LINK)
                f = new FacebookLinkPost(parseDate(dateS), body, type, timeline, app, linkS);
            else
                f = new FacebookPost(parseDate(dateS), body, type, timeline, app);
            output.add(f);
        }
        return output;
    }
}
