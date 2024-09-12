package se.roseabrams.footprintdiary.common;

import java.io.Serializable;

public class TitledString implements Serializable {
    public final String TITLE;
    public final String BODY;

    public TitledString(String title, String body) {
        assert title != null && !title.isBlank();
        assert body != null;// && !body.isBlank(); // TODO consider how to handle this
        TITLE = title.intern();
        BODY = body;
    }

    @Override
    @Deprecated /// only for human-readability during debugging, not practical for anything internal
    public String toString() {
        return TITLE + ": " + (BODY.length() > 50 ? BODY.substring(0, 47) + "..." : BODY);
    }
}
