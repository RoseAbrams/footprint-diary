package se.roseabrams.footprintdiary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Util {

    private static DocumentBuilderFactory dbf = null;
    private static DocumentBuilder db = null;
    public static final Charset CHARSET = StandardCharsets.UTF_8;
    //public static final char DELIM = ',';
    //public static final char NEWLINE = '\n';

    private Util() {
    }

    public static String readFile(File f) throws IOException {
        return Files.readString(f.toPath(), CHARSET);
    }

    public static JSONObject readJsonObjectFile(File f) throws IOException {
        return new JSONObject(readFile(f));
    }

    public static JSONArray readJsonArrayFile(File f) throws IOException {
        return new JSONArray(readFile(f));
    }

    public static List<String> readFileLines(File f) throws IOException {
        return Files.readAllLines(f.toPath(), CHARSET);
    }

    public static Document readXmlFile(File f) throws IOException {
        try {
            if (db == null) {
                dbf = DocumentBuilderFactory.newInstance();
                db = dbf.newDocumentBuilder();
            }
            return db.parse(f);
        } catch (SAXException e) {
            throw new IOException(e);
        } catch (ParserConfigurationException e) {
            throw new Error(e);
        }
    }

    public static void writeFile(File f, String data) throws IOException {
        Files.deleteIfExists(f.toPath());
        Files.writeString(f.toPath(), data, CHARSET, StandardOpenOption.CREATE_NEW);
    }

    public static <E extends Enum<E>> E findJsonInEnum(String input, E[] enumValues) {
        if (input == null || input.isEmpty())
            return null;
        String inputM = input.toUpperCase().replace('-', '_');
        for (E v : enumValues) {
            if (inputM.equals(v.name())) {
                return v;
            }
        }
        throw new IllegalArgumentException("No value " + input + " in enum " + enumValues.getClass());
    }

    public static void serialize(Object o, File outputFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(o);
        oos.flush();
        fos.flush();
        oos.close();
        fos.close();
    }

    public static Object deserialize(File inputFile) throws IOException {
        try {
            FileInputStream fis = new FileInputStream(inputFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object output = ois.readObject();
            ois.close();
            fis.close();
            return output;
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    public static String jsonStringNullsafe(JSONObject o, String key) {
        if (o.isNull(key))
            return null;
        else
            return o.getString(key);
    }

    public static boolean jsonBooleanNullsafe(JSONObject o, String key) {
        if (o.isNull(key))
            return false;
        else
            return o.getBoolean(key);
    }

    public static String parseHtml(String input) {
        String output = input.replace("+", " ");
        while (output.contains("%")) {
            String charRef = output.substring(output.indexOf("%") + 1, output.indexOf("%") + 3);
            int charResultI = Integer.parseInt(charRef, 16);
            char charResult = (char) charResultI;
            output = output.replace("%" + charRef, String.valueOf(charResult));
        }
        return output;
    }

    // manual converter: https://ldu2.github.io/rfc2047/
    // untested written by MSCopilot because i'm tired
    public static String decodeRfc2047(String encoded) {
        Pattern pattern = Pattern.compile("=\\?([^?]+)\\?([BQbq])\\?([^?]+)\\?=");
        Matcher matcher = pattern.matcher(encoded);
        StringBuffer decoded = new StringBuffer();

        while (matcher.find()) {
            String charsetS = matcher.group(1);
            Charset charset = Charset.forName(charsetS);
            String encoding = matcher.group(2);
            String encodedText = matcher.group(3);
            String decodedText = "";

            if (encoding.equalsIgnoreCase("B")) {
                decodedText = new String(Base64.getDecoder().decode(encodedText), charset);
            } else if (encoding.equalsIgnoreCase("Q")) {
                StringBuilder decodedQ = new StringBuilder();
                byte[] bytes = new byte[encodedText.length()];
                int byteIndex = 0;

                for (int i = 0; i < encodedText.length(); i++) {
                    char c = encodedText.charAt(i);
                    if (c == '_') {
                        bytes[byteIndex++] = (byte) ' ';
                    } else if (c == '=') {
                        int hex1 = Character.digit(encodedText.charAt(i + 1), 16);
                        int hex2 = Character.digit(encodedText.charAt(i + 2), 16);
                        bytes[byteIndex++] = (byte) ((hex1 << 4) + hex2);
                        i += 2;
                    } else {
                        bytes[byteIndex++] = (byte) c;
                    }
                }

                decodedQ.append(new String(bytes, 0, byteIndex, charset));

                decodedText = decodedQ.toString();
            } else {
                throw new AssertionError();
            }

            matcher.appendReplacement(decoded, Matcher.quoteReplacement(decodedText));
        }
        matcher.appendTail(decoded);
        return decoded.toString();
    }
}
