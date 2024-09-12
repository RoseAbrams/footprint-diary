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
    public final TitledString[] TEXT; // sometimes length 0 because data has no payload

    public MedicalRecord(DiaryDate dd, Type type, String author, String provider, TitledString[] text) {
        super(DiaryEntryCategory.MEDICAL, dd);
        TYPE = type;
        AUTHOR = author;
        PROVIDER = provider.intern();
        TEXT = text;
    }

    @Override
    public String getStringSummary() {
        return AUTHOR + " @ " + PROVIDER;
    }

    public static enum Type {
        GENERIC_NOTE, NO_MEETING, VISIT, COORDINATION, VACCINATION, CONSULTATION, LABORATORY, CLOSING_NOTE, INPATIENT,
        REGISTRATION, TEST_RESULT;

        @Override
        public String toString() {
            switch (this) {
                case GENERIC_NOTE:
                    return "Anteckning";
                case NO_MEETING:
                    return "Anteckning utan fysiskt möte";
                case VISIT:
                    return "Besöksanteckning";
                case COORDINATION:
                    return "Samordning";
                case VACCINATION:
                    return "Vaccinering";
                case CONSULTATION:
                    return "Konsultationsremiss";
                case LABORATORY:
                    return "Labbremiss";
                case CLOSING_NOTE:
                    return "Slutanteckning";
                case INPATIENT:
                    return "Slutenvårdsanteckning";
                case REGISTRATION:
                    return "Inskrivning";
                case TEST_RESULT:
                    return "Provsvar";
                default:
                    throw new AssertionError();
            }
        }

        static Type parse(String s) {
            for (Type type : values()) {
                if (s.equals(type.toString()))
                    return type;
            }
            switch (s) {
                case "Comirnaty, mot Covid-19":
                    return VACCINATION;
                case "Konsultationsremiss, Labbremiss":
                    return LABORATORY;
                case "Åtgärd/behandling": // occurs only once and is empty, nothing to gleam
                    return GENERIC_NOTE;
                default:
                    throw new AssertionError();
            }
        }
    }

    @SuppressWarnings("unused")
    public static List<MedicalRecord> createFromHtml(File recordsFile) throws IOException {
        ArrayList<MedicalRecord> output = new ArrayList<>(800);
        Document d = Jsoup.parse(recordsFile);
        for (Element recordE : d.select("li.ic-block-list__item")) {
            Element headingE = recordE.selectFirst("button.nc-journal-overview--expandable article header");
            Element sectionE = recordE.selectFirst("div.nc-detail--loader > section.ic-section");
            Elements allDetailsE = sectionE.select("div.information-details");
            Element detailsE = allDetailsE.get(0);

            String categoryTypeS = headingE.selectFirst("span.iu-tt-uc").text();
            String headingTypeS = headingE.selectFirst("h3 > a").text();

            Element detailsHeadingE = detailsE.selectFirst("div.nc-print > div.nc-print-heading");
            String detailsTypeS = detailsHeadingE.selectFirst("h3.nc-heading__information-type").text();
            Element timestampQ = detailsHeadingE.selectFirst("span.nc-document-timestamp");
            if (timestampQ == null)
                continue; // TODO nonstandard time notation for TEST_RESULT
            String timestampS = detailsHeadingE.selectFirst("span.nc-document-timestamp").text();

            Elements detailsRows = detailsE.select("div.information-details__row");
            assert detailsRows.size() == 1;
            Element originE = detailsRows.get(0).selectFirst("div.detail-description");
            String author = originE.ownText();
            String provider = originE.selectFirst("span.nu-display-block").text();

            // more convoluted payload DOM sometimes, worth resolving? searchword is "Melior"
            Elements payloadFieldsE = new Elements(allDetailsE.subList(1, allDetailsE.size()));
            String payloadTypeS = null;
            Elements payloadE = payloadFieldsE.select("div.information-details__row");
            ArrayList<TitledString> payloadTextL = new ArrayList<TitledString>(payloadE.size());
            for (int i = 0; i < payloadE.size(); i++) {
                String title = payloadE.get(i).selectFirst(".detail-title").text();
                String body = payloadE.get(i).selectFirst("div.detail-description").text();
                if (title.isBlank())
                    continue;
                if (body.isBlank()) {
                    continue;
                }
                payloadTextL.add(new TitledString(title, body));
            }
            TitledString[] payloadText = payloadTextL.toArray(new TitledString[payloadTextL.size()]);

            DiaryDate date;
            if (timestampS.contains(":"))
                date = new DiaryDateTime(timestampS + ":00");
            else
                date = new DiaryDate(timestampS);

            assert headingTypeS.equals(detailsTypeS); // will not be true for TEST_RESULT
            String bestTypeS = detailsTypeS;
            Type type = Type.parse(bestTypeS);

            MedicalRecord m = new MedicalRecord(date, type, author, provider, payloadText);
            output.add(m);
        }
        return output;
    }
}
