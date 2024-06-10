package se.roseabrams.footprintdiary.interfaces;

import se.roseabrams.footprintdiary.content.Content;

public interface ContentOwner extends DiaryEntryData {

    public Content getContent();
}
