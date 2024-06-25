package se.roseabrams.footprintdiary.entries.youtube;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;

public class YouTubeComment extends YouTubeEvent {

    public final String ID;
    public final String TEXT;

    public YouTubeComment(DiaryDate dd, YouTubeVideo video, String id, String text) {
        super(dd, video);
        ID = id;
        TEXT = text;
    }

    public String getDirectLink() {
        return VIDEO.getPath() + "&lc=" + ID;
    }

    public boolean textTruncated() {
        return TEXT.endsWith("...");
    }

    @Override
    public String getStringSummary() {
        return TEXT;
    }

    public static YouTubeComment[] createFromHtml(File commentsFile) throws IOException {
        ArrayList<YouTubeComment> output = new ArrayList<>(1000);
        Document d = Jsoup.parse(commentsFile);
        // Elements commentsEs = d.select("c-wiz.xDtZAf div.uUy2re");
        DiaryDate day = null;
        // for (Element commentE : commentsEs) {
        for (Element e : d.root().children()) {
            if (e.tagName().equals("c-wiz")) {
                Element commentE = e;
                assert day != null;
                Element textE = commentE.selectFirst("div.QTGV3c");
                String text = textE.text();
                Element parentE = commentE.selectFirst("div.SiEggd > a");
                String parentName = parentE.text();
                String parentLink = parentE.attr("href");
                String commentId = parentLink.substring(parentLink.lastIndexOf("lc=") + 3);
                if (parentLink.equals("watch?")) {
                    String videoId = parentLink.substring(parentLink.indexOf("="), parentLink.indexOf("&"));
                    YouTubeVideo v = YouTubeVideo.getOrCreate(videoId, parentName, null, null);
                } else if (parentLink.equals("post")) {
                    ...
                } else {
                    throw new AssertionError(); // should not happen unless there's an unknown third parent type
                }
                String timeS = commentE.selectFirst("div.wlgrwd > div.H3Q9vf").ownText();
                byte hour = Byte.parseByte(timeS.substring(0, timeS.indexOf(":")));
                byte minute = Byte.parseByte(timeS.substring(timeS.indexOf(":") + 1), (timeS.indexOf(":") + 3));
                hour += timeS.contains("PM") ? 12 : 0;

                YouTubeComment c = new YouTubeComment(new DiaryDateTime(day, hour, minute, (byte) 0),
                        v, commentId, text);
                output.add(c);
            } else if (e.tagName().equals("div") && e.hasClass("CW0isc")) {
                Element daySeperatorE = e;
                String dayS = daySeperatorE.selectFirst("h2").text();
                short year;
                byte dayOfMonth;
                if (dayS.contains(",")) {
                    year = Short.parseShort(dayS.substring(dayS.indexOf(",") + 1));
                    dayOfMonth = Byte.parseByte(dayS.substring(dayS.indexOf(" ") + 1, dayS.indexOf(",")));
                } else {
                    // year is only shown for non-current years at time of scrape
                    year = 2024;
                    dayOfMonth = Byte.parseByte(dayS.substring(dayS.indexOf(" ") + 1));
                }

                day = new DiaryDate(year, DiaryDate.parseMonthName(dayS.substring(0, 3)), dayOfMonth);
            }
        }
        return output.toArray(new YouTubeComment[output.size()]);
    }

    public static YouTubeComment[] createFromCsv(File commentsFile) throws IOException {
        ArrayList<YouTubeComment> output = new ArrayList<>(1000);
        ...
        return output.toArray(new YouTubeComment[output.size()]);
    }
}
