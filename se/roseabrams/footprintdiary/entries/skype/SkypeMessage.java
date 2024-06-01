package se.roseabrams.footprintdiary.entries.skype;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntrySource;
import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.interfaces.Message;
import se.roseabrams.footprintdiary.interfaces.PlainText;

public class SkypeMessage extends DiaryEntry implements Message, PlainText {
    public final String SENDER;
    public final String RECIPIENT;
    public final String MESSAGE;

    public SkypeMessage(DiaryDateTime dd, String user, String recipient, String message) {
        super(DiaryEntrySource.SKYPE, dd);
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
        return SENDER == PersonalConstants.SKYPE_MY_USERNAME;
    }

    public static SkypeMessage[] createFromTxt(File skypeTxt) throws IOException {
        ArrayList<SkypeMessage> output = new ArrayList<>();
        List<String> messages = Util.readFileLines(skypeTxt);
        for (String message : messages) {
            String timestampS = message.substring(message.indexOf("["), message.indexOf("]"));
            DiaryDateTime timestamp = new DiaryDateTime(Short.parseShort("20" + timestampS.substring(6, 8)),
                    Byte.parseByte(timestampS.substring(3, 5)), Byte.parseByte(timestampS.substring(0, 2)),
                    Byte.parseByte(timestampS.substring(9, 11)), Byte.parseByte(timestampS.substring(12, 14)),
                    Byte.parseByte(timestampS.substring(15, 17)));
            String user = message.substring(message.indexOf("]") + 2, message.indexOf(":"));
            String messageBody = message.substring(message.indexOf(":") + 2);

            output.add(new SkypeMessage(timestamp, user, messageBody));
        }
        return output.toArray(new SkypeMessage[output.size()]);
    }
}