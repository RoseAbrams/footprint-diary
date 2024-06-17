package se.roseabrams.footprintdiary.entries.discord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
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
    public final Type TYPE;
    public final String CONTENTS;

    public DiscordMessage(DiaryDate date, long id, String contents, String recipient, Type type) {
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

            CSVParser s = new CSVParser(new File(messagesDirectory + "\\c" + conversationCode, "messages.csv"));

            Type type;
            String recipient;
            if (conversationName == null) {
                type = null;
                recipient = null;
            } else if (conversationName.startsWith("Direct Message with ")) {
                type = Type.DM;
                recipient = conversationName.substring("Direct Message with ".length());
            } else {
                type = Type.SERVER_CHANNEL;
                recipient = conversationName;
            }

            s.nextLine(); // skip past headings
            while (s.hasNext()) {
                String[] l = s.nextLineTokens();
                String idS = l[0];
                long id = Long.parseLong(idS);
                String timestamp = l[1];
                String contents = l[2];
                String attachmentsUrlS = l[3];

                DiaryDateTime dd = new DiaryDateTime(timestamp.substring(0, 20));
                DiscordMessage d;
                if (attachmentsUrlS != null && !attachmentsUrlS.isBlank()) {
                    d = new DiscordFileMessage(dd, id, contents, recipient, type, attachmentsUrlS);
                } else {
                    d = new DiscordMessage(dd, id, contents, recipient, type);
                }
                output.add(d);
            }
            s.close();
        }
        return output.toArray(new DiscordMessage[output.size()]);
    }

    public static enum Type {
        DM, SERVER_CHANNEL
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
