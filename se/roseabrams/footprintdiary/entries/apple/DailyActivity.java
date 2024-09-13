package se.roseabrams.footprintdiary.entries.apple;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.common.CustomCountable;

public class DailyActivity extends DiaryEntry implements CustomCountable {

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

    public static List<DailyActivity> createDays(File exportFile) throws IOException {
        ArrayList<DailyActivity> output = new ArrayList<>(2000);
        List<HealthData> h = HealthData.createAllFromXml(exportFile);
        for (HealthData d : h) {
            DailyActivity foundA = null;
            for (DailyActivity iA : output) {
                if (d.DATE.equals(iA.DATE, false)) {
                    foundA = iA;
                    break;
                }
            }
            if (foundA == null) {
                foundA = new DailyActivity(d.DATE);
                output.add(foundA);
            }
            switch (d.TYPE) {
                case STEP_COUNT:
                    foundA.stepsTaken += d.VALUE;
                    break;
                case DISTANCE_MOVED:
                    foundA.kmWalked += d.VALUE;
                    break;
                case ENERGY_BURNED_ACTIVE:
                case ENERGY_BURNED_BASAL:
                    foundA.kcalBurned += d.VALUE;
                    break;
                default:
                    break;
            }
        }
        return output;
    }
}
