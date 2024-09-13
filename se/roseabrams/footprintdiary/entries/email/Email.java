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

    public final String SENDER_NAME;
    public final String SENDER_ADDRESS;
    public final String RECIPIENT_NAME;
    public final String RECIPIENT_ADDRESS;
    public final String SUBJECT;
    public final Folder FOLDER;
    public final int IMPORTANCE;
    public final int PRIORITY;

    public Email(DiaryDate dd, String senderName, String senderAddress, String recipientName, String recipientAddress,
            String subject, Folder folder, int importance, int priority, String[] categories) {
        super(DiaryEntryCategory.EMAIL, dd);

        assert senderName != null || senderAddress != null;
        assert senderName == null || !senderName.isBlank();
        assert senderAddress == null || !senderAddress.isBlank();
        assert recipientName != null || recipientAddress != null;
        assert recipientName == null || !recipientName.isBlank();
        assert recipientAddress == null || !recipientAddress.isBlank();
        assert subject != null && !subject.isBlank();
        assert categories == null || categories.length > 0;

        SENDER_NAME = senderName;
        SENDER_ADDRESS = senderAddress;
        RECIPIENT_NAME = recipientName;
        RECIPIENT_ADDRESS = recipientAddress;
        SUBJECT = subject;
        FOLDER = folder;
        IMPORTANCE = importance;
        PRIORITY = priority;
    }

    @Override
    public String getStringSummary() {
        return SUBJECT;
    }

    @Override
    public String getSender() {
        if (SENDER_NAME != null && SENDER_ADDRESS != null)
            return SENDER_NAME + " <" + SENDER_ADDRESS + ">";
        if (SENDER_NAME != null)
            return SENDER_NAME;
        if (SENDER_ADDRESS != null)
            return SENDER_ADDRESS;
        throw new AssertionError();
    }

    @Override
    public String getRecipient() {
        if (RECIPIENT_NAME != null && RECIPIENT_ADDRESS != null)
            return RECIPIENT_NAME + " <" + RECIPIENT_ADDRESS + ">";
        if (RECIPIENT_NAME != null)
            return RECIPIENT_NAME;
        if (RECIPIENT_ADDRESS != null)
            return RECIPIENT_ADDRESS;
        throw new AssertionError();
    }

    @Override
    public boolean isByMe() {
        return FOLDER == Folder.SENT;
    }

    public static enum Folder {
        INBOX, SENT, TRASH;
    }

    private static final Pattern MBOX_MESSAGE_START_LINE = Pattern.compile("From [0-9]{19}@.*");

    //public EmailType guessType() {...} // keywords in subject and body, or use ML clustering+classification

    //public enum EmailType {...}

    @SuppressWarnings("unused")
    public static List<Email> createFromMbox(File emailFile) throws IOException {
        ArrayList<Email> output = new ArrayList<>();
        List<String> mboxLines = Util.readFileLines(emailFile);

        boolean currentIsEmail = false;
        long id;
        DiaryDateTime firstlineDate = null;
        DiaryDateTime fieldDate = null;
        String senderName = null;
        String senderAddress = null;
        String recipientName = null;
        String recipientAddress = null;
        String subject = null;
        Folder folder = null;
        boolean important = false;
        ArrayList<String> categories = new ArrayList<>(5);

        for (int iLine = 0; iLine < mboxLines.size(); iLine++) {
            String mboxLine = mboxLines.get(iLine);
            if (mboxLine.matches(MBOX_MESSAGE_START_LINE.pattern())) {
                if (currentIsEmail) {
                    currentIsEmail = false;
                    DiaryDateTime bestDate = fieldDate; // TODO evaluate
                    Email e = new Email(bestDate, senderName, senderAddress, recipientName, recipientAddress, subject,
                            folder, important ? 1 : 0, 0, categories.toArray(new String[categories.size()]));
                    output.add(e);
                }
                currentIsEmail = true;
                id = 0;
                firstlineDate = null;
                fieldDate = null;
                senderName = null;
                senderAddress = null;
                recipientName = null;
                recipientAddress = null;
                subject = null;
                folder = null;
                important = false;
                categories.clear();
                String idS = mboxLine.substring(5, mboxLine.indexOf("@"));
                firstlineDate = parseDate(mboxLine.substring(mboxLine.indexOf("@") + 4)); // looks like always UTC-0
            } else if (mboxLine.startsWith("Subject: ")) {
                subject = mboxLine.substring(mboxLine.indexOf(":") + 2);
                while (mboxLines.get(iLine + 1).startsWith(" ")) {
                    subject += mboxLines.get(iLine + 1);
                    iLine++;
                }
                subject = Util.decodeRfc2047(subject);
            } else if (mboxLine.startsWith("To: ")) {
                String[] recipient = parseContact(mboxLine.substring(mboxLine.indexOf(":") + 2));
                recipientName = recipient[0];
                recipientAddress = recipient[1];
            } else if (mboxLine.startsWith("From: ") && !mboxLine.startsWith("From: From:")) {
                String[] sender = parseContact(mboxLine.substring(mboxLine.indexOf(":") + 2));
                senderName = sender[0];
                senderAddress = sender[1];
            } else if (mboxLine.startsWith("Date: ")) {
                fieldDate = parseDate(mboxLine.substring(mboxLine.indexOf(":") + 2));
            } else if (mboxLine.startsWith("X-Gmail-Labels: ")) {
                Scanner s = new Scanner(mboxLine.substring(mboxLine.indexOf(":") + 2));
                s.useDelimiter(",");
                while (s.hasNext()) {
                    String label = s.next();
                    if (label.startsWith("Category "))
                        categories.add(label.substring("Category ".length()).intern());
                    else {
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

    private static String[] parseContact(String s) {
        String[] output = { null, null };

        if (s.contains("<")) {
            output[0] = s.substring(0, s.indexOf("<"));
            output[1] = s.substring(s.indexOf("<") + 1, s.indexOf(">"));
        } else if (s.contains("@") && !s.trim().contains(" "))
            output[1] = s;
        else
            output[0] = s; // don't think this will ever happen, keep on lookout

        for (int i = 0; i < output.length; i++) {
            if (output[i] == null)
                continue;
            output[i] = output[i].trim();
            if (output[i].startsWith("\"") && output[i].endsWith("\"")) {
                output[i] = output[i].substring(1, output[i].length() - 1);
                output[i] = output[i].trim();
            }
        }

        return output;
    }

    private static DiaryDateTime parseDate(String s) {
        String[] dateComps = s.split(" ");
        assert dateComps.length == 6;
        byte day = Byte.parseByte(dateComps[1]);
        byte month = DiaryDate.parseMonthName(dateComps[2]);
        short year = Short.parseShort(dateComps[3]);
        String[] timeComps = dateComps[4].split(":");
        byte hour = Byte.parseByte(timeComps[0]);
        byte minute = Byte.parseByte(timeComps[1]);
        byte second = Byte.parseByte(timeComps[2]);
        return new DiaryDateTime(year, month, day, hour, minute, second);
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
        // TODO confirm folder paths, these are just guesses
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
            if (o == null)
                break;

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
                Email e = new Email(date, senderName, senderAddress, recipientName, recipientAddress, subject,
                        folderType, importance, priority, null);
                output.add(e);
            } else
                throw new AssertionError();

            nContantActual++;
        }
        assert nContent == nContantActual;// all seems to work, although main folder lacks ~800 (6 %) of emails

        return output;
    }
}
