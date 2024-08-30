package se.roseabrams.footprintdiary.entries.steam;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;

public abstract class SteamEvent extends DiaryEntry {

    public SteamEvent(DiaryEntryCategory source, DiaryDate dd) {
        super(source, dd);
    }
}
