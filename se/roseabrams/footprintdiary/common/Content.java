package se.roseabrams.footprintdiary.common;

public abstract class Content {

    public final ContentType TYPE;
    public final String FILE_EXTENSION;

    protected Content(ContentType type) {
        TYPE = type;
        FILE_EXTENSION = null;
    }

    public Content(String fileExtension) {
        TYPE = ContentType.parseExtension(fileExtension);
        FILE_EXTENSION = fileExtension;
    }

    public abstract String getName();

    public abstract String getPath();

    @Override
    public String toString() {
        return getName();
    }

    static String getExtFromPath(String p) {
        return p.substring(p.lastIndexOf(".") + 1);
    }
}
