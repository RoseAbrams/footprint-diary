package se.roseabrams.footprintdiary.entries.whatsapp;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.interfaces.ContentOwner;

public class WhatsAppMediaMessage extends WhatsAppMessage implements ContentOwner {

    public WhatsAppMediaMessage(DiaryDateTime date, String sender, String channelName, String text,
            String attachmentS) {
        super(date, sender, attachmentS, text);
        ... // somehow translate simple filename to full path
    }
}
