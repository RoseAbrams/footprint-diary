package ja1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONObject;

public class Util {
    public static String loadFile(File f, int maxLength) throws IOException {
        char[] buffer = new char[maxLength];
        new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8")).read(buffer);
        return new String(buffer);
    }

    public static JSONObject loadJsonFile(File f, int maxLength) throws IOException {
        return new JSONObject(loadFile(f, maxLength));
    }
}
