package se.roseabrams.footprintdiary.entries.skype;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.interfaces.Message;
import se.roseabrams.footprintdiary.interfaces.PlainText;

public class SkypeMessage extends DiaryEntry implements Message, PlainText {
    public final String SENDER;
    public final String RECIPIENT;
    public final String MESSAGE;

    public SkypeMessage(DiaryDateTime dd, String user, String recipient, String message) {
        super(DiaryEntryCategory.SKYPE, dd);
        SENDER = user.intern();
        RECIPIENT = recipient.intern();
        MESSAGE = message;
    }

    @Override
    public String getStringSummary() {
        return SENDER + ": " + MESSAGE;
    }

    @Override
    public String getSender() {
        return SENDER;
    }

    @Override
    public String getRecipient() {
        return RECIPIENT;
    }

    @Override
    public boolean isByMe() {
        return SENDER.equals(PersonalConstants.SKYPE_USERNAME);
    }

    public static DiaryEntry[] createAllFromTxt(File directory) throws IOException {
        ArrayList<SkypeMessage> output = new ArrayList<>();
        for (File convFile : directory.listFiles()) {
            output.addAll(createFromTxt(convFile));
        }
        return output.toArray(new DiaryEntry[output.size()]);
    }

    public static ArrayList<SkypeMessage> createFromTxt(File skypeTxt) throws IOException {
        ArrayList<SkypeMessage> output = new ArrayList<>();
        List<String> messages = Util.readFileLines(skypeTxt);

        String recipient = null;
        for (String message : messages) {
            String sender = message.substring(message.indexOf("]") + 2, message.indexOf(": "));
            if (!sender.equals(PersonalConstants.SKYPE_USERNAME)) {
                recipient = sender;
                break;
            }
        }
        assert recipient != null;

        for (String message : messages) {
            String timestampS = message.substring(message.indexOf("["), message.indexOf("]"));
            DiaryDateTime timestamp = new DiaryDateTime(Short.parseShort("20" + timestampS.substring(6, 8)),
                    Byte.parseByte(timestampS.substring(3, 5)), Byte.parseByte(timestampS.substring(0, 2)),
                    Byte.parseByte(timestampS.substring(9, 11)), Byte.parseByte(timestampS.substring(12, 14)),
                    Byte.parseByte(timestampS.substring(15, 17)));
            String sender = message.substring(message.indexOf("]") + 2, message.indexOf(": "));
            String messageBody = message.substring(message.indexOf(":") + 2);

            output.add(new SkypeMessage(timestamp, sender, recipient, messageBody));
        }
        return output;
    }
}