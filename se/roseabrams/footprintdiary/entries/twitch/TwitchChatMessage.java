package se.roseabrams.footprintdiary.entries.twitch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.PersonalConstants;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.common.Message;

public class TwitchChatMessage extends TwitchEvent implements Message {

    public final String CHANNEL;
    public final String BODY;
    public final boolean IS_REPLY;
    public final String CHANNEL_POINT_REWARD;

    public TwitchChatMessage(DiaryDateTime date, String channel, String body, boolean isReply,
            String channelPointReward) {
        super(date);
        assert body != null && !body.isBlank();
        CHANNEL = channel.intern();
        BODY = body;
        IS_REPLY = isReply;
        CHANNEL_POINT_REWARD = channelPointReward.intern();
    }

    @Override
    public String getSender() {
        return PersonalConstants.TWITCH_USERNAME;
    }

    @Override
    public String getRecipient() {
        return CHANNEL;
    }

    @Override
    public boolean isByMe() {
        return true;
    }

    @Override
    public String getStringSummary() {
        return BODY;
    }

    @SuppressWarnings("unused")
    public static TwitchChatMessage[] createFromCsv(File twitchChatFile) throws IOException {
        ArrayList<TwitchChatMessage> output = new ArrayList<>();
        List<String> chatMessages = Util.readFileLines(twitchChatFile);
        for (String chatMessage : chatMessages) {
            Scanner s = new Scanner(chatMessage);
            s.useDelimiter(",");

            String time = s.next();
            String city = s.next();
            String countryCode = s.next();
            int region = s.nextInt();
            String bodyFull = s.next();
            int asnId = s.nextInt();
            String ip = s.next();
            String login = s.next();
            long timeServer = s.nextLong();
            String body = s.next();
            assert bodyFull.isEmpty() || bodyFull.equals(body); // as a test
            String channel = s.next();
            String msgId = s.next();
            String tags = s.next();
            String asn = s.next();
            int userId = s.nextInt();
            assert userId == PersonalConstants.TWITCH_USERID;
            String timeUtc = s.next();
            String clientId = s.next();
            String roomId = s.next();
            assert roomId.isEmpty();
            String roomType = s.next();
            assert roomType.isEmpty();
            String chatType = s.next();
            assert chatType.equals("twitch_chat");
            String chatId = s.next();
            assert chatId.isEmpty();
            String channelPointReward = s.next();
            String previousDrop = s.next();
            assert previousDrop.isEmpty();
            boolean isReply = s.nextBoolean();
            boolean isMention = s.nextBoolean();
            String chantId = s.next();
            assert chantId.isEmpty();
            String chantMessageId = s.next();
            assert chantMessageId.isEmpty();
            s.close();

            DiaryDateTime date = new DiaryDateTime(time);
            body = body.replace("\'@", "@");
            TwitchChatMessage t = new TwitchChatMessage(date, channel, body, isReply, channelPointReward);
            output.add(t);
        }
        return output.toArray(new TwitchChatMessage[output.size()]);
    }
}
