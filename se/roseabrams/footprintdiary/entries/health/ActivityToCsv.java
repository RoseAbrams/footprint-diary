package se.roseabrams.footprintdiary.entries.health;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.Util;

public class ActivityToCsv {/*
    public static void main(String[] args) throws IOException {
        StringBuilder output = new StringBuilder(1000000);
        Document d = Util.readXmlFile(
                new File("D:\\Dropbox\\Privat\\postGym program\\footprint diary\\data\\apple\\health export.xml"));
        NodeList allNodes = d.getDocumentElement().getChildNodes();
        for (int i = 0; i < allNodes.getLength(); i++) {
            Node node = allNodes.item(i);
            NamedNodeMap attr = node.getAttributes();
            if (node.getNodeName() == "Record") {
                String type = attr.getNamedItem("type").getTextContent();
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
                String value = attr.getNamedItem("value").getTextContent();
                // could be "HKCategoryValueNotApplicable", "HKCategoryValueSleepAnalysisInBed"
                String creationDateS = attr.getNamedItem("creationDate").getTextContent();
                String startDateS = attr.getNamedItem("startDate").getTextContent();
                String endDateS = attr.getNamedItem("endDate").getTextContent();
                DiaryDateTime creationDate = new DiaryDateTime(creationDateS.substring(0, 20));
                DiaryDateTime startDate = new DiaryDateTime(startDateS.substring(0, 20));
                DiaryDateTime endDate = new DiaryDateTime(endDateS.substring(0, 20));

                output.append(type).append(Util.DELIM).append(sourceName).append(Util.DELIM).append(sourceVersion)
                        .append(Util.DELIM).append('\"').append(device).append('\"').append(Util.DELIM).append(unit).append(Util.DELIM)
                        .append(value).append(Util.DELIM).append(creationDate).append(Util.DELIM).append(startDate)
                        .append(Util.DELIM).append(endDate).append(Util.NEWLINE);
            }
        }
        Util.writeFile(
                (new File("D:\\Dropbox\\Privat\\postGym program\\footprint diary\\data\\apple\\health export.csv")),
                output.toString());
    }*/
}
