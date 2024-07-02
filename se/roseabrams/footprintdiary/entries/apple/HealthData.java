package se.roseabrams.footprintdiary.entries.apple;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.Util;

public class HealthData extends DiaryEntry {

    public final Type TYPE;
    public final String SOURCE_NAME;
    public final String SOURCE_VERSION;
    public final String DEVICE;
    public final float VALUE;
    public final DiaryDateTime CREATION_DATE;
    public final DiaryDateTime START_DATE;
    public final DiaryDateTime END_DATE;

    public HealthData(Type type, String sourceName, String sourceVersion, String device, String unit, float value,
            DiaryDateTime creationDate, DiaryDateTime startDate, DiaryDateTime endDate) {
        super(DiaryEntryCategory.HEALTH, startDate.reduce());

        assert (type.unit() == null && unit == null) || type.unit().equals(unit);

        TYPE = type;
        SOURCE_NAME = sourceName;
        SOURCE_VERSION = sourceVersion;
        DEVICE = device;
        VALUE = value;
        CREATION_DATE = creationDate;
        START_DATE = startDate;
        END_DATE = endDate;
    }

    @Override
    public String getStringSummary() {
        return TYPE + ": " + VALUE;
    }

    public static enum Type {
        MINDFUL_SESSION, SEXUAL_ACTIVITY, SLEEP_TIME, SLEEP_GOAL, ENERGY_BURNED_ACTIVE, WALKING_STEADINESS,
        ENERGY_BURNED_BASAL, DISTANCE_MOVED, FLIGHTS_CLIMBED, HEADPHONE_AUDIO, HEIGHT, STEP_COUNT, WALKING_ASYMMETRY,
        DOUBLE_SUPPORT, WALKING_SPEED, STEP_LENGTH;

        public String fullName() {
            return FULL_NAME[ordinal()];
        }

        public String unit() {
            return UNIT[ordinal()];
        }

        private static final String[] FULL_NAME = { "HKCategoryTypeIdentifierMindfulSession",
                "HKCategoryTypeIdentifierSexualActivity",
                "HKCategoryTypeIdentifierSleepAnalysis",
                "HKDataTypeSleepDurationGoal",
                "HKQuantityTypeIdentifierActiveEnergyBurned",
                "HKQuantityTypeIdentifierAppleWalkingSteadiness",
                "HKQuantityTypeIdentifierBasalEnergyBurned",
                "HKQuantityTypeIdentifierDistanceWalkingRunning",
                "HKQuantityTypeIdentifierFlightsClimbed",
                "HKQuantityTypeIdentifierHeadphoneAudioExposure",
                "HKQuantityTypeIdentifierHeight",
                "HKQuantityTypeIdentifierStepCount",
                "HKQuantityTypeIdentifierWalkingAsymmetryPercentage",
                "HKQuantityTypeIdentifierWalkingDoubleSupportPercentage",
                "HKQuantityTypeIdentifierWalkingSpeed",
                "HKQuantityTypeIdentifierWalkingStepLength" };

        private static final String[] UNIT = { null, null, null, "hr", "kcal", "%", "kcal", "km", "count",
                "dBASPL", "cm", "count", "%", "%", "km/hr", "cm" };
    }

    public static HealthData[] createAllFromXml(File exportFile) throws IOException {
        ArrayList<HealthData> output = new ArrayList<>(800000);
        Document d = Util.readXmlFile(exportFile);
        NodeList allNodes = d.getDocumentElement().getChildNodes();

        for (int i = 0; i < allNodes.getLength(); i++) {
            Node node = allNodes.item(i);
            if (node.getNodeName() == "Record") {
                output.add(createFromXml(node));
            }
        }
        return output.toArray(new HealthData[output.size()]);
    }

    public static HealthData createFromXml(Node node) {
        NamedNodeMap attr = node.getAttributes();
        String typeS = attr.getNamedItem("type").getTextContent();
        Type type = null;
        for (Type t : Type.values()) {
            if (typeS.equals(t.fullName())) {
                type = t;
                break;
            }
        }
        assert type != null;
        String sourceName = attr.getNamedItem("sourceName").getTextContent().intern();
        Node sourceVersionN = attr.getNamedItem("sourceVersion");
        String sourceVersion = null;
        if (sourceVersionN != null) {
            sourceVersion = sourceVersionN.getTextContent().intern();
        }
        Node deviceN = attr.getNamedItem("device");
        String device = null;
        if (deviceN != null) {
            device = deviceN.getTextContent().intern();
        }
        Node unitN = attr.getNamedItem("unit");
        String unit = null;
        if (unitN != null) {
            unit = unitN.getTextContent().intern();
        }
        String valueS = attr.getNamedItem("value").getTextContent();
        float value;
        if (valueS.startsWith("HKCategory")) {
            value = Float.NaN;
        } else {
            value = Float.parseFloat(valueS);
        }
        String creationDateS = attr.getNamedItem("creationDate").getTextContent();
        String startDateS = attr.getNamedItem("startDate").getTextContent();
        String endDateS = attr.getNamedItem("endDate").getTextContent();
        DiaryDateTime creationDate = new DiaryDateTime(creationDateS.substring(0, 20));
        DiaryDateTime startDate = new DiaryDateTime(startDateS.substring(0, 20));
        DiaryDateTime endDate = new DiaryDateTime(endDateS.substring(0, 20));

        return new HealthData(type, sourceName, sourceVersion, device, unit, value, creationDate, startDate, endDate);
    }
}
