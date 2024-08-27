package se.roseabrams.footprintdiary.entries.steam2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.Util;

public class SteamFetcher {

    private final String API_KEY;

    public SteamFetcher(File apiKey) {
        try {
            API_KEY = Util.readFile(apiKey);
        } catch (IOException e) {
            throw new AssertionError("API key file could not be read.", e);
        }
    }

    /// https://developer.valvesoftware.com/wiki/Steam_Web_API
    public static enum Endpoint {
        PLAYER_SUMMARY, PLAYER_FRIENDS, PLAYER_ACHIEVMENTS, PLAYER_STATS, PLAYER_OWNED_GAMES,
        PLAYER_RECENTLY_PLAYED_GAMES;

        @Override
        public String toString() {
            switch (this) {
                //case APP_NEWS:
                //    return "ISteamNews/GetNewsForApp/v0002";
                //case APP_GLOBAL_ACHIEVMENT_PERCENTAGE:
                //    return "ISteamUserStats/GetGlobalAchievementPercentagesForApp/v0002";
                case PLAYER_SUMMARY:
                    return "ISteamUser/GetPlayerSummaries/v0002";
                case PLAYER_FRIENDS:
                    return "ISteamUser/GetFriendList/v0001";
                case PLAYER_ACHIEVMENTS:
                    return "ISteamUserStats/GetPlayerAchievements/v0001";
                case PLAYER_STATS:
                    return "ISteamUserStats/GetUserStatsForGame/v0002";
                case PLAYER_OWNED_GAMES:
                    return "IPlayerService/GetOwnedGames/v0001";
                case PLAYER_RECENTLY_PLAYED_GAMES:
                    return "IPlayerService/GetRecentlyPlayedGames/v0001";
                default:
                    throw new AssertionError();
            }
        }
    }

    private JSONObject call(Endpoint endpoint, int appId) throws IOException {
        StringBuilder urlS = new StringBuilder(150);
        urlS.append("http://api.steampowered.com/").append(endpoint.toString())
                .append("/?key=").append(API_KEY)
                .append("&format=json")
                .append("&steamid").append(endpoint == Endpoint.PLAYER_SUMMARY ? "s" : "").append("=")
                .append(PersonalConstants.STEAM_USERID);
        switch (endpoint) {
            case PLAYER_ACHIEVMENTS:
            case PLAYER_STATS:
                assert appId > 0;
                urlS.append("&appid=").append(appId).append("&l=EN");
                break;
            case PLAYER_FRIENDS:
                urlS.append("&relationship=all");
                break;
            case PLAYER_OWNED_GAMES:
                urlS.append("&include_appinfo=true&include_played_free_games=true");
                break;
            default:
                break;
        }

        URL url = new URL(urlS.toString());
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod("GET");
        int responseCode = c.getResponseCode();
        if (responseCode != 200)
            throw new IllegalStateException("API call failed with response code " + responseCode);

        StringBuilder output = new StringBuilder();
        BufferedReader response = new BufferedReader(new InputStreamReader(c.getInputStream()));
        while (true) {
            String responseLine = response.readLine();
            if (responseLine == null)
                break;
            output.append(responseLine);
        }

        return new JSONObject(output.toString());
    }
}
