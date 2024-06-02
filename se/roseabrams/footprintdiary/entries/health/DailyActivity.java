package se.roseabrams.footprintdiary.entries.health;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.interfaces.CustomCounted;

public class DailyActivity extends HealthData implements CustomCounted {

    // TODO if all values are zero because of no data, an object should not exist
    public final int STEPS_TAKEN;
    public final float KILOMETERS_WALKED;
    public final float CALORIES_BURNED;

    @Override
    public String getStringSummary() {
        return STEPS_TAKEN + " steps, " + KILOMETERS_WALKED + " km of distance, " + CALORIES_BURNED + " kcal expended";
    }

    @Override
    public int getCustomCount() {
        return Math.round(KILOMETERS_WALKED);
    }

    public static DailyActivity[] createDays(HealthData[] healthData) throws IOException {
    }
}
