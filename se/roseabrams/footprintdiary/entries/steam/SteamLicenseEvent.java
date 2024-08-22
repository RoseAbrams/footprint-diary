package se.roseabrams.footprintdiary.entries.steam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntryCategory;

public class SteamLicenseEvent extends SteamEvent {

    public final String NAME;
    public final Method METHOD;

    public SteamLicenseEvent(DiaryDate date, String name, Method method) {
        super(DiaryEntryCategory.STEAM, date);
        NAME = name;
        METHOD = method;
    }

    @Override
    public String getStringSummary() {
        return NAME;
    }

    public static enum Method {
        STORE, FREE, GIFT, RETAIL;
    }

    public static SteamLicenseEvent[] createFromHtml(File licenses) throws IOException {
        ArrayList<SteamLicenseEvent> output = new ArrayList<>(300);
        Document d = Jsoup.parse(licenses);
        Elements tableRows = d.select("tbody > tr[data-panel]");
        for (Element tableRow : tableRows) {
            Elements rowCells = tableRow.select("td");
            String dateS = rowCells.get(0).text();
            String licenseName = rowCells.get(1).ownText();
            String methodS = rowCells.get(2).text();

            DiaryDate date = new DiaryDate(Short.parseShort(dateS.substring(dateS.indexOf(",") + 2)),
                    DiaryDate.parseMonthName(dateS.substring(dateS.indexOf(" ") + 1, dateS.indexOf(","))),
                    Byte.parseByte(dateS.substring(0, 2).trim()));
            Method method;
            switch (methodS) {
                case "Steam Store":
                    method = Method.STORE;
                    break;
                case "Complimentary":
                    method = Method.FREE;
                    break;
                case "Gift/Guest Pass":
                    method = Method.GIFT;
                    break;
                case "Retail":
                    method = Method.RETAIL;
                    break;
                default:
                    throw new AssertionError();
            }

            SteamLicenseEvent s = new SteamLicenseEvent(date, licenseName, method);
            output.add(s);
        }
        return output.toArray(new SteamLicenseEvent[output.size()]);
    }
}
