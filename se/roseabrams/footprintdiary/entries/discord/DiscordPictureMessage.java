package se.roseabrams.footprintdiary.entries.discord;

import java.net.URL;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.interfaces.Picture;
import se.roseabrams.footprintdiary.interfaces.RemoteResource;

public class DiscordPictureMessage extends DiscordMessage implements Picture, RemoteResource {

    public final URL ATTACHMENT_URL;

    public DiscordPictureMessage(DiaryDate date, long id, String contents, String recipient, Type type,
            URL attachmentUrl) {
        super(date, id, contents, recipient, type);
        ATTACHMENT_URL = attachmentUrl;
    }

    @Override
    public String getPathToResource() {
        return ATTACHMENT_URL.toString();
    }

    @Override
    public URL getURLOfResource() {
        return ATTACHMENT_URL;
    }
}
