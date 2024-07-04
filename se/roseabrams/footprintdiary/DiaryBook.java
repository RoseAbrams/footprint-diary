package se.roseabrams.footprintdiary;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

public class DiaryBook extends Diary implements Serializable {

    private final DiaryPage[] PAGES;
    @SuppressWarnings("unused")
    private int discardedOutsideDateRange;

    public DiaryBook(DiaryDate starDate, DiaryDate endDate) { // exclusive enddate
        PAGES = new DiaryPage[starDate.differenceDays(endDate)];
        GregorianCalendar iDate = (GregorianCalendar) starDate.getDetailedDate().clone();
        for (int i = 0; i < PAGES.length; i++) {
            PAGES[i] = new DiaryPage(new DiaryDate((short) iDate.get(GregorianCalendar.YEAR),
                    (byte) (iDate.get(GregorianCalendar.MONTH) + 1), (byte) iDate.get(GregorianCalendar.DAY_OF_MONTH)));
            iDate.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    public void add(DiaryEntry[] entries) {
        for (DiaryEntry e : entries) {
            add(e);
        }
    }

    public void add(Collection<? extends DiaryEntry> entries) {
        for (DiaryEntry e : entries) {
            add(e);
        }
    }

    public void add(DiaryEntry e) {
        for (DiaryPage page : PAGES) {
            if (page.DATE.equals(e.DATE, false)) {
                page.add(e);
                return;
            }
        }
        discardedOutsideDateRange++;
    }

    public DiaryPage randomPage() {
        return PAGES[(int) (PAGES.length * Math.random())];
    }

    public DiaryEntry randomEntry() {
        return randomPage().randomEntry();
    }

    public String sumsCsv(boolean blankZeroes) {
        StringBuilder output = new StringBuilder(100000);
        for (DiaryEntryCategory s : DiaryEntryCategory.valuesCustomOrder()) {
            output.append(s).append(",");
        }
        output.deleteCharAt(output.length() - 1);
        output.append("\n");
        for (DiaryPage page : PAGES) {
            output.append(page.sumsCsv()).append("\n");
        }
        return blankZeroes ? output.toString().replace(",0", ",") : output.toString();
    }

    public String indexCsv() {
        StringBuilder output = new StringBuilder(1000000);
        output.append(DiaryEntry.indexCsvHeaders(","));
        for (DiaryPage page : PAGES) {
            output.append(page.indexCsv());
        }
        return output.toString();
    }

    public String[] prose() {
        String[] output = new String[PAGES.length];
        for (int i = 0; i < PAGES.length; i++) {
            output[i] = PAGES[i].prose();
        }
        return output;
    }
}