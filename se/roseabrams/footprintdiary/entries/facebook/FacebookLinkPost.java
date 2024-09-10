package se.roseabrams.footprintdiary.entries.facebook;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentContainer;
import se.roseabrams.footprintdiary.common.Webpage;

public class FacebookLinkPost extends FacebookPost implements ContentContainer {

    public final Webpage LINK;

    public FacebookLinkPost(DiaryDateTime dd, String body, Type type, String timeline, String app, String linkS) {
        super(dd, body, type, timeline, app);
        LINK = linkS != null ? new Webpage(linkS) : null; // some cases where the link simply isn't provided, so far I've only seen it with Spotify embeds
    }

    @Override
    public Content getContent() {
        return LINK;
    }
}
