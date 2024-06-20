package se.roseabrams.footprintdiary.entries.whatsapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.common.Message;

public class WhatsAppMessage extends DiaryEntry implements Message {

    public final String SENDER;
    public final String RECIPIENT;
    public final String TEXT;

    public WhatsAppMessage(DiaryDate dd, String sender, String recipient, String text) {
        super(DiaryEntryCategory.WHATSAPP, dd);
        SENDER = sender.intern();
        RECIPIENT = recipient.intern();
        TEXT = text;
    }

    @Override
    public String getStringSummary() {
        return SENDER + ": " + TEXT;
    }

    @Override
    public String getSender() {
        return SENDER;
    }

    @Override
    public String getRecipient() {
        return RECIPIENT;
    }

    @Override
    public boolean isByMe() {
        return SENDER.equals(PersonalConstants.WHATSAPP_NAME);
    }

    public static DiaryEntry[] createAllFromFolder(File chatsFolder) throws IOException {
        ArrayList<WhatsAppMessage> output = new ArrayList<>(10000);

        File[] chatFolders = chatsFolder.listFiles();
        for (File chatFolder : chatFolders) {
            assert chatFolder.isDirectory();
            File chatFile = new File(chatFolder.getAbsolutePath(), "_chat.txt");
            output.addAll(createFromTxt(chatFile, chatFolder));
        }

        return output.toArray(new WhatsAppMessage[output.size()]);
    }

    public static ArrayList<WhatsAppMessage> createFromTxt(File chatFile, File parent) throws IOException {
        ArrayList<WhatsAppMessage> output = new ArrayList<>(10000);
        Scanner s = new Scanner(chatFile);
        ArrayList<String> chatFileLines = new ArrayList<>();
        while (s.hasNextLine()) {
            chatFileLines.add(s.nextLine());
        }
        for (int i = 0; i < chatFileLines.size(); i++) {
            String s2 = chatFileLines.get(i);
            assert s2.charAt(0) == '['; // if not, multiline solution failed
            String timestamp1 = s2.substring(0, s2.indexOf(","));
            String timestamp2 = s2.substring(s2.indexOf(",") + 1, s2.indexOf("]"));
            String sender = s2.substring(s2.indexOf("]") + 2, s2.indexOf(": "));
            String text = s2.substring(s2.indexOf(": ") + 2);

            while (chatFileLines.get(i + 1).charAt(0) != '[') {
                text += "\n" + chatFileLines.get(i + 1);
                i++;
            }

            String attachmentS = null;
            File attachment = null;
            if (text.startsWith("<attached:")) {
                attachmentS = text.substring(text.lastIndexOf(": ") + 2, text.lastIndexOf(">"));
                attachment = new File(parent.getAbsolutePath(), attachmentS);
            }

            DiaryDateTime date = new DiaryDateTime(timestamp1 + timestamp2);
            // because ", " has two chars and this is for one char

            if (attachmentS != null) {
                output.add(new WhatsAppMediaMessage(date, sender, parent.getName(), text, attachment));
            } else {
                output.add(new WhatsAppMessage(date, sender, parent.getName(), text));
            }
        }
        s.close();
        return output;
    }
}
