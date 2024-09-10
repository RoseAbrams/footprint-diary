package se.roseabrams.footprintdiary.entries.youtube;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public static List<YouTubeEvent> createHistoryFromHtml(File watchedFile) throws IOException {
        ArrayList<YouTubeEvent> output = new ArrayList<>(100000);
        Document d = Jsoup.parse(watchedFile);
        Elements playbackEs = d.body().select("div.outer-cell div.content-cell.mdl-typography--body-1");
        for (Element playbackE : playbackEs) {
            if (!playbackE.hasText())
                continue; // half of the matches are empty, no idea how to filter them away with CSS

            YouTubeEvent y;

            Node dateE = playbackE.select("br").last().nextSibling();
            String dateS = dateE.toString().trim();
            DiaryDateTime date = new DiaryDateTime(
                    Short.parseShort(dateS.substring(dateS.indexOf(",") + 2, dateS.lastIndexOf(","))),
                    DiaryDate.parseMonthName(dateS.substring(0, 3)),
                    Byte.parseByte(dateS.substring(4, dateS.indexOf(","))),
                    (byte) (Byte.parseByte(dateS.substring(dateS.lastIndexOf(",") + 2, dateS.indexOf(":")))
                            + (dateS.contains("PM") && !dateS.contains(" 12:") ? 12 : 0)),
                    Byte.parseByte(dateS.substring(dateS.indexOf(":") + 1, dateS.lastIndexOf(":"))),
                    Byte.parseByte(dateS.substring(dateS.lastIndexOf(":") + 1, dateS.lastIndexOf(":") + 3)));
            Elements links = playbackE.select("a");
            Element primaryLink = links.first();

            if (playbackE.text().startsWith("Watched")) {
                YouTubeVideo v;
                boolean isAd;
                if (playbackE.text().startsWith("Watched a video that has been removed")) {
                    v = null;
                    isAd = false;
                } else {
                    String videoId;
                    String videoTitle;
                    String channelId;
                    String channelName;

                    String primaryUrl = primaryLink.attr("href");
                    if (primaryUrl.contains("music.youtube"))
                        primaryUrl = primaryUrl.replace("music.youtube", "www.youtube");
                    assert primaryUrl.startsWith("https://www.youtube.com/watch?v=");
                    videoId = primaryUrl.substring("https://www.youtube.com/watch?v=".length());
                    if (videoId.isEmpty())
                        continue; // very weird entry that somehow shows up...?

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
                        assert channelUrl.startsWith("https://www.youtube.com/channel/");
                        channelId = channelUrl.substring("https://www.youtube.com/channel/".length());
                        channelName = channelLink.text();
                    }
                    Element infoE = playbackE.parent().selectFirst("div.mdl-typography--caption");
                    isAd = infoE.text().contains("From Google Ads");

                    v = YouTubeVideo.getOrCreate(videoId, videoTitle, channelId, channelName);
                }
                y = new YouTubePlayback(date, v, isAd);
            } else if (playbackE.text().startsWith("Searched for")) {
                String searchTerm = primaryLink.text();

                y = new YouTubeSearch(date, searchTerm);
            } else if (playbackE.text().startsWith("Visited")) {
                continue; // website arrived at when adverts clicked on, probably not very interesting
            } else {
                throw new AssertionError();
            }
            output.add(y);
        }
        return output;
    }
}