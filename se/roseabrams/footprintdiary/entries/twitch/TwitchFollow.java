package se.roseabrams.footprintdiary.entries.twitch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.Util;

public class TwitchFollow extends TwitchEvent {

    public final boolean FOLLOWING; // as opposed to unfollowing
    public final String STREAM_CATEGORY;

    public TwitchFollow(DiaryDateTime date, boolean following, String channel, String streamCategory) {
        super(DiaryEntryCategory.TWITCH_FOLLOW, date, channel);
        FOLLOWING = following;
        STREAM_CATEGORY = streamCategory.intern();
    }

    @Override
    public String getStringSummary() {
        return (FOLLOWING ? "" : "un") + "followed " + CHANNEL;
    }

    @SuppressWarnings("unused")
    public static List<TwitchFollow> createFromCsv(File twitchFollowFile, File twitchUnfollowFile) throws IOException {
        ArrayList<TwitchFollow> output = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            boolean isFollow;
            List<String> follows;
            switch (i) {
                case 0:
                    follows = Util.readFileLines(twitchFollowFile);
                    isFollow = true;
                    break;
                case 1:
                    follows = Util.readFileLines(twitchUnfollowFile);
                    isFollow = false;
                    break;
                default:
                    throw new AssertionError();
            }
            for (String follow : follows) {
                Scanner s = new Scanner(follow);
                s.useDelimiter(",");

                String time = s.next();
                String ip = s.next();
                String city = s.next();
                String country = s.next();
                int region = s.nextInt();
                String asn = s.next();
                String deviceId = s.next();
                String channel = s.next();
                String channelGame = s.next();
                String hostChannel;
                if (i == 0)
                    hostChannel = s.next();
                String source = s.next();
                String login = s.next();
                String platform;
                boolean partner;
                boolean loggedIn;
                if (i == 1) {
                    platform = s.next();
                    partner = s.nextBoolean();
                    loggedIn = s.nextBoolean();
                }
                if (i == 0) {
                    loggedIn = s.nextBoolean();
                    platform = s.next();
                    partner = s.nextBoolean();
                }
                String clientTime = s.next();
                if (i == 0) {
                    String vodId = s.next();
                    boolean ctaVisible = s.nextBoolean();
                }
                String followCount;
                if (i == 1) {
                    hostChannel = s.next();
                    followCount = s.next();
                }
                int userId = s.nextInt();
                assert userId == PersonalConstants.TWITCH_USERID;
                int hostChannelId = s.nextInt();
                int channelId = s.nextInt();
                String appVersion;
                String player;
                String game = "";
                if (i == 0) {
                    appVersion = s.next();
                    player = s.next();
                    game = s.next();
                }
                String clientApp = s.next();
                String isRecommendation;
                String mWebDeviceId;
                String multiStreamId;
                String screenName;
                String interaction;
                if (i == 0) {
                    isRecommendation = s.next();
                    mWebDeviceId = s.next();
                    multiStreamId = s.next();
                    assert multiStreamId.isEmpty();
                    screenName = s.next();
                    interaction = s.next();
                }
                String timeUtc = s.next();
                String url;
                if (i == 0)
                    url = s.next();
                s.close();

                DiaryDateTime date = new DiaryDateTime(time);
                assert channelGame.isBlank() || game.isBlank(); // one or both may be empty, but both should never be non-empty
                String bestGame = game.isBlank() ? channelGame : game;

                TwitchFollow t = new TwitchFollow(date, isFollow, channel, bestGame);
                output.add(t);
            }
        }
        return output;
    }
}
