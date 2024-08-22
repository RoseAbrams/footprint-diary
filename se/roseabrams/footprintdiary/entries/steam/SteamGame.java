package se.roseabrams.footprintdiary.entries.steam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.common.ContentType;
import se.roseabrams.footprintdiary.common.Webpage;

public class SteamGame extends Webpage {

    public final int ID;
    public final String NAME;
    static final ArrayList<SteamGame> CACHE = new ArrayList<>(1000);

    public SteamGame(int appId, String name) {
        super("https://store.steampowered.com/app/" + appId, name, ContentType.APPLICATION);
        ID = appId;
        NAME = name;
    }

    @Override
    public String toString() {
        return NAME;
    }

    static SteamGame get(String name, boolean allowNull) {
        for (SteamGame g : CACHE) {
            if (g.TITLE.equals(name))
                return g; // TODO same products have different name formats all across
        }

        if (allowNull)
            return null;
        else
            throw new NoSuchElementException("No cached SteamGame named \"" + name + "\"");
    }

    public static void cacheFromWebsite() throws IOException {
        Connection c = Jsoup.newSession();
        c.timeout(120000);
        c.url("https://steamcommunity.com/id/" + PersonalConstants.STEAM_USERNAME + "/games/?tab=all");
        Document d = c.get();

        Elements gameLinks = d.select("span.w6q9piMq3gT16oj_lEvpy > a._22awlPiAoaZjQMqxJhp-KP");
        for (Element gameLink : gameLinks) {
            String gameName = gameLink.text().intern();
            String gameUrl = gameLink.attr("href");
            String gameIdS = gameUrl.substring(gameUrl.lastIndexOf("/") + 1);
            SteamGame g = new SteamGame(Integer.parseInt(gameIdS), gameName);
            CACHE.add(g);
        }
    }
}
