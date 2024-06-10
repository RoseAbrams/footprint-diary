package se.roseabrams.footprintdiary.content;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class RemoteContent extends Content {

    public final URL URL;

    public RemoteContent(String url) {
        super(...);
        try {
            URL = URI.create(url).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Illegal URL passed.", e);
        }
    }

    @Override
    public String getName() {
        return URL.getFile();
    }

    @Override
    public String getPath() {
        return URL.getPath();
    }
}
