package se.roseabrams.footprintdiary.entries.discord;

import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.EntrySource;
import se.roseabrams.footprintdiary.interfaces.Message;
import se.roseabrams.footprintdiary.interfaces.PlainText;

public class DiscordMessage extends DiaryEntry implements Message, PlainText {

    public DiscordMessage(short year, byte month, byte day, byte hour, byte minute, byte second) {
        super(EntrySource.DISCORD, year, month, day, hour, minute, second);
    }
}
