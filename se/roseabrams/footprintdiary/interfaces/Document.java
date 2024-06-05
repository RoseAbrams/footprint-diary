package se.roseabrams.footprintdiary.interfaces;

import se.roseabrams.footprintdiary.Filetype;

public interface Document extends Resource {
    // word, excel, powerpoint, pdf, any common format of styled ultra-rich content

    public static final Filetype FILETYPE = Filetype.DOCUMENT;
}
