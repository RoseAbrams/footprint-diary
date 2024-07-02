package se.roseabrams.footprintdiary.entries.email;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.common.Message;

public class Email /*extends DiaryEntry implements Message*/ {
/*
    public Email(DiaryDate dd) {
        super(DiaryEntryCategory.EMAIL, dd);
    }

    private static final Pattern MBOX_MESSAGE_START_LINE = Pattern.compile("From [0-9]{19}@.*");

    //public EmailType guessType() {...} // keywords in subject and body

    //public enum EmailType {...}

    public static Email[] createFromMbox(File emailFile) {
        ArrayList<Email> output = new ArrayList<>();
        List<String> mboxLines = Util.readFileLines(emailFile);
        for (String mboxLine : mboxLines) {
            boolean currentIsEmail = false;
            long id;
            DiaryDateTime arrivedDate;
            String sender;
            String recipient;
            String subject;
            if (mboxLine.matches(MBOX_MESSAGE_START_LINE.pattern())) {
                if (currentIsEmail) {
                    ... // reset all
                    Email e = new Email(sender, recipient, subject);
                    output.add(e);
                }
                currentIsEmail = true;
                String idS = mboxLine.substring(5, mboxLine.indexOf("@"));
                String dateS = mboxLine.substring(mboxLine.indexOf("@") + 4); // looks like always UTC-0
                ...
            } else if (mboxLine.startsWith("Subject: ")) {
                subject = mboxLine.substring(mboxLine.indexOf(":") + 1);
                if (subject.startsWith("=?utf-8")) {
                    ...
                }
            } else if (mboxLine.startsWith("Delivered-To: ")) {
                recipient = mboxLine.substring(mboxLine.indexOf(":") + 1);
            } else if (mboxLine.startsWith("Received: from ")) {
                ... // looks like only first such entry per email is the real one
                recipient = mboxLine.substring(mboxLine.indexOf(":") + 1);
            } else if (mboxLine.startsWith("Date: ")) {
                ... // looks same or very close to firstline
            } else if (mboxLine.startsWith("X-Gmail-Labels: ")) {
                ... // probably differentiates sent/received and other markings
            }
        }
    }*/
}
// found by quick googling, good starting point? https://github.com/epfromer/pst-extractor
