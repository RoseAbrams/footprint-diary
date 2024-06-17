package se.roseabrams.footprintdiary.entries.discord;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentOwner;
import se.roseabrams.footprintdiary.common.RemoteContent;

public class DiscordFileMessage extends DiscordMessage implements ContentOwner {

    public final RemoteContent ATTACHMENT;

    public DiscordFileMessage(DiaryDate date, long id, String contents, String recipient, Type type,
            String attachmentUrl) {
        super(date, id, contents, recipient, type);
        ATTACHMENT = new RemoteContent(attachmentUrl);
    }

    @Override
    public Content getContent() {
        return ATTACHMENT;
    }
/*
    @Override
    public StringBuilder detailedCsv(StringBuilder s, String delim) {
        return super.detailedCsv(s, delim).append(delim).append(ATTACHMENT_URL);
    }*/
}
