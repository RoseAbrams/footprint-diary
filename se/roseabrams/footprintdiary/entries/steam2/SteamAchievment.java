package se.roseabrams.footprintdiary.entries.steam2;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.common.TitledString;
import se.roseabrams.footprintdiary.entries.steam.SteamEvent;
import se.roseabrams.footprintdiary.entries.steam2.SteamFetcher.Endpoint;

public class SteamAchievment extends SteamEvent {

    // will probably branch out game into own class later
    public final int GAME_ID;
    public final String GAME_NAME;
    public final TitledString TEXT;

    public SteamAchievment(DiaryDateTime date, int gameId, String gameName, TitledString text) {
        super(DiaryEntryCategory.STEAM, date);
        GAME_ID = gameId;
        GAME_NAME = gameName;
        TEXT = text;
    }

    @Override
    public String getStringSummary() {
        return TEXT.TITLE + " (" + GAME_NAME + ")";
    }

    public static SteamAchievment[] createFromApi(SteamFetcher api) throws IOException {
        ArrayList<SteamAchievment> output = new ArrayList<>(1000);
        JSONObject games = api.call(Endpoint.PLAYER_OWNED_GAMES, 0).getJSONObject("response");
        for (Object gameO : games.getJSONArray("games")) {
            JSONObject game = (JSONObject) gameO;
            int appId = game.getInt("appid");
            String gameName = game.getString("name");
            JSONObject achievments = api.call(Endpoint.PLAYER_ACHIEVMENTS, appId).getJSONObject("playerstats");
            if (!achievments.getBoolean("success")) {
                if (!achievments.getString("error").equals("Requested app has no stats"))
                    throw new AssertionError(); // investigate if it happens
                continue;
            }
            for (Object achievmentO : achievments.getJSONArray("achievments")) {
                JSONObject achievment = (JSONObject) achievmentO;
                if (achievment.getInt("achieved") == 0)
                    continue;
                String name = achievment.getString("name");
                String desc = achievment.getString("description");
                long dateL = achievment.getLong("unlocktime");
                assert dateL > 0;
                SteamAchievment a = new SteamAchievment(new DiaryDateTime(dateL), appId, gameName, new TitledString(name, desc));
                output.add(a);
            }

            try {
                Thread.sleep(200); // to avoid being IPblocked for DoS
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }
        }
        return output.toArray(new SteamAchievment[output.size()]);
    }
}
