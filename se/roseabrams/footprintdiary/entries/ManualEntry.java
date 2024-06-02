package se.roseabrams.footprintdiary.entries;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.interfaces.PlainText;

public class ManualEntry extends DiaryEntry implements PlainText {

    public final String TEXT;

    public ManualEntry(DiaryDate dd, String text) {
        super(DiaryEntryCategory.MANUAL, dd);
        TEXT = text;
    }

    @Override
    public String getStringSummary() {
        return TEXT;
    }
}
