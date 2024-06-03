package se.roseabrams.footprintdiary.entries.health;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.Util;

public abstract class HealthData extends DiaryEntry {

    public HealthData(DiaryDate date) {
        super(DiaryEntryCategory.ACTIVITY, date);
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

        @Override
        public String toString() {
            return fullName();
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

    public static HealthData[] createFromXml(File exportFile) throws IOException {
        ArrayList<Activity> output = new ArrayList<>(800000);
        Document d = Util.readXmlFile(exportFile);
        NodeList allNodes = d.getDocumentElement().getChildNodes();

        for (int i = 0; i < allNodes.getLength(); i++) {
            Node node = allNodes.item(i);
            NamedNodeMap attr = node.getAttributes();
            if (node.getNodeName() == "Record") {
                String typeS = attr.getNamedItem("type").getTextContent();
                Type type = null;
                for (Type t : Type.values()) {
                    if (typeS.equals(t.fullName())) {
                        type = t;
                        break;
                    }
                }
                assert type != null;
                String sourceName = attr.getNamedItem("sourceName").getTextContent();
                Node sourceVersionN = attr.getNamedItem("sourceVersion");
                String sourceVersion = null;
                if (sourceVersionN != null) {
                    sourceVersion = sourceVersionN.getTextContent();
                }
                Node deviceN = attr.getNamedItem("device");
                String device = null;
                if (deviceN != null) {
                    device = deviceN.getTextContent();
                }
                Node unitN = attr.getNamedItem("unit");
                String unit = null;
                if (unitN != null) {
                    unit = unitN.getTextContent();
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

                switch (type) {
                    case DISTANCE_MOVED:
                        output.add(new Activity());
                        break;
                    case STEP_COUNT:
                    output.add(new Activity());
                        break;
                    case ENERGY_BURNED_ACTIVE:
                    output.add(new Activity());
                        break;
                    default:
                        System.out.println("Skipping unimplemented HealthData.Type: " + type);
                        break;
                }
                // ...
            }
        }
    }
}
