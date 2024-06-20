package se.roseabrams.footprintdiary.common;

import java.util.Set;

public interface ContentsContainer extends Contentable {

    public Set<Content> getContents();
}
