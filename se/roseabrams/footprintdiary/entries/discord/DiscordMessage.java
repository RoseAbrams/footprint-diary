package se.roseabrams.footprintdiary.entries.discord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    public final ChannelType TYPE;
    public final String CONTENTS;

    public DiscordMessage(DiaryDate date, long id, String contents, String recipient, ChannelType type) {
        super(DiaryEntryCategory.DISCORD, date);
        ID = id;
        CONTENTS = contents;
        TYPE = type;
        RECIPIENT = recipient.intern();
    }

    @Override
    public String getStringSummary() {
        return CONTENTS;
    }

    @Override
    public String getSender() {
        return PersonalConstants.DISCORD_USERNAME;
    }

    @Override
    public String getRecipient() {
        return RECIPIENT;
    }

    @Override
    public boolean isByMe() {
        return true;
    }

    public static DiscordMessage[] createAllFromCsv(File messagesDirectory) throws IOException {
        ArrayList<DiscordMessage> output = new ArrayList<>(10000);
        File indexFile = new File(messagesDirectory, "index.json");
        JSONObject index = Util.readJsonFile(indexFile);
        Map<String, Object> indexMap = index.toMap();
        for (Entry<String, Object> i : indexMap.entrySet()) {
            String conversationCode = i.getKey();
            String conversationName = (String) i.getValue();

            File conversationFile = new File(messagesDirectory + "\\c" + conversationCode, "messages.csv");
            CSVParser s = new CSVParser(conversationFile);

            ChannelType type;
            String recipient;
            if (conversationName == null) {
                type = null;
                recipient = null;
            } else if (conversationName.startsWith("Direct Message with ")) {
                type = ChannelType.DM;
                recipient = conversationName.substring("Direct Message with ".length());
            } else {
                type = ChannelType.SERVER;
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

                String[] attachmentsUrls;
                if (attachments.contains(" ")) {
                    ArrayList<String> attachmentsUrlsL = new ArrayList<>();
                    Scanner s2 = new Scanner(attachments);
                    s2.delimiter();
                    while (s2.hasNext()) {
                        attachmentsUrlsL.add(s2.next());
                    }
                    attachmentsUrls = attachmentsUrlsL.toArray(new String[attachmentsUrlsL.size()]);
                } else {
                    attachmentsUrls = new String[1];
                    attachmentsUrls[0] = attachments;
                }

                DiaryDateTime date = new DiaryDateTime(dateS.substring(0, 20));
                DiscordMessage d;
                if (attachments != null && !attachments.isBlank())
                    d = new DiscordFileMessage(date, id, contents, recipient, type, attachmentsUrls);
                else
                    d = new DiscordMessage(date, id, contents, recipient, type);
                output.add(d);
            }
            s.close();
        }
        return output.toArray(new DiscordMessage[output.size()]);
    }

    public static enum ChannelType {
        DM, SERVER
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
