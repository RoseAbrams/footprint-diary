package se.roseabrams.footprintdiary.entries.facebook;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.interfaces.Message;

public class MessengerMessage extends DiaryEntry implements Message {

    public final String TEXT;
    public final String CHANNEL;
    public final String SENDER;

    public MessengerMessage(DiaryDate dd, String text, String channel, String sender) {
        super(DiaryEntryCategory.FACEBOOK_MESSAGE, dd);
        TEXT = text;
        CHANNEL = channel;
        SENDER = sender;
    }

    @Override
    public String getStringSummary() {
        return SENDER + ": " + TEXT;
    }

    @Override
    public String getSender() {
        return SENDER;
    }

    @Override
    public String getRecipient() {
        return isByMe() ? CHANNEL : PersonalConstants.FACEBOOK_NAME;
    }

    @Override
    public boolean isByMe() {
        return SENDER.equals(PersonalConstants.FACEBOOK_NAME);
    }
}
