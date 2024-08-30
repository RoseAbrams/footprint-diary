package se.roseabrams.footprintdiary.entries.email;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.common.Message;

public class Email extends DiaryEntry implements Message {

    public final String SENDER_NAME;
    public final String SENDER_ADDRESS;
    public final String RECIPIENT;
    public final String SUBJECT;
    public final Folder FOLDER;
    public final boolean IMPORTANT;

    public Email(DiaryDate dd, String senderName, String senderAddress, String recipient,
            String subject, Folder folder, boolean important, String[] strings) {
        super(DiaryEntryCategory.EMAIL, dd);
        SENDER_NAME = senderName;
        SENDER_ADDRESS = senderAddress;
        RECIPIENT = recipient;
        SUBJECT = subject;
        FOLDER = folder;
        IMPORTANT = important;
    }

    @Override
    public String getStringSummary() {
        return SUBJECT;
    }

    @Override
    public String getSender() {
        return SENDER_ADDRESS;
    }

    @Override
    public String getRecipient() {
        return RECIPIENT;
    }

    @Override
    public boolean isByMe() {
        return FOLDER == Folder.SENT;
    }

    public static enum Folder {
        INBOX, SENT, TRASH;
    }

    private static final Pattern MBOX_MESSAGE_START_LINE = Pattern.compile("From [0-9]{19}@.*");

    //public EmailType guessType() {...} // keywords in subject and body

    //public enum EmailType {...}

    public static List<Email> createFromMbox(File emailFile) throws IOException {
        ArrayList<Email> output = new ArrayList<>();
        List<String> mboxLines = Util.readFileLines(emailFile);
        for (int i = 0; i < mboxLines.size(); i++) {
            String mboxLine = mboxLines.get(i);
            boolean currentIsEmail = false;
            long id;
            DiaryDateTime bestDate = null;
            DiaryDateTime firstlineDate = null;
            DiaryDateTime fieldDate = null;
            String senderName = null;
            String senderAddress = null;
            String recipient = null;
            String subject = null;
            Folder folder = null;
            boolean important = false;
            ArrayList<String> categories = new ArrayList<>(5);
            if (mboxLine.matches(MBOX_MESSAGE_START_LINE.pattern())) {
                if (currentIsEmail) {
                    currentIsEmail = false;
                    Email e = new Email(bestDate, senderName, senderAddress, recipient, subject,
                            folder, important, categories.toArray(new String[categories.size()]));
                    output.add(e);
                }
                currentIsEmail = true;
                id = 0;
                bestDate = null;
                firstlineDate = null;
                fieldDate = null;
                senderName = null;
                senderAddress = null;
                recipient = null;
                subject = null;
                folder = null;
                important = false;
                categories.clear();
                String idS = mboxLine.substring(5, mboxLine.indexOf("@"));
                firstlineDate = parseDate(mboxLine.substring(mboxLine.indexOf("@") + 4)); // looks like always UTC-0
            } else if (mboxLine.startsWith("Subject: ")) {
                subject = mboxLine.substring(mboxLine.indexOf(":") + 2);
                while (mboxLines.get(i + 1).startsWith(" ")) {
                    subject += mboxLines.get(i + 1);
                    i++;
                }
                while (subject.contains("=?") && subject.contains("?=")) {
                    /// TODO doesn't solve partial encodes
                    int posEncodeStart = subject.indexOf("=?") + 2;
                    int posEncodeEnd = subject.indexOf("?=");
                    int posEncodePayloadStart = subject.lastIndexOf("?", posEncodeEnd) + 1;
                    String encode = subject.substring(posEncodeStart - 2, posEncodeEnd + 2);
                    String encodingName = subject.substring(posEncodeStart, posEncodePayloadStart - 1);
                    String payload = subject.substring(posEncodePayloadStart, posEncodeEnd);
                    String payloadDecoded = new String(Base64.getDecoder().decode(payload.getBytes(encodingName)));
                    subject = subject.replace(payload, payloadDecoded);
                }
            } else if (mboxLine.startsWith("To: ")) {
                recipient = mboxLine.substring(mboxLine.indexOf(":") + 2);
            } else if (mboxLine.startsWith("From: ") && !mboxLine.startsWith("From: From:")) {
                String sender = mboxLine.substring(mboxLine.indexOf(":") + 2);
                senderName = sender.substring(0, sender.indexOf("<"));
                senderAddress = sender.substring(sender.indexOf("<") + 1, sender.indexOf(">"));
            } else if (mboxLine.startsWith("Date: ")) {
                fieldDate = parseDate(mboxLine.substring(mboxLine.indexOf(":") + 2));
            } else if (mboxLine.startsWith("X-Gmail-Labels: ")) {
                Scanner s = new Scanner(mboxLine.substring(mboxLine.indexOf(":") + 2));
                s.useDelimiter(",");
                while (s.hasNext()) {
                    String label = s.next();
                    if (label.startsWith("Category ")) {
                        categories.add(label.substring("Category ".length()).intern());
                    } else {
                        switch (label) {
                            case "Inbox":
                                folder = Folder.INBOX;
                                break;
                            case "Sent":
                                folder = Folder.SENT;
                                break;
                            case "Spam":
                                folder = Folder.TRASH;
                                break;
                            case "Important":
                                important = true;
                                break;
                            case "Opened":
                            case "Unread":
                                break;
                            default:
                                throw new AssertionError();
                        }
                    }
                }
                s.close();
            }
        }
    }

    public static List<Email> createFromPstJson(File emailFile) {
    }

    private static DiaryDateTime parseDate(String substring) {
    }
}
