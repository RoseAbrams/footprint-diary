package se.roseabrams.footprintdiary.interfaces;

public interface Message extends DiaryEntryData {
    public String getSender();

    public String getRecipient();
}
