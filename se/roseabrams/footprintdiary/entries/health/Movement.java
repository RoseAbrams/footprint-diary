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

public class Movement extends HealthData {

    @Override
    public String getStringSummary() {
    }

    public static Movement[] createDaysFromXml(File exportFile)
            throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document d = db.parse(exportFile);
        NodeList allNodes = d.getDocumentElement().getChildNodes();
        for (int i = 0; i < allNodes.getLength(); i++) {
            Node node = allNodes.item(i);
            NamedNodeMap attributes = node.getAttributes();
            if (node.getNodeName() == "Record") {
                String type = attributes.getNamedItem("type").toString();
                String sourceName = attributes.getNamedItem("sourceName").toString();
                String sourceVersion = attributes.getNamedItem("sourceVersion").toString();
                String device = attributes.getNamedItem("device").toString();
                String unit = attributes.getNamedItem("unit").toString();
                String value = attributes.getNamedItem("value").toString();
                String creationDateS = attributes.getNamedItem("creationDate").toString();
                String startDateS = attributes.getNamedItem("startDate").toString();
                String endDateS = attributes.getNamedItem("endDate").toString();
                DiaryDateTime creationDate = new DiaryDateTime(creationDateS.substring(0, 20));
                DiaryDateTime startDate = new DiaryDateTime(startDateS.substring(0, 20));
                DiaryDateTime endDate = new DiaryDateTime(endDateS.substring(0, 20));
                // ...
            }
        }
    }
}
