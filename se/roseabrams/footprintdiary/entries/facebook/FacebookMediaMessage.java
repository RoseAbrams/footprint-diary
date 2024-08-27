package se.roseabrams.footprintdiary.entries.facebook;

import java.io.File;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentContainer;
import se.roseabrams.footprintdiary.common.LocalContent;

public class FacebookMediaMessage extends FacebookMessage implements ContentContainer {

    public final LocalContent MEDIA;

    public FacebookMediaMessage(DiaryDateTime date, String text, String channel, String sender, String mediaS) {
        super(date, text, channel, sender);
        MEDIA = new LocalContent(new File(mediaS));
    }

    @Override
    public Content getContent() {
        return MEDIA;
    }
}
