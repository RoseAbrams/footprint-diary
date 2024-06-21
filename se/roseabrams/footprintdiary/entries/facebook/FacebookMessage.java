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

    public static FacebookMessage[] createFromFolder(File messageParentFolder) throws IOException {
        ArrayList<FacebookMessage> output = new ArrayList<>(100000);
        for (File messageFolder : messageParentFolder.listFiles()) {
            assert messageFolder.listFiles(pathname -> !pathname.isDirectory()).length == 1;
            File messageFile = new File(messageFolder, "message_1.html");
            output.addAll(createFromHtml(messageFile));
        }
        return output.toArray(new FacebookMessage[output.size()]);
    }

    public static ArrayList<FacebookMessage> createFromHtml(File messageFile) throws IOException {
        ArrayList<FacebookMessage> output = new ArrayList<>(1000);
        Document d = Jsoup.parse(messageFile);
        String channel = d.title();
        Elements messagesE = d.select("div._a706 > div._3-95._a6-g");
        for (Element messageE : messagesE) {
            String sender = messageE.selectFirst("div._a6-h._a6-i").text();
            String text = messageE.selectFirst("div._2ph_._a6-p").text();
            String dateS = messageE.selectFirst("div._3-94._a6-o > div._a72d").text();

            File media = null;
            /*
             * if (messageE.selectFirst("img") != null) {
             * //...
             * } else if (messageE.selectFirst("video") != null) {
             * //...
             * }
             */
            // guard until media handling is implemented
            assert messageE.selectFirst("img") == null && messageE.selectFirst("video") == null;

            if (media == null) {
                output.add(new FacebookMessage(FacebookWallEvent.parseDate(dateS), text, channel, sender));
            } else {
                output.add(new FacebookMediaMessage(FacebookWallEvent.parseDate(dateS), text, channel, sender, media));
            }
        }
        return output;
    }
}
