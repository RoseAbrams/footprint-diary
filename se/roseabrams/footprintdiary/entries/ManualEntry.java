package entries;

import DiaryDate;
import DiaryEntry;
import DiaryEntrySource;
import interfaces.PlainText;

public class ManualEntry extends DiaryEntry implements PlainText {

    private final String TEXT;

    public ManualEntry(DiaryDate dd, String text) {
        super(DiaryEntrySource.MANUAL, dd);
        TEXT = text;
    }

    @Override
    public String getStringSummary() {
        return TEXT;
    }
}
