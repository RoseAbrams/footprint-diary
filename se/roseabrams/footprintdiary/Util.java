package se.roseabrams.footprintdiary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Util {
    private static DocumentBuilderFactory dbf = null;
    private static DocumentBuilder db = null;

    @Deprecated // figure out something simpler and more modern for simple text ingest
    public static String readFile(File f, int maxLength) throws IOException {
        char[] buffer = new char[maxLength];
        new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8")).read(buffer);
        return new String(buffer);
    }

    @Deprecated // figure out something simpler and more modern for simple text ingest
    public static JSONObject readJsonFile(File f, int maxLength) throws IOException {
        return new JSONObject(readFile(f, maxLength));
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

    public static void writeFile(File f, String data) {
        ...
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
