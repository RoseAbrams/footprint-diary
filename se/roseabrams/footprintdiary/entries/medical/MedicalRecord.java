package se.roseabrams.footprintdiary.entries.medical;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.common.TitledString;

public class MedicalRecord extends DiaryEntry {

    public final Type TYPE;
    public final String AUTHOR;
    public final String PROVIDER;
    public final TitledString[] TEXT;

    public MedicalRecord(DiaryDate dd, Type type, String author, String provider, TitledString[] text) {
        super(DiaryEntryCategory.MEDICAL, dd);
        TYPE = type;
        AUTHOR = author;
        PROVIDER = provider;
        TEXT = text;
    }

    @Override
    public String getStringSummary() {
        return AUTHOR + " @ " + PROVIDER;
    }

    public static enum Type {
    }

    @SuppressWarnings("unused")
    public static List<MedicalRecord> createFromHtml(File recordsFile) throws IOException {
        ArrayList<MedicalRecord> output = new ArrayList<>(500);
        Document d = Jsoup.parse(recordsFile);
        for (Element recordE : d.select("li.ic-block-list__item")) {
            Element headingE = recordE.selectFirst("button.nc-journal-overview--expandable article header");
            Element sectionE = recordE.selectFirst("div.nc-detail--loader > section.ic-section");
            Element detailsE = sectionE.selectFirst("div.information-details");

            String categoryTypeS = headingE.selectFirst("span.iu-tt-uc").text();
            String headingTypeS = headingE.selectFirst("h3 > a").text();

            Element detailsHeadingE = detailsE.selectFirst("div.nc-print > div.nc-print-heading");
            String detailsTypeS = detailsHeadingE.selectFirst("h3.nc-heading__information-type").text();
            String timestampS = detailsHeadingE.selectFirst("span.nc-document-timestamp").text();

            Elements detailsRows = detailsE.select("div.information-details__row");
            assert detailsRows.size() == 1 && detailsRows.get(0).text().startsWith("Antecknad av");
            Element originE = detailsRows.get(0).selectFirst("div.detail-description");
            String author = originE.ownText();
            String provider = originE.selectFirst("span.nu-display-block").text();

            Elements payloadFieldsE = sectionE.select("div.docbook");
            assert payloadFieldsE.size() == 2;
            String payloadTypeS = payloadFieldsE.get(0).select("div.information-details__row > h4.detail-title").text();
            Elements payloadE = payloadFieldsE.get(1).select("div.information-details__row");
            TitledString[] payloadText = new TitledString[payloadE.size()];
            for (int i = 0; i < payloadE.size(); i++) {
                String title = payloadE.get(i).selectFirst("h4.detail-title").text();
                String body = payloadE.get(i).selectFirst("div.detail-description").text();
                payloadText[i] = new TitledString(title, body.isBlank() ? null : body);
            }

            DiaryDateTime timestampTemp = new DiaryDateTime(timestampS);
            DiaryDate timestamp;
            if (timestampTemp.shouldReduce())
                timestamp = timestampTemp.reduce();
            else
                timestamp = timestampTemp;

            String bestTypeS = payloadTypeS; // TODO find best field for this
            Type type = Type.valueOf(bestTypeS.toUpperCase());

            MedicalRecord m = new MedicalRecord(timestamp, type, author, provider, payloadText);
            output.add(m);
        }
        return output;
    }
}
