package se.roseabrams.footprintdiary.entries.facebook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public final String BODY;
    public final String CHANNEL;
    public final String SENDER;
    public final boolean IS_GROUP_CHANNEL;
    public final boolean IS_UNKNOWN_CHANNEL;

    public FacebookMessage(DiaryDate dd, String body, String channel, String sender, boolean isGroupChannel,
            boolean isUnknownChannel) {
        super(DiaryEntryCategory.FACEBOOK_MESSAGE, dd);
        assert body != null;
        BODY = body;
        CHANNEL = channel.intern();
        SENDER = sender != null ? sender.intern() : null;
        IS_GROUP_CHANNEL = isGroupChannel;
        IS_UNKNOWN_CHANNEL = isUnknownChannel;
    }

    @Override
    public String getStringSummary() {
        return SENDER + ": " + BODY;
    }

    @Override
    public String getSender() {
        return SENDER;
    }

    @Override
    public String getRecipient() {
        if (IS_UNKNOWN_CHANNEL)
            return "unknown channel " + CHANNEL;
        else if (IS_GROUP_CHANNEL)
            return "server channel " + CHANNEL;
        else
            return isByMe() ? CHANNEL : PersonalConstants.FACEBOOK_NAME;
    }

    @Override
    public boolean isByMe() {
        return SENDER.equals(PersonalConstants.FACEBOOK_NAME);
    }

    public static List<FacebookMessage> createFromFolder(File messageParentFolder) throws IOException {
        ArrayList<FacebookMessage> output = new ArrayList<>(100000);
        for (File messageFolder : messageParentFolder.listFiles()) {
            for (File messageFile : messageFolder.listFiles(pathname -> !pathname.isDirectory())) {
                output.addAll(createFromHtml(messageFile));
            }
        }
        return output;
    }

    public static ArrayList<FacebookMessage> createFromHtml(File messageFile) throws IOException {
        ArrayList<FacebookMessage> output = new ArrayList<>(1000);
        Document d = Jsoup.parse(messageFile);
        String channel;
        boolean isUnknownChannel;
        boolean isGroupChannel = false;
        if (d.title().startsWith("Participants:"))
            if (d.title().contains("Facebook user")) {
                channel = messageFile.getParent();
                channel = channel.substring(channel.lastIndexOf("\\") + 1);
                isUnknownChannel = true;
            } else
                throw new AssertionError();
        else {
            channel = d.title();
            isUnknownChannel = false;
        }

        Elements messagesE = d.select("div._a706 > div._3-95._a6-g");
        for (int i = 0; i < messagesE.size(); i++) {
            Element messageE = messagesE.get(i);
            if (messageE.text().startsWith("Participants: ") || messageE.text().startsWith("Group Invite Link: ")) {
                isGroupChannel = true;
                while (messagesE.get(i + 1).text().startsWith("Group")) {
                    i++;
                }
                continue;
            }

            Element senderQ = messageE.selectFirst("div._a6-h._a6-i");
            String sender;
            if (senderQ != null)
                sender = senderQ.text();
            else if (isUnknownChannel)
                sender = null; // deleted user
            else
                throw new AssertionError(); // investigate – probably indicator of system measage?
            String body = messageE.selectFirst("div._2ph_._a6-p").text();
            String dateS = messageE.selectFirst("div._3-94._a6-o div._a72d").text();

            Element mediaE = messageE.selectFirst("img");
            if (mediaE == null)
                mediaE = messageE.selectFirst("video");

            String mediaS;
            if (mediaE != null) {
                mediaS = mediaE.attr("src");
                if (mediaS.contains("/stickers_used/"))
                    mediaS = null; // TODO
                else
                    mediaS = messageFile.getParent() + mediaS.substring(
                            mediaS.indexOf("/", mediaS.lastIndexOf("/") - 20));
            } else
                mediaS = null;

            if (body.equals("You missed a call from a Messenger user.") ||
                    body.equals("You are now connected on Messenger") ||
                    (body.endsWith(" to the group.") && body.startsWith(sender)))
                sender = "SYSTEM";

            FacebookMessage m;
            if (mediaS == null)
                m = new FacebookMessage(FacebookWallEvent.parseDate(dateS), body, channel, sender, isGroupChannel,
                        isUnknownChannel);
            else
                m = new FacebookMediaMessage(FacebookWallEvent.parseDate(dateS), body, channel, sender, isGroupChannel,
                        isUnknownChannel, mediaS);
            output.add(m);
        }
        return output;
    }
}
