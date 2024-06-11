package se.roseabrams.footprintdiary.entries.whatsapp;

import java.io.File;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.content.Content;
import se.roseabrams.footprintdiary.content.LocalContent;
import se.roseabrams.footprintdiary.interfaces.ContentOwner;

public class WhatsAppMediaMessage extends WhatsAppMessage implements ContentOwner {

    public final LocalContent ATTACHMENT;

    public WhatsAppMediaMessage(DiaryDateTime date, String sender, String channelName, String text,
            File attachment) {
        super(date, sender, channelName, text);
        ATTACHMENT = new LocalContent(attachment);
    }

    @Override
    public Content getContent() {
        return ATTACHMENT;
    }
}
