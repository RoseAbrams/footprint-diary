package se.roseabrams.footprintdiary.entries.health;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntrySource;

public abstract class HealthData extends DiaryEntry {

    public HealthData(DiaryDate date) {
        super(DiaryEntrySource.ACTIVITY, date);
    }
}
