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
    private static final String INTR_QUOTE = "\"\"";

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
        int startPosition;
        int endPosition;
        int newPosition;
        String nextChar = Character.toString(fBuffer.charAt(fPosition));
        if (nextChar.equals(quote)) {
            String nextChar2 = Character.toString(fBuffer.charAt(fPosition + 1));
            String nextChar3 = Character.toString(fBuffer.charAt(fPosition + 2));
            if (nextChar2.equals(quote) && !nextChar3.equals(quote)) {
                // two quotes, multiline
                startPosition = fPosition + 2;
                endPosition = closeToken(quote + quote, null);
                newPosition = endPosition + 2;
            } else {
                // one quote, close before delim
                startPosition = fPosition + 1;
                endPosition = closeToken(quote + delim, quote + newline);
                newPosition = endPosition + 2;
            }
        } else {
            // no quote, simple delim
            startPosition = fPosition;
            endPosition = closeToken(delim, newline);
            newPosition = endPosition + 1;
        }
        String output = fBuffer.substring(startPosition, endPosition);
        fPosition = newPosition;
        if (output.contains(INTR_QUOTE)) 
            output = output.replace(INTR_QUOTE, "\"");
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
        int posInternalQuoteOpen = fBuffer.indexOf(INTR_QUOTE, startPos);
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
            endPosition = closeToken(closer1, closer2, posBackticksClose + BACKTICKS.length());
        }
        if (posInternalQuoteOpen != -1 && posInternalQuoteOpen < endPosition) {
            int posInternalQuoteClose = closeToken(INTR_QUOTE, null, posInternalQuoteOpen + INTR_QUOTE.length());
            endPosition = closeToken(closer1, closer2, posInternalQuoteClose + INTR_QUOTE.length());
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
