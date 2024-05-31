package se.roseabrams.footprintdiary.entries.wikimedia;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.entries.steam.SteamStore;
import se.roseabrams.footprintdiary.entries.whatsapp.WhatsAppMessage;
import se.roseabrams.footprintdiary.interfaces.RemoteResource;

public class WikimediaEdit extends DiaryEntry implements RemoteResource {
    /*
     * https://meta.wikimedia.org/wiki/Special:CentralAuth/Rose_Abrams
     * 9 sites with edits
     */
    public static WikimediaEdit[] createFromHtml(File contribsFile, String site) throws IOException {
        ArrayList<WhatsAppMessage> output = new ArrayList<>();
        Document d = Util.readXmlFile(contribsFile);
        NodeList contribs = d.getDocumentElement().getChildNodes();
        String baseURL = "https://" + site + ".org";
        for (int i = 0; i < contribs.getLength(); i++) {
            if (contribs.item(i).getNodeName().equals("ul")) {
                Node contrib = contribs.item(i).getFirstChild();
                contrib. // why can't i just query it??
            }
        }

        return output.toArray(new WikimediaEdit[output.size()]);
    }
}
