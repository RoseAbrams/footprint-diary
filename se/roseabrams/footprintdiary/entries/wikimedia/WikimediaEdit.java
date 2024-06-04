package se.roseabrams.footprintdiary.entries.wikimedia;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.interfaces.RemoteResource;

public class WikimediaEdit extends DiaryEntry implements RemoteResource {

    public final String SITE;
    public final String PAGE_TITLE;
    public final int EDIT_SIZE;
    public final int OLDID;
    public final String EDIT_SUMMARY;
    public final String[] TAGS;

    public WikimediaEdit(DiaryDateTime dd, String site, String pageTitle, int editSize, int oldid, String editSummary,
            String[] tags) {
        super(DiaryEntryCategory.WIKIMEDIA_EDIT, dd);
        SITE = site;
        PAGE_TITLE = pageTitle.intern();
        EDIT_SIZE = editSize;
        OLDID = oldid;
        EDIT_SUMMARY = editSummary;
        TAGS = tags;
        try {
            getSiteUrl();
            getArticleUrl();
            getArticlePermaUrl();
            getDiffUrl();
        } catch (AssertionError | RuntimeException e) {
            throw new IllegalArgumentException("Arguments would cause invalid URLs.", e);
        }
    }

    public URL getSiteUrl() {
        try {
            return URI.create("https://" + SITE + ".org/").toURL();
        } catch (MalformedURLException e) {
            throw new AssertionError(e);
        }
    }

    public URL getArticleUrl() {
        try {
            return URI.create(getSiteUrl().getPath() + "wiki/" + PAGE_TITLE).toURL();
        } catch (MalformedURLException e) {
            throw new AssertionError(e);
        }
    }

    public URL getArticlePermaUrl() {
        try {
            return URI.create(getSiteUrl().getPath() + "w/index.php?title=" + PAGE_TITLE + "&oldid=" + OLDID).toURL();
        } catch (MalformedURLException e) {
            throw new AssertionError(e);
        }
    }

    public URL getDiffUrl() {
        try {
            StringBuilder s = new StringBuilder(getArticlePermaUrl().getPath());
            return URI.create(s.insert(s.lastIndexOf("&"), "&diff=prev").toString()).toURL();
        } catch (MalformedURLException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public String getStringSummary() {
        return PAGE_TITLE + " (" + (EDIT_SUMMARY.length() > 10 ? EDIT_SUMMARY.substring(0, 10) + "..." : EDIT_SUMMARY)
                + ")";
    }

    @Override
    public String getPathToResource() {
        return getDiffUrl().getPath();
    }

    @Override
    public URL getUrlOfResource() {
        return getDiffUrl();
    }

    public static WikimediaEdit[] createFromWebsites() throws IOException {
        ArrayList<WikimediaEdit> output = new ArrayList<>(7000);
        for (String wiki : PersonalConstants.WIKIS_WITH_EDITS) {
            output.addAll(createFromWebsite(wiki));
        }
        return output.toArray(new WikimediaEdit[output.size()]);
    }

    public static ArrayList<WikimediaEdit> createFromWebsite(String site) throws IOException {
        Connection c = Jsoup.connect("https://" + site + ".org/w/index.php?title="
                + "Special:Contributions/Rose_Abrams&target=Rose+Abrams&offset=&limit=10000");
        Document d = c.get();

        ArrayList<WikimediaEdit> output = new ArrayList<>(5000);
        Elements edits = d.body().select("div#mw-content-text > section.mw-pager-body > ul.mw-contributions-list");
        for (Element edit : edits) {
            int oldid = Integer.parseInt(edit.select("li").first().attr("data-mw-revid"));
            int editSize = Integer.parseInt(edit.select("span.mw-diff-bytes").first().text());
            String dateS = edit.select("a.mw-changeslist-date").first().text();
            DiaryDateTime date = new DiaryDateTime(Short.parseShort(dateS.substring(14, 18)),
                    DiaryDate.parseMonthName(dateS.substring(11, 13)), Byte.parseByte(dateS.substring(7, 9)),
                    Byte.parseByte(dateS.substring(0, 2)), Byte.parseByte(dateS.substring(3, 5)), (byte) 0);
            String pageTitle = edit.select("a.mw-contributions-title").first().text();
            String editSummary = edit.select("span.comment").first().text();
            Elements tagsE = edit.select("span.mw-tag-markers > span.mw-tag-marker");
            String[] tags = new String[tagsE.size()];
            for (int i = 0; i < tagsE.size(); i++) {
                tags[i] = tagsE.get(i).text().intern();
            }

            output.add(new WikimediaEdit(date, site, pageTitle, editSize, oldid, editSummary, tags));
        }
        return output;
    }
    /*
     * public static WikimediaEdit[] createFromHtml(File contribsFile, String site)
     * throws IOException {
     * ArrayList<WikimediaEdit> output = new ArrayList<>();
     * Document d = Util.readXmlFile(contribsFile);
     * NodeList contribs = d.getDocumentElement().getChildNodes();
     * String baseURL = "https://" + site + ".org";
     * for (int i = 0; i < contribs.getLength(); i++) {
     * if (contribs.item(i).getNodeName().equals("ul")) {
     * Node contrib = contribs.item(i).getFirstChild();
     * contrib. // why can't i just query it??
     * }
     * }
     * 
     * return output.toArray(new WikimediaEdit[output.size()]);
     * }
     */
}
