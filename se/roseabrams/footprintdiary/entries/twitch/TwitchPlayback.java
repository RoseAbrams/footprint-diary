package se.roseabrams.footprintdiary.entries.twitch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.Util;

public class TwitchPlayback extends TwitchWatchEvent {

    public final String HOSTED_CHANNEL;
    public final String STREAM_CATEGORY;
    public final int WATCHTIME_MINUTES;
    public final boolean IS_EMBED;

    public TwitchPlayback(DiaryDate date, String channel, String hostedChannel, String streamCategory,
            int watchtimeMinutes, boolean isEmbed) {
        super(date, channel);
        assert watchtimeMinutes > 0;
        WATCHTIME_MINUTES = watchtimeMinutes;
        assert !streamCategory.isBlank();
        HOSTED_CHANNEL = hostedChannel.isBlank() ? null : hostedChannel.intern();
        STREAM_CATEGORY = streamCategory.intern();
        IS_EMBED = isEmbed;
    }

    @Override
    public int getWatchtimeMinutes() {
        return WATCHTIME_MINUTES;
    }

    public String getChannelOrHosted() {
        return HOSTED_CHANNEL != null ? HOSTED_CHANNEL : CHANNEL;
    }

    @Override
    public String getStringSummary() {
        return "watched " + getChannelOrHosted() + " stream " + STREAM_CATEGORY + " for " + WATCHTIME_MINUTES
                + " minutes";
    }

    @SuppressWarnings("unused")
    public static List<TwitchPlayback> createFromCsv(File watchFile) throws IOException {
        ArrayList<TwitchPlayback> output = new ArrayList<>(6000);
        List<String> watchInstances = Util.readFileLines(watchFile);
        for (String watchInstance : watchInstances) {
            Scanner s = new Scanner(watchInstance);
            s.useDelimiter(",");

            String broadcasterSoftware = s.next();
            String channelName = s.next();
            String channelId = s.next();
            String deviceId = s.next();
            boolean channelIsLive = s.nextBoolean();
            int minutesWatchedUnadjusted = s.nextInt();
            String referrerDomain = s.next();
            String referrerHost = s.next();
            assert referrerDomain.equals(referrerHost);
            String referrerUrl = s.next();
            String url = s.next();
            String viewerCountryCode = s.next();
            String viewerCity = s.next();
            int viewerRegion = s.nextInt();
            String asn = s.next();
            String hostChannelName = s.next();
            String userLogin = s.next();
            String chromecastSender = s.next();
            String platform = s.next();
            String contentMode = s.next();
            String vodId = s.next();
            String viewerMobileConnectionType = s.next();
            int userId = s.nextInt();
            assert userId == PersonalConstants.TWITCH_USERID;
            String day = s.next();
            String player = s.next();
            String gameName = s.next();
            int hostChannelId = s.nextInt();
            String mediumTrackingCode = s.next();
            String viewerLanguageCode = s.next();
            boolean isPlayerChatVisible = s.nextBoolean();
            String clientApp = s.next();
            String viewerDeviceModel = s.next();
            String viewerDeviceManufacturer = s.next();
            assert viewerDeviceManufacturer.isEmpty();
            String viewerBroadcastFamily = s.next();
            float viewerBrowserVersion = s.nextFloat();
            String viewerOsName = s.next();
            int viewerOsVersion = s.nextInt();
            s.close();

            boolean isEmbed = referrerDomain.isEmpty() || referrerDomain.equals("www.twitch.tv");
            // enumable fields: contentMode, player, mediumTrackingCode, platform

            TwitchPlayback t = new TwitchPlayback(new DiaryDate(day), channelName, hostChannelName, gameName,
                    minutesWatchedUnadjusted, isEmbed);
            output.add(t);
        }
        return output;
    }
}
