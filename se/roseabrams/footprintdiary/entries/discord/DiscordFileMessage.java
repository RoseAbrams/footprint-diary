package se.roseabrams.footprintdiary.entries.discord;

import java.net.URL;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.interfaces.RemoteResource;

public class DiscordFileMessage extends DiscordMessage implements RemoteResource {

    public final URL ATTACHMENT_URL;

    public DiscordFileMessage(DiaryDate date, long id, String contents, String recipient, Type type,
            URL attachmentUrl) {
        super(date, id, contents, recipient, type);
        ATTACHMENT_URL = attachmentUrl;
    }

    @Override
    public String getPathToResource() {
        return ATTACHMENT_URL.toString();
    }

    @Override
    public URL getUrlOfResource() {
        return ATTACHMENT_URL;
    }
/*
    @Override
    public StringBuilder detailedCsv(StringBuilder s, String delim) {
        return super.detailedCsv(s, delim).append(delim).append(ATTACHMENT_URL);
    }*/
}
