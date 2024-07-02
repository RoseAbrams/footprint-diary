package se.roseabrams.footprintdiary.common;

public class Webpage extends RemoteContent {

    public final String TITLE;
    public final ContentType CONTAINED_TYPE;

    @Deprecated // try to include the title
    public Webpage(String url) {
        this(url, null);
    }

    public Webpage(String url, String title) {
        this(url, title, null);
    }

    public Webpage(String url, String title, ContentType containedType) {
        super(ContentType.WEBPAGE, url);
        assert title == null || !title.isBlank();
        TITLE = title;
        CONTAINED_TYPE = containedType;
    }
}
