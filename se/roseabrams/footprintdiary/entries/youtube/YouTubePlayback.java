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

public class YouTubePlayback extends YouTubeEvent {

    public YouTubePlayback(DiaryDate dd, YouTubeVideo video) {
        super(dd, video);
    }

    @Override
    public String getStringSummary() {
        return VIDEO.toString();
    }

    public static YouTubePlayback[] createFromHtml(File watchedFile) throws IOException {
        ArrayList<YouTubePlayback> output = new ArrayList<>(100000);
        Document d = Jsoup.parse(watchedFile);
        Elements playbackEs = d.body().select("div.outer-cell div.content-cell");
        for (Element playbackE : playbackEs) {
            assert playbackE.text().startsWith("Watched");
            String videoId;
            String videoTitle;
            String channelId;
            String channelName;

            Elements links = playbackE.select("a");
            Element videoLink = links.first();
            String videoUrl = videoLink.attr("href");
            videoId = videoUrl.substring("https://youtube.com/watch?v=".length() - 1);
            if (videoLink.text().equals(videoUrl))
                videoTitle = null;
            else
                videoTitle = videoLink.text();
            if (links.size() == 1) {
                channelId = null;
                channelName = null;
            } else {
                Element channelLink = links.last();
                String channelUrl = channelLink.attr("href");
                channelId = channelUrl.substring("https://youtube.com/channel/".length() - 1);
                channelName = channelLink.text();
            }
            YouTubeVideo v = YouTubeVideo.getOrCreate(videoId, videoTitle, channelId, channelName);

            Node dateE = playbackE.select("br").last().nextSibling();
            String dateS = dateE.toString();
            DiaryDateTime date = new DiaryDateTime(
                    Short.parseShort(dateS.substring(dateS.indexOf(",") + 1, dateS.lastIndexOf(","))),
                    DiaryDate.parseMonthName(dateS.substring(0, 3)),
                    Byte.parseByte(dateS.substring(4, dateS.indexOf(","))),
                    Byte.parseByte(dateS.substring(dateS.lastIndexOf(",") + 1, dateS.indexOf(":"))),
                    Byte.parseByte(dateS.substring(dateS.indexOf(":") + 1, dateS.lastIndexOf(":"))),
                    Byte.parseByte(dateS.substring(dateS.lastIndexOf(":"), dateS.lastIndexOf(":") + 2)));

            output.add(new YouTubePlayback(date, v));
        }
        return output.toArray(new YouTubePlayback[output.size()]);
    }
}
