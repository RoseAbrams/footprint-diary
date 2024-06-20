package se.roseabrams.footprintdiary.common;

public abstract class Content {

    public final ContentType TYPE;
    public final String FILE_EXTENSION;

    @Deprecated // ext should not be null if possible
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
        if (p.contains("?"))
            p = p.substring(0, p.indexOf("?"));
        String output = p.substring(p.lastIndexOf(".") + 1);
        if (output == null || output.isBlank())
            throw new IllegalArgumentException("Cannot determine file extension from \"" + p + "\"");
        return output;
    }
}
