package se.roseabrams.footprintdiary.entries.discord;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.json.JSONObject;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntrySource;
import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.interfaces.Message;
import se.roseabrams.footprintdiary.interfaces.PlainText;

public class DiscordMessage extends DiaryEntry implements Message, PlainText {
    public final long ID;
    public final String RECIPIENT;
    public final Type TYPE;
    public final String CONTENTS;

    public DiscordMessage(DiaryDate date, long id, String contents, String recipient, Type type) {
        super(DiaryEntrySource.DISCORD, date);
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
        ArrayList<DiscordMessage> output = new ArrayList<>();
        File f = new File(messagesDirectory, "index.json");
        JSONObject index = Util.readJsonFile(f);
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
                String attachmentsUrlS = s2.hasNext() ? s2.next() : null;
                s2.close();
                URL attachmentsUrl;
                try {
                    attachmentsUrl = URI.create(attachmentsUrlS).toURL();
                } catch (MalformedURLException e) {
                    System.err.println("Invalid attachment URL for Discord message: " + attachmentsUrlS);
                    attachmentsUrl = null;
                }

                DiaryDateTime dd = new DiaryDateTime(timestamp.substring(0, 20));
                DiscordMessage d;
                if (attachmentsUrl == null) {
                    d = new DiscordMessage(dd, id, contents, recipient, type);
                } else {
                    String filetype = attachmentsUrlS.substring(attachmentsUrlS.substring(70).indexOf('.'),
                            attachmentsUrlS.indexOf('?') - 1);
                    switch (filetype.toLowerCase()) {
                        case "jpg":
                        case "png":
                            d = new DiscordPictureMessage(dd, id, contents, recipient, type, attachmentsUrl);
                            break;
                        default:
                            throw new UnsupportedOperationException("Unrecognized filetype: " + filetype);
                    }
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
}
