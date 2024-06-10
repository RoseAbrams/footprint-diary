package se.roseabrams.footprintdiary;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class CSVParser implements Iterator<String>, Closeable {

    private String fBuffer;
    private int fPosition = 0;
    private final String delim;
    private final String newline = "\n";
    private final String quote = "\"";
    private static final String DELIM = ",";
    private static final String BACKTICKS = "```";

    public CSVParser(File input) throws IOException {
        this(input, DELIM);
    }

    public CSVParser(String input) {
        this(input, DELIM);
    }

    public CSVParser(File input, String delim) throws IOException {
        this(Util.readFile(input), delim);
    }

    public CSVParser(String input, String delim) {
        fBuffer = input;
        this.delim = delim;
    }

    @Override
    public String next() {
        expectHasNext();
        String nextChar = Character.toString(fBuffer.charAt(fPosition));
        int endPosition;
        int newPosition;
        if (nextChar.equals(quote)) {
            String nextChar2 = Character.toString(fBuffer.charAt(fPosition + 1));
            String nextChar3 = Character.toString(fBuffer.charAt(fPosition + 2));
            if (nextChar2.equals(quote) && !nextChar3.equals(quote)) {
                // two quotes, multiline
                endPosition = closeToken(quote + quote, null);
                /*fPosition += 2;
                int posTwoQuote = fBuffer.indexOf(quote + quote, fPosition);
                if (posTwoQuote == -1)
                    endPosition = fBuffer.length() - 1;
                else
                    endPosition = posTwoQuote;*/
                newPosition = endPosition + 2;
            } else {
                // one quote, close before delim
                endPosition = closeToken(quote + delim, quote + newline);
                /*fPosition += 1;
                int posQuoteDelim = fBuffer.indexOf(quote + delim, fPosition);
                int posQuoteNewline = fBuffer.indexOf(quote + newline, fPosition);
                if (posQuoteDelim == -1)
                    if (posQuoteNewline == -1)
                        endPosition = fBuffer.length() - 1;
                    else
                        endPosition = posQuoteNewline;
                else if (posQuoteNewline == -1)
                    endPosition = posQuoteDelim;
                else
                    endPosition = posQuoteDelim < posQuoteNewline ? posQuoteDelim : posQuoteNewline;*/
                newPosition = endPosition + 2;
            }
        } else {
            // no quote, simple delim
            endPosition = closeToken(delim, newline);
            /*int posDelim = fBuffer.indexOf(delim, fPosition);
            int posNewline = fBuffer.indexOf(newline, fPosition);
            if (posDelim == -1)
                if (posNewline == -1)
                    endPosition = fBuffer.length() - 1;
                else
                    endPosition = posNewline;
            else if (posNewline == -1)
                endPosition = posDelim;
            else
                endPosition = posDelim < posNewline ? posDelim : posNewline;*/
            newPosition = endPosition + 1;
        }
        String output = fBuffer.substring(fPosition, endPosition);
        fPosition = newPosition;
        return output;
    }

    private int closeToken(String closer1, String closer2) {
        return closeToken(closer1, closer2, fPosition);
    }

    private int closeToken(String closer1, String closer2, int startPos) {
        assert closer1 != null && !closer1.isBlank();
        int endPosition;
        int pos1 = fBuffer.indexOf(closer1, startPos);
        int pos2 = closer2 != null ? fBuffer.indexOf(closer2, startPos) : -1;
        int posBackticksOpen = fBuffer.indexOf(BACKTICKS, startPos);
        if (pos1 == -1)
            if (pos2 == -1)
                endPosition = fBuffer.length() - 1;
            else
                endPosition = pos2;
        else if (pos2 == -1)
            endPosition = pos1;
        else
            endPosition = pos1 < pos2 ? pos1 : pos2;
        if (posBackticksOpen != -1 && posBackticksOpen < endPosition) {
            int posBackticksClose = closeToken(BACKTICKS, null, posBackticksOpen + BACKTICKS.length());
            endPosition = closeToken(closer1, closer2, posBackticksClose);
        }
        return endPosition;
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
        } while (!fBuffer.substring(fPosition - 1, fPosition).equals(newline));
        return output.toArray(new String[output.size()]);
    }

    @Override
    public boolean hasNext() {
        return fPosition < fBuffer.length();
    }

    public void expectHasNext() {
        if (!hasNext())
            throw new IllegalStateException("No more data (already read "
                    + fPosition + " out of " + fBuffer.length() + "characters)");
    }

    @Override
    public void close() {
        fBuffer = null;
    }
}
