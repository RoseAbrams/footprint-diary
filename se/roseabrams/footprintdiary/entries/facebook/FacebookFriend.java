package se.roseabrams.footprintdiary.entries.facebook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;

public class FacebookFriend extends DiaryEntry {

    public final String NAME;
    public final FriendshipStatus STATUS;

    public FacebookFriend(DiaryDate dd, String name, FriendshipStatus status) {
        super(DiaryEntryCategory.FACEBOOK_FRIEND, dd);
        NAME = name;
        STATUS = status;
    }

    @Override
    public String getStringSummary() {
        return STATUS.toString().toLowerCase() + " friend request with " + NAME;
    }

    public static enum FriendshipStatus {
        ACCEPTED, SENT, REMOVED, REJECTED, RECIEVED;
    }

    public static List<FacebookFriend> createFromFolder(File friendsFolder) throws IOException {
        ArrayList<FacebookFriend> output = new ArrayList<>();
        for (File friendsFile : friendsFolder.listFiles()) {
            if (!friendsFile.getName().endsWith(".html")) {
                continue;
            }
            FriendshipStatus status;
            switch (friendsFile.getName()) {
                case "your_friends.html":
                    status = FriendshipStatus.ACCEPTED;
                    break;
                case "sent_friend_requests.html":
                    status = FriendshipStatus.SENT;
                    break;
                case "removed_friends.html":
                    status = FriendshipStatus.REMOVED;
                    break;
                case "rejected_friend_requests.html":
                    status = FriendshipStatus.REJECTED;
                    break;
                case "received_friend_requests.html":
                    status = FriendshipStatus.RECIEVED;
                    break;
                default:
                    throw new AssertionError("Folder contents is unexpected.");
            }
            Document d = Jsoup.parse(friendsFile);
            Elements friendsE = d.select("div._a706 > div._3-95");
            for (Element friendE : friendsE) {
                String friendName = friendE.select("div._2ph_").text();
                String dateS = friendE.select("div._3-94 > div._a72d").text();
                DiaryDateTime date = FacebookWallEvent.parseDate(dateS);
                FacebookFriend f = new FacebookFriend(date, friendName, status);
                output.add(f);
            }
        }
        return output;
    }
}
