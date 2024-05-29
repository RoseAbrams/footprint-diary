package entries.discord;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.json.JSONObject;

import DiaryDate;
import DiaryDateTime;
import DiaryEntry;
import DiaryEntrySource;
import Util;
import entries.camera.CameraCapture;
import interfaces.Message;
import interfaces.PlainText;

public class DiscordMessage extends DiaryEntry implements Message, PlainText {
    private final long ID;
    private final String RECIPIENT;
    private final Type TYPE;
    private final String CONTENTS;

    public DiscordMessage(DiaryDate date, long id, String contents, String recipient, Type type) {
        super(DiaryEntrySource.DISCORD, date);
        ID = id;
        CONTENTS = contents;
        TYPE = type;
        RECIPIENT = recipient;
    }

    @Override
    public String getStringSummary() {
        return "\"" + CONTENTS + "\"";
    }

    @Override
    public String getSender() {
        return "ormen3757";
    }

    @Override
    public String getRecipient() {
        return RECIPIENT;
    }

    @Override
    public boolean isByMe() {
        return true;
    }

    public static DiscordMessage[] createAllFromCsv(File messagesDirectory) {
        ArrayList<DiscordMessage> output = new ArrayList<>();
        File f = new File(messagesDirectory, "index.json");
        JSONObject index = Util.loadJsonFile(f, 20000);
        Map<String, Object> indexMap = index.toMap();
        for (Entry<String, Object> i : indexMap.entrySet()) {
            String conversationCode = i.getKey();
            String conversationName = (String) i.getValue();

            Scanner s = new Scanner(new File(messagesDirectory + "\\c" + conversationCode, "messages.csv"));
            s.useDelimiter(",");

            Type type;
            String recipient;
            if (conversationName == null) {
                type = null;
                recipient = null;
            } else if (conversationName.startsWith("Direct Message with ")) {
                type = Type.DM;
                recipient = conversationName.substring(20);
            } else {
                type = Type.SERVER_CHANNEL;
                recipient = conversationName;
            }

            while (s.hasNextLine()) {
                Scanner s2 = new Scanner(s.nextLine());
                long id = s2.nextLong(10);
                String timestamp = s2.next();
                String contents = s2.next();
                String attachmentsUrl = s2.hasNext() ? s2.next() : null;
                s2.close();

                DiaryDateTime d = new DiaryDateTime(timestamp.substring(0, 20));
                if (attachmentsUrl == null) {
                    output.add(new DiscordMessage(d, id, contents, recipient, type));
                } else {
                    output.add(new DiscordFileMessage(d, id, contents, recipient, type, attachmentsUrl)); // filetypes?
                }
            }
            s.close();
        }
        return output.toArray(new DiscordMessage[output.size()]);
    }

    public static enum Type {
        DM, SERVER_CHANNEL
    }
}
