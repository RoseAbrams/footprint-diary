package ja1.entries.discord;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONObject;

import ja1.DiaryDate;
import ja1.DiaryDateTime;
import ja1.DiaryEntry;
import ja1.DiaryEntrySource;
import ja1.entries.camera.CameraCapture;
import ja1.interfaces.Message;
import ja1.interfaces.PlainText;

public class DiscordMessage extends DiaryEntry implements Message, PlainText {
    private final long ID;
    private final String RECIPIENT;
    private final String CONTENTS;

    public DiscordMessage(DiaryDate date, long id, String contents, String recipient) {
        super(DiaryEntrySource.DISCORD, date);
        ID = id;
        RECIPIENT = recipient;
        CONTENTS = contents;
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
        JSONObject j = new JSONObject();
        ...;
        HashMap<String, String> a = new HashMap<>();
        ...;
    }

    public static DiscordMessage[] createFromCsv(File csvFile, String recipient) {
        ArrayList<CameraCapture> output = new ArrayList<>();
        Scanner s = new Scanner(csvFile);
        s.useDelimiter(",");

        while (s.hasNextLine()) {
            Scanner s2 = new Scanner(s.nextLine());
            long id = s2.nextLong(10);
            String timestamp = s2.next();
            String contents = s2.next();
            String attachmentsUrl = s2.hasNext() ? s2.next() : null;
            s2.close();

            DiaryDateTime d = new DiaryDateTime(timestamp.substring(0, 20));
            if (attachmentsUrl == null) {
                new DiscordMessage(d, id, contents, recipient);
            } else {
                new DiscordFileMessage(d, id, contents, recipient, attachmentsUrl);  // possible to distinguish filetypes?
            }
        }
    }
}
