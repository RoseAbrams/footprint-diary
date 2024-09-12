package se.roseabrams.footprintdiary.entries.steam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.common.TitledString;

@Deprecated /// replaced with steam2.SteamAchievment
public class SteamAchievment extends SteamEvent {

    public final SteamGame GAME;
    public final TitledString TEXT;

    public SteamAchievment(DiaryDateTime date, SteamGame game, TitledString text) {
        super(DiaryEntryCategory.STEAM_ACHIEVMENT, date);
        GAME = game;
        TEXT = text;
    }

    @Override
    public String getStringSummary() {
        return TEXT.TITLE + " (" + GAME.NAME + ")";
    }

    public static List<SteamAchievment> createFromWebsite() throws IOException {
        ArrayList<SteamAchievment> output = new ArrayList<>(1000);
        Connection c = Jsoup.newSession();
        c.timeout(120000);
        for (SteamGame game : SteamGame.CACHE) {
            c.url("https://steamcommunity.com/id/" + PersonalConstants.STEAM_USERNAME + "/stats/" + game.ID
                    + "/?tab=achievements");
            Document d = c.get();
            Elements achievmentsE = d.select("div.achieveRow > div.achieveTxtHolder");

            for (Element achievmentE : achievmentsE) {
                Element textE = achievmentE.selectFirst("div.achieveTxt");
                Element dateE = achievmentE.selectFirst("div.achieveUnlockTime");
                if (dateE == null)
                    continue; // achievement is not achieved
                TitledString text = new TitledString(textE.selectFirst("h3").text(), textE.selectFirst("h5").text());
                String dateS = dateE.text().substring("Unlocked ".length());
                DiaryDateTime date = new DiaryDateTime(
                        dateS.contains(", ")
                                ? Short.parseShort(dateS.substring(dateS.indexOf(", ") + 2, dateS.indexOf("@")).trim())
                                : DiaryDate.TODAY.YEAR,
                        DiaryDate.parseMonthName(dateS.substring(2, 5).trim()),
                        Byte.parseByte(dateS.substring(0, 2).trim()),
                        Byte.parseByte(dateS.substring(dateS.indexOf("@") + 2, dateS.indexOf(":"))),
                        Byte.parseByte(dateS.substring(dateS.indexOf(":") + 1)),
                        (byte) 0);

                SteamAchievment a = new SteamAchievment(date, game, text);
                output.add(a);
            }
        }
        return output;
    }
}
