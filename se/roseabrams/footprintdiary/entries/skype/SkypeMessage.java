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
import se.roseabrams.footprintdiary.common.Message;

public class SkypeMessage extends DiaryEntry implements Message {

    public final String SENDER;
    public final String RECIPIENT;
    public final String MESSAGE;

    public SkypeMessage(DiaryDateTime dd, String sender, String recipient, String message) {
        super(DiaryEntryCategory.SKYPE, dd);

        assert !sender.isBlank();
        assert !recipient.isBlank();
        assert message != null && !message.isBlank();

        SENDER = sender.intern();
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

    public static List<SkypeMessage> createAllFromTxt(File directory) throws IOException {
        ArrayList<SkypeMessage> output = new ArrayList<>(50000);
        for (File convFile : directory.listFiles()) {
            output.addAll(createFromTxt(convFile));
        }
        return output;
    }

    public static ArrayList<SkypeMessage> createFromTxt(File skypeTxt) throws IOException {
        ArrayList<SkypeMessage> output = new ArrayList<>(50000);
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

        for (int i = 0; i < messages.size(); i++) {
            String message = messages.get(i);
            if (message.isBlank())
                continue;

            String timestampS = message.substring(message.indexOf("[") + 1, message.indexOf("]"));
            DiaryDateTime timestamp;
            if (timestampS.charAt(2) == '/') {
                timestamp = new DiaryDateTime(Short.parseShort("20" + timestampS.substring(6, 8)),
                        Byte.parseByte(timestampS.substring(3, 5)), Byte.parseByte(timestampS.substring(0, 2)),
                        Byte.parseByte(timestampS.substring(9, 11)), Byte.parseByte(timestampS.substring(12, 14)),
                        Byte.parseByte(timestampS.substring(15, 17)));
            } else if (timestampS.charAt(4) == '-') {
                timestamp = new DiaryDateTime(timestampS);
            } else {
                throw new AssertionError();
            }
            String sender;
            String messageBody = null;
            if (message.contains("] ***"))
                if (message.contains(" sent ")) {
                    sender = message.substring(message.indexOf("]") + 6, message.indexOf(" sent "));
                    messageBody = message.substring(message.indexOf(" sent ") + " sent ".length(), message.length() - 4);
                } else
                    sender = "SYSTEM";
            else
                sender = message.substring(message.indexOf("]") + 2, message.indexOf(": "));
            if (messageBody == null)
                messageBody = message.substring(message.indexOf(":", message.indexOf("]")) + 2);

            while (i < messages.size() - 1 && !messages.get(i + 1).startsWith("[")) {
                messageBody += messageBody + "\n" + messages.get(i + 1);
                i++;
            }

            output.add(new SkypeMessage(timestamp, sender, recipient, messageBody));
        }
        return output;
    }
}