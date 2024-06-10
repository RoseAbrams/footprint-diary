package se.roseabrams.footprintdiary.entries.discord;

import java.net.URL;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.interfaces.Picture;

public class DiscordPictureMessage extends DiscordFileMessage implements Picture {

    public DiscordPictureMessage(DiaryDate date, long id, String contents, String recipient, Type type,
            URL attachmentUrl) {
        super(date, id, contents, recipient, type, attachmentUrl);
    }
}
