package se.roseabrams.footprintdiary.entries.discord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import org.json.JSONObject;

import se.roseabrams.footprintdiary.CSVParser;
import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.common.Message;

public class DiscordMessage extends DiaryEntry implements Message {

    public final long ID;
    public final String RECIPIENT;
    public final boolean IS_GROUP_CHANNEL;
    public final boolean IS_UNKNOWN_CHANNEL;
    public final String CONTENTS;
    private static final String DM_CHANNEL_PREFIX = "Direct Message with ";

    public DiscordMessage(DiaryDate date, long id, String contents, String recipient, boolean isGroupChannel,
            boolean isUnknownChannel) {
        super(DiaryEntryCategory.DISCORD, date);
        ID = id;
        CONTENTS = contents;
        IS_GROUP_CHANNEL = isGroupChannel;
        IS_UNKNOWN_CHANNEL = isUnknownChannel;
        RECIPIENT = recipient.intern();
    }

    @Override
    public String getStringSummary() {
        return "(" + getRecipient() + ") " + getSender() + ": " + CONTENTS;
    }

    @Override
    public String getSender() {
        return PersonalConstants.DISCORD_USERNAME;
    }

    @Override
    public String getRecipient() {
        if (IS_UNKNOWN_CHANNEL)
            return "unknown channel " + RECIPIENT;
        else if (IS_GROUP_CHANNEL)
            return "server channel " + RECIPIENT;
        else
            return RECIPIENT;
    }

    @Override
    public boolean isByMe() {
        return true;
    }

public static List<DiscordMessage> createAllFromCsv(File messagesDirectory) throws IOException {
        ArrayList<DiscordMessage> output = new ArrayList<>(10000);
        File indexFile = new File(messagesDirectory, "index.json");
        JSONObject index = Util.readJsonObjectFile(indexFile);
        Map<String, Object> indexMap = index.toMap();
        for (Entry<String, Object> i : indexMap.entrySet()) {
            String conversationCode = i.getKey();
            String conversationName = (String) i.getValue();

            File conversationFile = new File(messagesDirectory + "\\c" + conversationCode, "messages.csv");
            CSVParser s = new CSVParser(conversationFile);

            boolean isGroupChannel;
            boolean isUnknownChannel = false;
            String recipient;
            if (conversationName == null) {
                // index.json gives the channelname as null, so not much else can be discerned
                isUnknownChannel = true;
                isGroupChannel = false;
                recipient = conversationCode;
            } else if (conversationName.startsWith(DM_CHANNEL_PREFIX)) {
                isGroupChannel = false;
                recipient = conversationName.substring(DM_CHANNEL_PREFIX.length());
            } else {
                isGroupChannel = true;
                recipient = conversationName;
            }

            s.nextLine(); // skip past headings
            while (s.hasNext()) {
                String[] l = s.nextLineTokens();
                String idS = l[0];
                long id = Long.parseLong(idS);
                String dateS = l[1];
                String contents = l[2];
                String attachments = l[3];

                String[] attachmentUrls;
                if (attachments.contains(" ")) {
                    ArrayList<String> attachmentsUrlsL = new ArrayList<>();
                    Scanner s2 = new Scanner(attachments);
                    s2.delimiter();
                    while (s2.hasNext()) {
                        attachmentsUrlsL.add(s2.next());
                    }
                    s2.close();
                    attachmentUrls = attachmentsUrlsL.toArray(new String[attachmentsUrlsL.size()]);
                } else {
                    attachmentUrls = new String[1];
                    attachmentUrls[0] = attachments;
                }

                DiaryDateTime date = new DiaryDateTime(dateS.substring(0, 20));
                DiscordMessage d;
                if (attachments != null && !attachments.isBlank())
                    d = new DiscordFileMessage(date, id, contents, recipient, isGroupChannel, isUnknownChannel,
                            attachmentUrls);
                else
                    d = new DiscordMessage(date, id, contents, recipient, isGroupChannel, isUnknownChannel);
                output.add(d);
            }
            s.close();
        }
        return output;
    }
    /*
     * @Override
     * public StringBuilder detailedCsv(StringBuilder s, String delim) {
     * return
     * s.append(ID).append(delim).append(RECIPIENT).append(delim).append(TYPE).
     * append(delim).append(CONTENTS);
     * }
     */
}
