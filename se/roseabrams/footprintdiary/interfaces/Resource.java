package se.roseabrams.footprintdiary.interfaces;

import se.roseabrams.footprintdiary.Filetype;

public interface Resource extends DiaryEntryData {

    public static final Filetype FILETYPE = null;

    public String getPathToResource();

    // public Filetype getFiletype();
}
