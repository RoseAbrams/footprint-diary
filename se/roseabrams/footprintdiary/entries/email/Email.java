package se.roseabrams.footprintdiary.entries.email;

import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.interfaces.Message;
import se.roseabrams.footprintdiary.interfaces.RichText;

public abstract class Email extends DiaryEntry implements RichText, Message {

    public EmailType guessType() {...}

    public enum EmailType {...}
}
