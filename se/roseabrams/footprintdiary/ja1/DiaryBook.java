package ja1;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DiaryBook extends Diary {
    private final DiaryPage[] PAGES;

    public DiaryBook(DiaryDate starDate, DiaryDate endDate) {
        PAGES = new DiaryPage[starDate.differenceDays(endDate)];
        GregorianCalendar iDate = (GregorianCalendar) starDate.getDetailedDate().clone();
        for (int i = 0; i < PAGES.length; i++) {
            iDate.add(Calendar.DATE, 1);
            PAGES[i] = new DiaryPage(new DiaryDate(iDate));
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
            }
        }
    }
}