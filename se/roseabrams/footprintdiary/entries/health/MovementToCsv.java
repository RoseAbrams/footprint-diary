package se.roseabrams.footprintdiary.entries.health;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.Util;

public class MovementToCsv {
    public static void main(String[] args) {
        StringBuilder output = new StringBuilder(1000000);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document d = db.parse(exportFile);
        NodeList allNodes = d.getDocumentElement().getChildNodes();
        for (int i = 0; i < allNodes.getLength(); i++) {
            Node node = allNodes.item(i);
            NamedNodeMap attr = node.getAttributes();
            if (node.getNodeName() == "Record") {
                String type = attr.getNamedItem("type").toString();
                String sourceName = attr.getNamedItem("sourceName").toString();
                String sourceVersion = attr.getNamedItem("sourceVersion").toString();
                String device = attr.getNamedItem("device").toString();
                String unit = attr.getNamedItem("unit").toString();
                String value = attr.getNamedItem("value").toString();
                String creationDateS = attr.getNamedItem("creationDate").toString();
                String startDateS = attr.getNamedItem("startDate").toString();
                String endDateS = attr.getNamedItem("endDate").toString();
                DiaryDateTime creationDate = new DiaryDateTime(creationDateS.substring(0, 20));
                DiaryDateTime startDate = new DiaryDateTime(startDateS.substring(0, 20));
                DiaryDateTime endDate = new DiaryDateTime(endDateS.substring(0, 20));

                output.append(type).append(',').append(sourceName).append(',').append(sourceVersion).append(',')
                        .append(device).append(',').append(unit).append(',').append(value).append(',')
                        .append(creationDate).append(',').append(startDate).append(',').append(endDate).append('\n');
            }
        }
        Util.writeFile(new File(...), output.toString());
    }
}
