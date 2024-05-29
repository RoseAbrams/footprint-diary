package ja1.entries.email;

import ja1.DiaryEntry;
import ja1.interfaces.Message;
import ja1.interfaces.RichText;

public abstract class Email extends DiaryEntry implements RichText, Message {

    public EmailType guessType() {...}

    public enum EmailType {...}
}
