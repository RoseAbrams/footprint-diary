package se.roseabrams.footprintdiary;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DiaryBook extends Diary implements Serializable {
    private final DiaryPage[] PAGES;

    public DiaryBook(DiaryDate starDate, DiaryDate endDate) {
        PAGES = new DiaryPage[starDate.differenceDays(endDate)];
        GregorianCalendar iDate = (GregorianCalendar) starDate.getDetailedDate().clone();
        for (int i = 0; i < PAGES.length; i++) {
            iDate.add(Calendar.DAY_OF_MONTH, 1);
            PAGES[i] = new DiaryPage(new DiaryDate((short) iDate.get(GregorianCalendar.YEAR),
                    (byte) iDate.get(GregorianCalendar.MONTH), (byte) iDate.get(GregorianCalendar.DAY_OF_MONTH)));
        }
    }

    public void add(DiaryEntry[] entries) {
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
        System.err.println("Entry date (" + e.DATE + ") was outside of Diary date range (" + PAGES[0].DATE + " - "
                + PAGES[PAGES.length - 1].DATE + ")");
    }
}