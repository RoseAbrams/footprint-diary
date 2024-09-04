package se.roseabrams.footprintdiary.entries.email;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.pff.PSTAppointment;
import com.pff.PSTContact;
import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;
import com.pff.PSTObject;

public class PstIngestTest {
    public static void main(String[] args) throws PSTException, IOException {
        PSTFile f = new PSTFile(new File(
                "C:/Users/RosaAbrahamsson/Downloads/outlook backup 2023-05-04 - 8fbe75217ef14aa3a4fdfc010ace07f9.pst"));
        readFolder(f.getRootFolder());
    }

    private static void readFolder(PSTFolder f) throws PSTException, IOException {
        String folderName = f.getDisplayName();
        int nContent = f.getContentCount();
        int nEmails = f.getEmailCount();
        int nContantActual = 0;
        for (PSTFolder sf : f.getSubFolders()) {
            readFolder(sf);
        }
        while (true) {
            PSTObject o = f.getNextChild();
            if (o == null) {
                break;
            }
            if (o instanceof PSTContact || o instanceof PSTAppointment) {
            } else if (o instanceof PSTMessage m) {
                String subject = m.getSubject();
                String body = m.getBody();
                String bodyHtml = m.getBodyHTML();
                String bodyPrefix = m.getBodyPrefix();
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
                boolean toMe = m.getMessageToMe();
                boolean fromMe = m.isFromMe();
                String address = m.getEmailAddress();
                boolean flagged = m.isFlagged();
                boolean read = m.isRead();
                if (!bodyHtml.isBlank()) {
                    Document bodyD = Jsoup.parse(bodyHtml);
                    String bodyParsed = bodyD.text();
                    System.out.println(subject.isBlank() ? "[BLANK SUBJECT]" : subject);
                }
            } else if (o instanceof PSTFolder sf) {
                readFolder(sf);
            } else {
                System.err.println();
            }
            nContantActual++;
        }
        if (nContent != nContantActual) { // all seems to work, although main folder lacks ~800 (6 %) of emails
            System.err.println();
        }
    }
}
