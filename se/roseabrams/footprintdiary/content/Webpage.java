package se.roseabrams.footprintdiary.content;

public class Webpage extends RemoteContent {

    public final ContentType CONTAINED_TYPE;

    public Webpage(String url) {
        this(url, null);
    }

    public Webpage(String url, ContentType containedType) {
        super(ContentType.WEBPAGE, url);
        CONTAINED_TYPE = containedType;
    }
}
