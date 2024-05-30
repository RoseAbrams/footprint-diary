package se.roseabrams.footprintdiary.entries.whatsapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntrySource;
import se.roseabrams.footprintdiary.interfaces.Message;
import se.roseabrams.footprintdiary.interfaces.PlainText;

public class WhatsAppMessage extends DiaryEntry implements Message, PlainText {
    public final String SENDER;
    public final String RECIPIENT;
    public final String TEXT;

    public WhatsAppMessage(DiaryDate dd, String sender, String recipient, String text) {
        super(DiaryEntrySource.WHATSAPP, dd);
        this.SENDER = sender.intern();
        this.RECIPIENT = recipient.intern();
        this.TEXT = text;
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
        return SENDER == "Rosa";
    }

    public static DiaryEntry[] createAllFromTxt(...) {
    }

    public static WhatsAppMessage[] createFromTxt(File chatFile, String channelName) throws FileNotFoundException {
        ArrayList<WhatsAppMessage> output = new ArrayList<>();
        Scanner s = new Scanner(chatFile);
        while (s.hasNextLine()) {
            String s2 = s.nextLine();
            String timestamp1 = s2.substring(0, s2.indexOf(','));
            String timestamp2 = s2.substring(s2.indexOf(',') + 1, s2.indexOf(']'));
            String sender = s2.substring(s2.indexOf(']') + 2, s2.indexOf(": "));
            String text = s2.substring(s2.indexOf(": ") + 2);

            DiaryDateTime date = new DiaryDateTime(timestamp1 + timestamp2); // because ", " has two chars and this is
                                                                             // for one char

            if (text.startsWith("<attached")) {
                // ?
            } else {
                output.add(new WhatsAppMessage(date, sender, channelName, text));
            }
        }
        s.close();
        return output.toArray(new WhatsAppMessage[output.size()]);
    }
}
