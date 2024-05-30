package se.roseabrams.footprintdiary.entries.steam;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntrySource;

public abstract class SteamEvent extends DiaryEntry {

    public SteamEvent(DiaryEntrySource source, DiaryDate dd) {
        super(DiaryEntrySource.STEAM, dd);
    }
}
