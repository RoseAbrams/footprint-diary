package se.roseabrams.footprintdiary.entries.email;

import DiaryEntry;
import interfaces.Message;
import interfaces.RichText;

public abstract class Email extends DiaryEntry implements RichText, Message {

    public EmailType guessType() {...}

    public enum EmailType {...}
}
