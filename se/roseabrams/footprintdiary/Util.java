package se.roseabrams.footprintdiary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONObject;

public class Util {
    @Deprecated // figure out something simpler and more modern for simple text ingest
    public static String loadFile(File f, int maxLength) throws IOException {
        char[] buffer = new char[maxLength];
        new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8")).read(buffer);
        return new String(buffer);
    }

    @Deprecated // figure out something simpler and more modern for simple text ingest
    public static JSONObject loadJsonFile(File f, int maxLength) throws IOException {
        return new JSONObject(loadFile(f, maxLength));
    }

    public static <E extends Enum<E>> E findJsonInEnum(String input, E[] enumValues) {
        for (E v : enumValues) {
            if (input.equals(v.name().toUpperCase().replace('_', '-'))) {
                return v;
            }
        }
        throw new IllegalArgumentException("No value " + input + " in enum " + enumValues.getClass());
    }
}
