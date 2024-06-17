package se.roseabrams.footprintdiary.entries.facebook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.common.Message;

public class FacebookMessage extends DiaryEntry implements Message {
    /*
     * "inbox/",
     * "archived_threads/"
     */

    public final String TEXT;
    public final String CHANNEL;
    public final String SENDER;

    public FacebookMessage(DiaryDate dd, String text, String channel, String sender) {
        super(DiaryEntryCategory.FACEBOOK_MESSAGE, dd);
        assert text != null && !text.isBlank();
        TEXT = text;
        CHANNEL = channel.intern();
        SENDER = sender.intern();
    }

    @Override
    public String getStringSummary() {
        return SENDER + ": " + TEXT;
    }

    @Override
    public String getSender() {
        return SENDER;
    }

    @Override
    public String getRecipient() {
        return isByMe() ? CHANNEL : PersonalConstants.FACEBOOK_NAME;
    }

    @Override
    public boolean isByMe() {
        return SENDER.equals(PersonalConstants.FACEBOOK_NAME);
    }

    public static FacebookMessage[] createFromHtml(File messengerFile) throws IOException {
        ArrayList<FacebookMessage> output = new ArrayList<>();
        Document d = Jsoup.parse(messengerFile);
        String channel = d.title();
        Elements messagesE = d.select("div._a706 > div._3-95._a6-g");
        for (Element messageE : messagesE) {
            String sender = messageE.selectFirst("div._a6-h._a6-i").text();
            String text = messageE.selectFirst("div._2ph_._a6-p").text();
            String dateS = messageE.selectFirst("div._3-94._a6-o > div._a72d").text();
            if (messageE.selectFirst("img") != null) {
                ...// media handling
            }
            output.add(new FacebookMessage(FacebookWallEvent.parseDate(dateS), text, channel, sender));
        }
        return output.toArray(new FacebookMessage[output.size()]);
    }
}
