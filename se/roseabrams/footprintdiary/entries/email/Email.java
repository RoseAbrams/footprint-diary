package se.roseabrams.footprintdiary.entries.email;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.common.Message;

import com.pff.PSTAppointment;
import com.pff.PSTContact;
import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;
import com.pff.PSTObject;
import com.pff.PSTTask;

public class Email extends DiaryEntry implements Message {

    // TODO rethink these vars
    public final String SENDER_NAME;
    public final String SENDER_ADDRESS;
    public final String RECIPIENT;
    //public final String RECIPIENT_NAME;
    //public final String RECIPIENT_ADDRESS;
    public final String SUBJECT;
    public final Folder FOLDER;
    public final boolean IMPORTANT;

    public Email(DiaryDate dd, String senderName, String senderAddress, String recipient,
            String subject, Folder folder, boolean important) {
        super(DiaryEntryCategory.EMAIL, dd);
        SENDER_NAME = senderName;
        SENDER_ADDRESS = senderAddress;
        RECIPIENT = recipient;
        SUBJECT = subject;
        FOLDER = folder;
        IMPORTANT = important;
    }

    public Email(DiaryDate dd, String senderName, String senderAddress, String recipient,
            String subject, Folder folder, boolean important, String[] categories) {
        this(dd, senderName, senderAddress, recipient, subject, folder, important);
        // TODO
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

    @SuppressWarnings("unused")
    public static List<Email> createFromMbox(File emailFile) throws IOException {
        ArrayList<Email> output = new ArrayList<>();
        List<String> mboxLines = Util.readFileLines(emailFile);
        for (int i = 0; i < mboxLines.size(); i++) {
            String mboxLine = mboxLines.get(i);
            boolean currentIsEmail = false;
            long id;
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
                    DiaryDateTime bestDate = fieldDate; // TODO evaluate
                    Email e = new Email(bestDate, senderName, senderAddress, recipient, subject,
                            folder, important, categories.toArray(new String[categories.size()]));
                    output.add(e);
                }
                currentIsEmail = true;
                id = 0;
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
                subject = Util.decodeRfc2047(subject);
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
        return output;
    }

    private static DiaryDateTime parseDate(String s) {
    }

    public static List<Email> createFromPst(File emailFile) throws IOException {
        try {
            PSTFile f = new PSTFile(emailFile);
            return createFromPstRecurse(f.getRootFolder(), "root");
        } catch (PSTException e) {
            throw new Error(e);
        }
    }

    @SuppressWarnings("unused")
    private static List<Email> createFromPstRecurse(PSTFolder f, String folderPath) throws PSTException, IOException {
        String folderName = folderPath + "/" + f.getDisplayName();
        ArrayList<Email> output;
        Folder folderType;
        // TODO confirm folder paths, these are mostly guesses
        if (folderName.equals("root/Inbox")) {
            output = new ArrayList<>(12000);
            folderType = Folder.INBOX;
        } else if (folderName.equals("root/Inbox/KYM")) {
            output = new ArrayList<>(1000);
            folderType = Folder.INBOX;
        } else if (folderName.equals("root/Sent")) {
            output = new ArrayList<>(1000);
            folderType = Folder.SENT;
        } else {
            return new ArrayList<>(0);
        }
        int nContent = f.getContentCount();
        int nEmails = f.getEmailCount();
        int nContantActual = 0;
        for (PSTFolder sf : f.getSubFolders()) {
            createFromPstRecurse(sf, folderName);
        }
        while (true) {
            PSTObject o = f.getNextChild();
            if (o == null) {
                break;
            }
            int typeOfNodeI = o.getNodeType();
            if (o instanceof PSTAppointment || o instanceof PSTTask) {
                // ignore, probably very little useful data
            } else if (o instanceof PSTContact c) {
                // TODO "Recipient Cache" might have useful info
            } else if (o instanceof PSTMessage m) {
                int typeOfObjectI = m.getObjectType();
                String subject = m.getSubject();
                String body = m.getBody();
                String bodyHtml = m.getBodyHTML();
                String bodyPrefix = m.getBodyPrefix();
                String headers = m.getTransportMessageHeaders();
                Date receivedDate = m.getMessageDeliveryTime();
                Date submitDate = m.getClientSubmitTime();
                Date createdDate = m.getCreationTime();
                String senderName = m.getSenderName();
                String senderAddress = m.getSenderEmailAddress();
                String senderType = m.getSenderAddrtype();
                String recipientName = m.getReceivedByName();
                String recipientAddress = m.getReceivedByAddress();
                String recipientType = m.getReceivedByAddressType();
                String id = m.getInternetMessageId();
                int importance = m.getImportance();
                int priority = m.getPriority();
                boolean toMe = m.getMessageToMe();
                boolean fromMe = m.isFromMe();
                String address = m.getEmailAddress();
                boolean flagged = m.isFlagged();
                boolean read = m.isRead();
                /*Document bodyD = Jsoup.parse(bodyHtml);
                String bodyParsed = bodyD.text();*/

                DiaryDateTime date = new DiaryDateTime(receivedDate);
                Email e = new Email(date, senderName, senderAddress, recipientName, subject, folderType,
                        importance > 0);
                output.add(e);
            } else {
                System.err.println();
            }
            nContantActual++;
        }
        if (nContent != nContantActual) { // all seems to work, although main folder lacks ~800 (6 %) of emails
            System.err.println();
        }
        return output;
    }
}
