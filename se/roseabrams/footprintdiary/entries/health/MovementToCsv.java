package se.roseabrams.footprintdiary.entries.health;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.Util;

public class MovementToCsv {
    public static void main(String[] args) throws IOException {
        StringBuilder output = new StringBuilder(1000000);
        Document d = Util.readXmlFile(
                new File("D:\\Dropbox\\Privat\\postGym program\\footprint diary\\data\\apple\\health export.xml"));
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

                output.append(type).append(Util.DELIM).append(sourceName).append(Util.DELIM).append(sourceVersion)
                        .append(Util.DELIM).append(device).append(Util.DELIM).append(unit).append(Util.DELIM)
                        .append(value).append(Util.DELIM).append(creationDate).append(Util.DELIM).append(startDate)
                        .append(Util.DELIM).append(endDate).append(Util.NEWLINE);
            }
        }
        Util.writeFile(
                (new File("D:\\Dropbox\\Privat\\postGym program\\footprint diary\\data\\apple\\health export.csv")),
                output.toString());
    }
}
