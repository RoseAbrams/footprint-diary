package se.roseabrams.footprintdiary.entries.health;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.interfaces.CustomCounted;

public class DailyActivity extends DiaryEntry implements CustomCounted {

    private int stepsTaken = 0;
    private float kmWalked = 0f;
    private float kcalBurned = 0f;

    public DailyActivity(DiaryDate dd) {
        super(DiaryEntryCategory.DAILY_HEALTH, dd);
    }

    public int stepsTaken() {
        return stepsTaken;
    }

    public float kmWalked() {
        return kmWalked;
    }

    public float kcalBurned() {
        return kcalBurned;
    }

    @Override
    public String getStringSummary() {
        return stepsTaken + " steps (" + kmWalked + " km, " + kcalBurned + " kcal)";
    }

    @Override
    public int getCustomCount() {
        return Math.round(kmWalked);
    }

    public static DailyActivity[] createDays(File exportFile) throws IOException {
        HashMap<DiaryDate, DailyActivity> output = new HashMap<>();
        HealthData[] h = HealthData.createAllFromXml(exportFile);
        for (HealthData d : h) {
            output.putIfAbsent(d.DATE, new DailyActivity(d.DATE));
            DailyActivity a = output.get(d.DATE);
            switch (d.TYPE) {
                case STEP_COUNT:
                    a.stepsTaken += d.VALUE;
                    break;
                case DISTANCE_MOVED:
                    a.kmWalked += d.VALUE;
                    break;
                case ENERGY_BURNED_ACTIVE:
                case ENERGY_BURNED_BASAL:
                    a.kcalBurned += d.VALUE;
                    break;
                default:
                    break;
            }
        }
        return output.values().toArray(new DailyActivity[output.size()]);
    }
}
