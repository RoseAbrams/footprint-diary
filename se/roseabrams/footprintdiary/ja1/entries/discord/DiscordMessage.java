package ja1.entries.discord;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.json.JSONObject;

import ja1.DiaryDate;
import ja1.DiaryDateTime;
import ja1.DiaryEntry;
import ja1.DiaryEntrySource;
import ja1.Util;
import ja1.entries.camera.CameraCapture;
import ja1.interfaces.Message;
import ja1.interfaces.PlainText;

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
        File f = new File(messagesDirectory, "index.json");
        JSONObject index = Util.loadJsonFile(f, 20000);
        Map<String, Object> indexMap = index.toMap();
        for (Entry<String, Object> i : indexMap.entrySet()) {
            String conversationCode = i.getKey();
            String conversationName = (String) i.getValue();

            ArrayList<CameraCapture> output = new ArrayList<>();
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
                    new DiscordMessage(d, id, contents, recipient, type);
                } else {
                    new DiscordFileMessage(d, id, contents, recipient, type, attachmentsUrl); // distinguish filetypes?
                }
            }
        }
    }

    public static enum Type {
        DM, SERVER_CHANNEL
    }
}
