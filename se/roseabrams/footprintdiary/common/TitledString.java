package se.roseabrams.footprintdiary.common;

import java.io.Serializable;

public class TitledString implements Serializable {
    public final String TITLE;
    public final String BODY;

    public TitledString(String title, String body) {
        assert title != null && !title.isBlank();
        assert body == null || !body.isBlank();
        TITLE = title.intern();
        BODY = body;
    }

    @Override
    @Deprecated /// only for human-readability during debugging, never use for program logic
    public String toString() {
        return "––– " + TITLE + " –––\n" + BODY;
    }
}
