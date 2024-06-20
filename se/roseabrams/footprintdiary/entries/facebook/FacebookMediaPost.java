package se.roseabrams.footprintdiary.entries.facebook;

import java.io.File;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentContainer;
import se.roseabrams.footprintdiary.common.LocalContent;

public class FacebookMediaPost extends FacebookPost implements ContentContainer {
    /* "your_photos.html", "your_videos.html", "your_uncategorized_photos.html" */

    public final LocalContent MEDIA;

    public FacebookMediaPost(DiaryDateTime date, String text, Type type, String timeline, File media) {
        super(date, text, type, timeline);
        MEDIA = new LocalContent(media);
    }

    @Override
    public Content getContent() {
        return MEDIA;
    }
}
