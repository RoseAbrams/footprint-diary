package se.roseabrams.footprintdiary.entries.health;

import DiaryDate;
import DiaryEntry;
import DiaryEntrySource;

public abstract class HealthData extends DiaryEntry {

    public HealthData(DiaryDate date) {
        super(DiaryEntrySource.HEALTH, date);
    }
}
