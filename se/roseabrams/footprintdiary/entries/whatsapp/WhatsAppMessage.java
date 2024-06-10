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
import se.roseabrams.footprintdiary.interfaces.Message;

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
        return TEXT;
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

    public static DiaryEntry[] createAllFromTxt(File chatsFolder) throws IOException {
        ArrayList<WhatsAppMessage> output = new ArrayList<>(10000);

        File[] chatFiles = chatsFolder.listFiles();
        for (File chatFile : chatFiles) {
            output.addAll(createFromTxt(chatFile, chatFile.getName()));
        }

        return output.toArray(new WhatsAppMessage[output.size()]);
    }

    public static ArrayList<WhatsAppMessage> createFromTxt(File chatFile, String channelName) throws IOException {
        ArrayList<WhatsAppMessage> output = new ArrayList<>(10000);
        Scanner s = new Scanner(chatFile);
        while (s.hasNextLine()) {
            String s2 = s.nextLine();
            String timestamp1 = s2.substring(0, s2.indexOf(","));
            String timestamp2 = s2.substring(s2.indexOf(",") + 1, s2.indexOf("]"));
            String sender = s2.substring(s2.indexOf("]") + 2, s2.indexOf(": "));
            String text = s2.substring(s2.indexOf(": ") + 2);
            String attachmentS = null;
            if (text.startsWith("<attached")) {
                attachmentS = s2.substring(s2.lastIndexOf(": "), s2.lastIndexOf(">"));
            }

            DiaryDateTime date = new DiaryDateTime(timestamp1 + timestamp2);
            // because ", " has two chars and this is for one char

            if (attachmentS != null) {
                output.add(new WhatsAppMediaMessage(date, sender, channelName, text, attachmentS));
            } else {
                output.add(new WhatsAppMessage(date, sender, channelName, text));
            }
        }
        s.close();
        return output;
    }
}
