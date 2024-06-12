package se.roseabrams.footprintdiary.entries.steam;

import se.roseabrams.footprintdiary.common.ContentType;
import se.roseabrams.footprintdiary.common.Webpage;

public class SteamGame extends Webpage {

    public final String NAME;

    public SteamGame(int storeId, String name) {
        super("https://store.steampowered.com/app/" + storeId, ContentType.APPLICATION);
        NAME = name;
    }
}
