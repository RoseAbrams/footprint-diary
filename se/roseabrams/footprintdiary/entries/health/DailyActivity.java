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

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.Util;

public class DailyActivity extends HealthData {

    // TODO if all values are zero because of no data, an object should not exist
    public final String STEPS_TAKEN;
    public final String DISTANCE_WALKED;
    public final String CALORIES_BURNED;

    @Override
    public String getStringSummary() {
    }

    public static DailyActivity[] createDays(HealthData[] healthData) throws IOException {
    }
}
