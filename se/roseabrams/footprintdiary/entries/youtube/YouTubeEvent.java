package se.roseabrams.footprintdiary.entries.youtube;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;

public abstract class YouTubeEvent extends DiaryEntry {

    public YouTubeEvent(DiaryDate dd) {
        super(DiaryEntryCategory.YOUTUBE, dd);
    }

    public static YouTubeEvent[] createFromHtml(File watchedFile) throws IOException {
        ArrayList<YouTubeEvent> output = new ArrayList<>(100000);
        Document d = Jsoup.parse(watchedFile);
        Elements playbackEs = d.body().select("div.outer-cell div.content-cell.mdl-typography--body-1");
        for (Element playbackE : playbackEs) {
            YouTubeEvent newEvent;

            Node dateE = playbackE.select("br").last().nextSibling();
            String dateS = dateE.toString();
            DiaryDateTime date = new DiaryDateTime(
                    Short.parseShort(dateS.substring(dateS.indexOf(",") + 1, dateS.lastIndexOf(","))),
                    DiaryDate.parseMonthName(dateS.substring(0, 3)),
                    Byte.parseByte(dateS.substring(4, dateS.indexOf(","))),
                    (byte) (Byte.parseByte(dateS.substring(dateS.lastIndexOf(",") + 1, dateS.indexOf(":")))
                            + (dateS.contains("PM") ? 12 : 0)),
                    Byte.parseByte(dateS.substring(dateS.indexOf(":") + 1, dateS.lastIndexOf(":"))),
                    Byte.parseByte(dateS.substring(dateS.lastIndexOf(":"), dateS.lastIndexOf(":") + 2)));
            Elements links = playbackE.select("a");
            Element primaryLink = links.first();
            String primaryUrl = primaryLink.attr("href");

            if (playbackE.text().startsWith("Watched")) {
                String videoId;
                String videoTitle;
                String channelId;
                String channelName;

                videoId = primaryUrl.substring("https://youtube.com/watch?v=".length() - 1);
                if (primaryLink.text().equals(primaryUrl))
                    videoTitle = null;
                else
                    videoTitle = primaryLink.text();
                if (links.size() == 1) {
                    channelId = null;
                    channelName = null;
                } else {
                    Element channelLink = links.last();
                    String channelUrl = channelLink.attr("href");
                    channelId = channelUrl.substring("https://youtube.com/channel/".length() - 1);
                    channelName = channelLink.text();
                }
                Element infoE = playbackE.parent().selectFirst("div.mdl-typography--caption");
                boolean isAd = infoE.text().contains("From Google Ads");

                YouTubeVideo v = YouTubeVideo.getOrCreate(videoId, videoTitle, channelId, channelName);
                newEvent = new YouTubePlayback(date, v, isAd);
            } else if (playbackE.text().startsWith("Searched for")) {
                String searchTerm = primaryLink.text();

                newEvent = new YouTubeSearch(date, searchTerm);
            } else {
                throw new AssertionError();
            }
            output.add(newEvent);
        }
        return output.toArray(new YouTubeEvent[output.size()]);
    }
}