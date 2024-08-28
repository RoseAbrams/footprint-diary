package se.roseabrams.footprintdiary.entries.facebook;

import java.io.File;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentContainer;
import se.roseabrams.footprintdiary.common.LocalContent;

public class FacebookMediaPost extends FacebookPost implements ContentContainer {

    public final LocalContent MEDIA;

    public FacebookMediaPost(DiaryDateTime date, String text, Type type, String timeline, String app, String media) {
        super(date, text, type, timeline, app);
        MEDIA = media != null ? new LocalContent(new File(media)) : null; // sometimes the picture simply isn't there, nothing i can do🙁
    }

    @Override
    public Content getContent() {
        return MEDIA;
    }
}
