package se.roseabrams.footprintdiary.common;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class RemoteContent extends Content {

    public final URL URL;

    RemoteContent(ContentType type, String url) {
        super(type);
        try {
            URL = URI.create(url).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Illegal URL passed.", e);
        }
    }

    public RemoteContent(String url) {
        this(getTypeFromContextOrExt(url), url);
    }

    @Override
    public String getName() {
        return URL.getFile();
    }

    @Override
    public String getPath() {
        return URL.toExternalForm();
    }

    static ContentType getTypeFromContextOrExt(String p) {
        if (p.contains("youtube.com/watch?v="))
            return ContentType.VIDEO;

        return ContentType.parseExtension(getExtFromPath(p));
    }
}
