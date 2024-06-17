package se.roseabrams.footprintdiary.entries.whatsapp;

import java.io.File;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentOwner;
import se.roseabrams.footprintdiary.common.LocalContent;

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
