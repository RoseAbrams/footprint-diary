package se.roseabrams.footprintdiary.entries.discord;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntrySource;
import se.roseabrams.footprintdiary.interfaces.Message;
import se.roseabrams.footprintdiary.interfaces.PlainText;

public class DiscordMessage extends DiaryEntry implements Message, PlainText {

    public DiscordMessage(DiaryDate date) {
        super(DiaryEntrySource.DISCORD, date);
    }
}
