package se.roseabrams.footprintdiary.interfaces;

import se.roseabrams.footprintdiary.common.Content;

public interface ContentOwner extends DiaryEntryData {

    public Content getContent();
}
