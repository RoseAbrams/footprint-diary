package ja1.entries.health;

import ja1.DiaryDate;
import ja1.DiaryEntry;
import ja1.DiaryEntrySource;

public abstract class HealthData extends DiaryEntry {

    public HealthData(DiaryDate date) {
        super(DiaryEntrySource.HEALTH, date);
    }
}
