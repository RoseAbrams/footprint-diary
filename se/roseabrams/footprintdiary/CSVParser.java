package se.roseabrams.footprintdiary;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class CSVParser implements Iterator<String>, Closeable {

    private String fBuffer;
    private int fPosition = 0;
    private String fDelim = ",";
    private String fNewline = "\n";
    private String fQuote = "\"";

    public CSVParser(File input) throws IOException {
        this(Util.readFile(input));
    }

    public CSVParser(String input) {
        fBuffer = input;
    }

    public void useDelimiter(String s) {
        fDelim = s.intern();
    }

    @Override
    public String next() {
        expectHasNext();
        String nextChar = Character.toString(fBuffer.charAt(fPosition));
        TokenType t;
        if (nextChar.equals(fQuote)) {
            String nextNextChar = Character.toString(fBuffer.charAt(fPosition + 1));
            if (nextNextChar.equals(fQuote))
                t = TokenType.TWO_QUOTE;
            else
                t = TokenType.ONE_QUOTE;
        } else
            t = TokenType.NO_QUOTE;
        int endPosition;
        int newPosition;
        switch (t) {
            case NO_QUOTE:
                int posDelim = fBuffer.indexOf(fDelim, fPosition);
                int posNewline = fBuffer.indexOf(fNewline, fPosition);
                if (posDelim == -1)
                    if (posNewline == -1)
                        endPosition = fBuffer.length() - 1;
                    else
                        endPosition = posNewline;
                else if (posNewline == -1)
                    endPosition = posDelim;
                else
                    endPosition = posDelim < posNewline ? posDelim : posNewline;
                newPosition = endPosition + 1;
                break;
            case ONE_QUOTE:
                fPosition += 1;
                int posQuoteDelim = fBuffer.indexOf(fQuote + fDelim, fPosition);
                int posQuoteNewline = fBuffer.indexOf(fQuote + fNewline, fPosition);
                if (posQuoteDelim == -1)
                    if (posQuoteNewline == -1)
                        endPosition = fBuffer.length() - 1;
                    else
                        endPosition = posQuoteNewline;
                else if (posQuoteNewline == -1)
                    endPosition = posQuoteDelim;
                else
                    endPosition = posQuoteDelim < posQuoteNewline ? posQuoteDelim : posQuoteNewline;
                newPosition = endPosition + 2;
                break;
            case TWO_QUOTE:
                fPosition += 2;
                int posTwoQuote = fBuffer.indexOf(fQuote + fQuote, fPosition);
                if (posTwoQuote == -1)
                    endPosition = fBuffer.length() - 1;
                else
                    endPosition = posTwoQuote;
                newPosition = endPosition + 2;
            default:
                throw new AssertionError();
        }
        ... // issue-causing examples:
        // 1165315126203785338,2023-10-21 15:46:16.595000+00:00,"""Vintergatan"", ISBN 9789189059870",
        /* 1165680212659404851,2023-10-22 15:56:59.989000+00:00,"```Den drömmen, som aldrig besannats,
som dröm var den vacker att få,
för den, som ur Eden förbannats
är Eden ett Eden ändå.``` – Gustaf Fröding", */
        String output = fBuffer.substring(fPosition, endPosition);
        fPosition = newPosition;
        return output;
    }

    public String nextLine() {
        StringBuilder output = new StringBuilder();
        String[] tokens = nextLineTokens();
        for (String s : tokens) {
            output.append(s);
        }
        return output.toString();
    }

    public String[] nextLineTokens() {
        ArrayList<String> output = new ArrayList<>();
        do {
            output.add(next());
        } while (!fBuffer.substring(fPosition - 1, fPosition).equals(fNewline));
        return output.toArray(new String[output.size()]);
    }

    @Override
    public boolean hasNext() {
        return fPosition < fBuffer.length();
    }

    public void expectHasNext() {
        if (!hasNext())
            throw new IllegalStateException(
                    "No more data (already read " + fPosition + " out of " + fBuffer.length() + "characters");
    }

    @Override
    public void close() {
        fBuffer = null;
    }

    private static enum TokenType {
        NO_QUOTE, ONE_QUOTE, TWO_QUOTE;
    }
}
