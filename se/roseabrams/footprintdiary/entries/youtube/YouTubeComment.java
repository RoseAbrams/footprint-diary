package se.roseabrams.footprintdiary.entries.youtube;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;

public class YouTubeComment extends YouTubeVideoEvent {

    public final String ID;
    public final String TEXT;
    public final boolean IS_REPLY;

    public YouTubeComment(DiaryDate dd, YouTubeVideo video, String id, String text, boolean isReply) {
        super(dd, video);
        assert id != null && !id.isBlank();

        ID = id;
        TEXT = text;
        IS_REPLY = isReply;
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

    @Override
    public boolean equals(Object o) {
        return o instanceof YouTubeComment c2 && ID.equals(c2.ID); // is this reliable? better check during debug
    }

    public static YouTubeComment[] createFromHtml(File commentsFile) throws IOException {
        ArrayList<YouTubeComment> output = new ArrayList<>(1000);
        Document d = Jsoup.parse(commentsFile);
        // Elements commentsEs = d.select("c-wiz.xDtZAf div.uUy2re");
        DiaryDate date = null;
        // for (Element commentE : commentsEs) {
        for (Element e : d.selectFirst("body > div").children()) {
            if (e.tagName().equals("c-wiz") && e.hasClass("xDtZAf")) {
                Element commentE = e;
                assert date != null;
                Element textE = commentE.selectFirst("div.QTGV3c");
                String text = textE.text();

                YouTubeVideo v;
                Element descE = commentE.selectFirst("div.SiEggd");
                if (descE == null) 
                    continue; // really not sure what happened here...?
                Element parentE = descE.selectFirst("a");
                if (parentE == null) {
                    if (descE.text().contains("unavailable video"))
                        continue; // TODO
                    else if (descE.text().contains("private video"))
                        continue; // TODO
                    else
                        throw new AssertionError();
                }
                Element parentLinkE = parentE.selectFirst("a");
                String parentName = parentLinkE.text();
                String parentLink = parentLinkE.attr("href");
                String commentId = parentLink.substring(parentLink.lastIndexOf("lc=") + 3);
                if (parentLink.contains("watch?")) {
                    String videoId = parentLink.substring(parentLink.indexOf("=") + 1, parentLink.indexOf("&"));
                    v = YouTubeVideo.getOrCreate(videoId, parentName, null, null);
                } else if (parentLink.contains("/post/")) {
                    System.err.println("skipped YouTube comment on post: \"" + text + "\""); // TODO
                    continue;
                } else
                    throw new AssertionError(); // should not happen unless there's an unknown third parent type

                String timeS = commentE.selectFirst("div.wlgrwd > div.H3Q9vf").ownText();
                byte hour = Byte.parseByte(timeS.substring(0, timeS.indexOf(":")));
                byte minute = Byte.parseByte(timeS.substring(timeS.indexOf(":") + 1, timeS.indexOf(":") + 3));
                hour += timeS.contains("PM") && !timeS.startsWith("12") ? 12 : 0;
                boolean isReply = parentE != null && parentE.ownText().startsWith("Replied");

                YouTubeComment c = new YouTubeComment(new DiaryDateTime(date, hour, minute, (byte) 0),
                        v, commentId, text, isReply);
                output.add(c);
            } else if (e.tagName().equals("div") && e.hasClass("CW0isc")) {
                Element daySeperatorE = e;
                String dateS = daySeperatorE.selectFirst("h2").text();
                short year;
                byte month = DiaryDate.parseMonthName(dateS.substring(0, 3));
                byte day;
                if (dateS.contains(",")) {
                    year = Short.parseShort(dateS.substring(dateS.indexOf(",") + 2));
                    day = Byte.parseByte(dateS.substring(dateS.indexOf(" ") + 1, dateS.indexOf(",")));
                } else {
                    // year is only shown for non-current years at time of scrape
                    year = 2024;
                    day = Byte.parseByte(dateS.substring(dateS.indexOf(" ") + 1));
                }

                date = new DiaryDate(year, month, day);
            } else {
                throw new AssertionError();
            }
        }
        return output.toArray(new YouTubeComment[output.size()]);
    }

    public static YouTubeComment[] createFromCsv(File commentsFile) throws IOException {
        final Pattern JSON_OBJECT_DELIM = Pattern.compile("(?:\\\"}),(?:{\\\")");
        ArrayList<YouTubeComment> output = new ArrayList<>(1000);
        Scanner s = new Scanner(commentsFile);
        s.nextLine(); // headings
        while (s.hasNextLine()) {
            Scanner s2 = new Scanner(s.nextLine());
            s2.useDelimiter(",");
            String channelId = s2.next();
            String timestampS = s2.next();
            int price = s2.nextInt();
            String commentId = s2.next();
            String parentCommentId = s2.next();
            String videoId = s2.next();
            String textJson = s2.nextLine();
            s2.close();

            assert price == 0; // purely exploratory
            textJson = textJson.substring(1, textJson.length() - 2); // remove surrounding quotes
            //textJson = textJson.replace("\\\"\"", "\""); // probably not needed since converting to json later
            textJson = textJson.replace("\"\"", "\"");
            textJson = textJson.replace("\\n", "\n");
            String text;
            if (textJson.contains(JSON_OBJECT_DELIM.toString())) {
                // rendered in multiple json objects
                ArrayList<JSONObject> textParts = new ArrayList<>(5);
                Scanner s3 = new Scanner(textJson);
                s3.useDelimiter(JSON_OBJECT_DELIM);
                while (s3.hasNext()) {
                    textParts.add(new JSONObject(s3.next()));
                }
                s3.close();

                StringBuilder textB = new StringBuilder();
                for (JSONObject textPart : textParts) {
                    textB.append(textPart.getString("text"));
                }
                text = textB.toString();
            } else {
                // rendered in single json object
                JSONObject textO = new JSONObject(textJson);
                text = textO.getString("text");
            }

            DiaryDateTime timestamp = new DiaryDateTime(timestampS);
            YouTubeVideo v = YouTubeVideo.getOrCreate(videoId, null, channelId, null);

            YouTubeComment c = new YouTubeComment(timestamp, v, commentId, text, !parentCommentId.isBlank());
            output.add(c);
        }
        s.close();
        return output.toArray(new YouTubeComment[output.size()]);
    }
}
