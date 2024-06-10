package se.roseabrams.footprintdiary.content;

import java.io.File;

public class LocalContent extends Content {

    public final File FILE;

    public LocalContent(File file) {
        super(getExtFromPath(file.getName()));
        FILE = file;
    }

    @Override
    public String getName() {
        return FILE.getName();
    }

    @Override
    public String getPath() {
        return FILE.getPath();
    }
}
