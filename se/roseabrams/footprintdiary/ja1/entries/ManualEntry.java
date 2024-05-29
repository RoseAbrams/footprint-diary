package ja1.entries;

import ja1.interfaces.PlainText;
import ja1.DiaryDate;
import ja1.DiaryEntry;
import ja1.DiaryEntrySource;

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
