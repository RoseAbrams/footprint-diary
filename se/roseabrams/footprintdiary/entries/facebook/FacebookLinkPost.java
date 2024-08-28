package se.roseabrams.footprintdiary.entries.facebook;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentContainer;
import se.roseabrams.footprintdiary.common.Webpage;

public class FacebookLinkPost extends FacebookPost implements ContentContainer {

    public final Webpage LINK;

    public FacebookLinkPost(DiaryDateTime dd, String body, Type type, String timeline, String app, String linkS) {
        super(dd, body, type, timeline, app);
        LINK = new Webpage(linkS);
    }

    @Override
    public Content getContent() {
        return LINK;
    }
}
