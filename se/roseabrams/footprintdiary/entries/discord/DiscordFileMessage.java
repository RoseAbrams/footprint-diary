package se.roseabrams.footprintdiary.entries.discord;

import java.util.Set;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentsContainer;
import se.roseabrams.footprintdiary.common.RemoteContent;

public class DiscordFileMessage extends DiscordMessage implements ContentsContainer {

    private final RemoteContent[] ATTACHMENTS;

    public DiscordFileMessage(DiaryDate date, long id, String contents, String recipient, boolean isGroupChannel,
            boolean isUnknownChannel, String[] attachmentUrls) {
        super(date, id, contents, recipient, isGroupChannel, isUnknownChannel);
        ATTACHMENTS = new RemoteContent[attachmentUrls.length];
        for (int i = 0; i < attachmentUrls.length; i++) {
            ATTACHMENTS[i] = new RemoteContent(attachmentUrls[i]);
        }
    }

    @Override
    public Set<Content> getContents() {
        return Set.of(ATTACHMENTS);
    }
    /*
     * @Override
     * public StringBuilder detailedCsv(StringBuilder s, String delim) {
     * return super.detailedCsv(s, delim).append(delim).append(ATTACHMENT_URL);
     * }
     */
}
