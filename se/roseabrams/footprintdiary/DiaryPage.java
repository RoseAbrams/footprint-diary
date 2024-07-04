package se.roseabrams.footprintdiary;

import java.io.Serializable;

public abstract class DiaryPage extends Diary implements Serializable {

    public final DiaryDate DATE;

    public DiaryPage(DiaryDate date) {
        DATE = date;
    }

    public abstract String sumsCsv();

    public abstract String indexCsv();

    public abstract String prose();
}
