package se.roseabrams.footprintdiary.entries.facebook;

import java.io.File;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentOwner;
import se.roseabrams.footprintdiary.common.LocalContent;

public class FacebookMediaMessage extends FacebookMessage implements ContentOwner {

    public final LocalContent MEDIA;

    public FacebookMediaMessage(DiaryDateTime date, String text, String channel, String sender, File media) {
        super(date, text, channel, sender);
        MEDIA = new LocalContent(media);
    }

    @Override
    public Content getContent() {
        return MEDIA;
    }
}
