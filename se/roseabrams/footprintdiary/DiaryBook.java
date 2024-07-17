package se.roseabrams.footprintdiary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class DiaryBook extends Diary implements Serializable {

    private final ArrayList<DiaryPage> PAGES;

    public DiaryBook() {
        PAGES = new ArrayList<>(3000);
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
        DiaryPage newPage = new DiaryPage(e.DATE.reduce());
        PAGES.add(newPage);
        PAGES.sort(null);
        newPage.add(e);
    }

    public void addFillerPages() {
        for (int i = 0; i < PAGES.size() - 1; i++) {
            DiaryDate thisPageDate = PAGES.get(i).DATE;
            DiaryDate tomorrowPageDate = thisPageDate.tomorrow();
            DiaryDate nextPageDate = PAGES.get(i + 1).DATE;
            if (!tomorrowPageDate.equals(nextPageDate, false))
                PAGES.add(i + 1, new DiaryPage(tomorrowPageDate));
        }
    }

    public DiaryPage randomPage() {
        return PAGES.get((int) (PAGES.size() * Math.random()));
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
        String[] output = new String[PAGES.size()];
        for (int i = 0; i < PAGES.size(); i++) {
            output[i] = PAGES.get(i).prose();
        }
        return output;
    }
}