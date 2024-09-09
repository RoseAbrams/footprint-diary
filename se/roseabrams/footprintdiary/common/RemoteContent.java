package se.roseabrams.footprintdiary.common;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class RemoteContent extends Content {

    public final URL URL;

    RemoteContent(ContentType type, String url) {
        super(type);
        try {
            if (!url.contains("://")) {
                System.err.println("URL \"" + url + "\" has no protocol, will assume HTTP");
                url = "http://" + url;
            }
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
        if (p.contains("youtube.com/watch?v=") || p.contains("youtu.be/") || p.contains("v.redd.it/"))
            return ContentType.VIDEO;
        if (p.contains("reddit.com/gallery") || p.contains("imgur.com/"))
            return ContentType.PICTURE;
        if (p.contains("docs.google.com/document/"))
            return ContentType.DOCUMENT;
        if (p.contains("wikipedia.org/") || p.contains("knowyourmeme.com/"))
            return ContentType.BOOK;
        if (p.contains("/r/") && p.contains("/comments/")) // crosspost
            return null;
        if (p.contains("/reddit.com/")) // weird unexplorable edge case
            return null;

        try {
            return ContentType.parseExtension(getExtFromPath(p));
        } catch (RuntimeException e) {
            return ContentType.WEBPAGE;
        }
    }
}
