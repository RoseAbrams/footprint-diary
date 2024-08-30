package se.roseabrams.footprintdiary.entries.whatsapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.common.Message;

public class WhatsAppMessage extends DiaryEntry implements Message {

    public final String SENDER;
    public final String RECIPIENT;
    public final String TEXT;
    private static final String FOLDER_PREFIX = "WhatsApp Chat - ";
    private static final String OPENING_DISCLAIMER = "\u200eMessages and calls are end-to-end encrypted. No one outside of this chat, not even WhatsApp, can read or listen to them.";

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

    public static List<WhatsAppMessage> createAllFromFolder(File chatsFolder) throws IOException {
        ArrayList<WhatsAppMessage> output = new ArrayList<>(10000);

        File[] chatFolders = chatsFolder.listFiles();
        for (File chatFolder : chatFolders) {
            if (chatFolder.isDirectory()) {
                File chatFile = new File(chatFolder.getAbsolutePath(), "_chat.txt");
                output.addAll(createFromTxt(chatFile, chatFolder));
            }
        }

        return output;
    }

    public static ArrayList<WhatsAppMessage> createFromTxt(File chatFile, File parent) throws IOException {
        ArrayList<WhatsAppMessage> output = new ArrayList<>(10000);
        final String channelName = parent.getName().substring(FOLDER_PREFIX.length());
        List<String> chatFileLines = Util.readFileLines(chatFile);
        for (int i = 0; i < chatFileLines.size(); i++) {
            String s2 = chatFileLines.get(i);
            assert hasProperLineStart(s2); // if not, multiline solution failed
            String timestamp1 = s2.substring(s2.indexOf("[") + 1, s2.indexOf(","));
            String timestamp2 = s2.substring(s2.indexOf(",") + 1, s2.indexOf("]"));
            String sender;
            String text;
            try {
                sender = s2.substring(s2.indexOf("]") + 2, s2.indexOf(": "));
                text = s2.substring(s2.indexOf(": ") + 2);
                if (text.equals(OPENING_DISCLAIMER))
                    continue;
            } catch (IndexOutOfBoundsException e) {
                // apparently there are empty messages
                sender = s2.substring(s2.indexOf("]") + 2);
                text = "";
            }

            while (i + 1 < chatFileLines.size() && !hasProperLineStart(chatFileLines.get(i + 1))) {
                text += "\n" + chatFileLines.get(i + 1);
                i++;
            }

            String attachmentS = null;
            File attachment = null;
            if (text.startsWith("<attached:") || text.startsWith("\u200e<attached:")) {
                attachmentS = text.substring(text.lastIndexOf(": ") + 2, text.lastIndexOf(">"));
                attachment = new File(parent.getAbsolutePath(), attachmentS);
            }

            DiaryDateTime date = new DiaryDateTime(timestamp1 + timestamp2);
            // because ", " has two chars and this is for one char

            WhatsAppMessage w;
            if (attachmentS != null)
                w = new WhatsAppMediaMessage(date, sender, channelName, text, attachment);
            else
                w = new WhatsAppMessage(date, sender, channelName, text);
            output.add(w);
        }
        return output;
    }

    private static boolean hasProperLineStart(String s) {
        if (s.isEmpty())
            return false;
        char c = s.charAt(0);
        return c == '[' || c == '\u200e';
    }
}
