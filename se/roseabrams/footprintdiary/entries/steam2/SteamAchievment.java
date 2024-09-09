package se.roseabrams.footprintdiary.entries.steam2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.entries.steam.SteamEvent;
import se.roseabrams.footprintdiary.entries.steam2.SteamFetcher.Endpoint;

public class SteamAchievment extends SteamEvent {

    // will probably branch out game into own class later
    public final int GAME_ID;
    public final String GAME_NAME;
    public final String TITLE;
    public final String DESCRIPTION;

    public SteamAchievment(DiaryDateTime date, int gameId, String gameName, String title, String description) {
        super(DiaryEntryCategory.STEAM_ACHIEVMENT, date);

        assert gameId > 0;
        assert gameName != null && !gameName.isBlank();
        assert title != null && !title.isBlank();
        assert description != null;

        GAME_ID = gameId;
        GAME_NAME = gameName;
        TITLE = title;
        DESCRIPTION = description;
    }

    @Override
    public String getStringSummary() {
        return "\"" + TITLE + "\" (" + GAME_NAME + ")";
    }

    public static List<SteamAchievment> createFromApi(SteamFetcher api) throws IOException {
        int nEmpty = 0;
        int nOkButEmpty = 0;
        int nEmptyDesc = 0;
        ArrayList<SteamAchievment> output = new ArrayList<>(1000);
        JSONObject gamesO = api.call(Endpoint.PLAYER_OWNED_GAMES, 0).getJSONObject("response");
        JSONArray games = gamesO.getJSONArray("games");
        for (Object gameO : games) {
            try {
                Thread.sleep(200); // to avoid being IPblocked for DoS
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }

            JSONObject game = (JSONObject) gameO;
            int appId = game.getInt("appid");
            String gameName = game.getString("name");
            JSONObject achievmentsR = api.call(Endpoint.PLAYER_ACHIEVMENTS, appId);
            if (achievmentsR == null) {
                nEmpty++;
                continue; // no stats for game
            }
            JSONObject achievments = achievmentsR.getJSONObject("playerstats");
            if (!achievments.getBoolean("success")) {
                //if (!achievments.getString("error").equals("Requested app has no stats"))
                throw new AssertionError();
                //continue;
            }
            if (!achievments.has("achievements")) {
                System.err.println("API call returned OK but response was empty: " + gameName);
                nOkButEmpty++;
                continue;
            }
            for (Object achievmentO : achievments.getJSONArray("achievements")) {
                JSONObject achievment = (JSONObject) achievmentO;
                if (achievment.getInt("achieved") == 0)
                    continue;
                String name = achievment.getString("name");
                String desc = achievment.getString("description");
                @SuppressWarnings("unused")
                String apiName = achievment.getString("apiname");
                long dateL = achievment.getLong("unlocktime");
                assert dateL > 0;

                if (desc.isBlank()) {
                    //System.err.println("Unspecified description for achievment: \"" + name + "\" (" + gameName + ")");
                    nEmptyDesc++;
                }
                SteamAchievment a = new SteamAchievment(new DiaryDateTime(dateL), appId, gameName, name, desc);
                output.add(a);
            }
        }
        System.out.println((nEmpty + nOkButEmpty) + " out of " + games.length() + " games returned as empty");
        System.out.println("\t... out of which " + nOkButEmpty + " still gave a response of OK");
        System.out.println(nEmptyDesc + " out of " + output.size() + " achievments had unspecified descriptions");
        return output;
    }
}
