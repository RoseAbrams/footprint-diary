package se.roseabrams.footprintdiary;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

    public static JSONObject readJsonFile(File f) throws IOException {
        return new JSONObject(readFile(f));
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
        String inputM = input.toUpperCase().replace('-', '_');
        for (E v : enumValues) {
            if (inputM.equals(v.name())) {
                return v;
            }
        }
        throw new IllegalArgumentException("No value " + input + " in enum " + enumValues.getClass());
    }
}
