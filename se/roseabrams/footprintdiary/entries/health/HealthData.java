package se.roseabrams.footprintdiary.entries.health;

import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.EntrySource;

public abstract class HealthData extends DiaryEntry {

    public HealthData(short year, byte month, byte day, byte hour, byte minute, byte second) {
        super(EntrySource.HEALTH, year, month, day, hour, minute, second);
    }
}
