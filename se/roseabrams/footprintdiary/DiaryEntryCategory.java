package se.roseabrams.footprintdiary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import se.roseabrams.footprintdiary.entries.camera.CameraPicture;
import se.roseabrams.footprintdiary.entries.camera.CameraVideo;
import se.roseabrams.footprintdiary.entries.camera.ScreenRecording;
import se.roseabrams.footprintdiary.entries.camera.Screenshot;
import se.roseabrams.footprintdiary.entries.health.DailyActivity;
import se.roseabrams.footprintdiary.entries.reddit.RedditComment;
import se.roseabrams.footprintdiary.entries.reddit.RedditSubmission;
import se.roseabrams.footprintdiary.entries.reddit.RedditPost;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlayback;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlaylisting;
import se.roseabrams.footprintdiary.interfaces.Message;

public enum DiaryEntryCategory { // categorization intent is for human displaying
    DISCORD {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I sent " + filteredList.size() + " message" + p(filteredList.size()) + " on Discord.";
        }
    },
    TORRENT {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I started downloading " + filteredList.size() + " torrent" + p(filteredList.size()) + ".";
        }
    },
    CAMERA {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            int nPictures = 0;
            int nVideos = 0;
            int nScreenshots = 0;
            int nScreenRecordings = 0;
            for (DiaryEntry e : filteredList) {
                if (e instanceof CameraPicture)
                    nPictures++;
                else if (e instanceof CameraVideo)
                    nVideos++;
                else if (e instanceof Screenshot)
                    nScreenshots++;
                else if (e instanceof ScreenRecording)
                    nScreenRecordings++;
            }

            StringBuilder output = new StringBuilder(40);
            output.append("I used my camera ").append(nPictures + nVideos + nScreenshots + nScreenRecordings)
                    .append(" times. I took ");
            if (nPictures != 0) {
                output.append(nPictures).append(" photo" + p(nPictures) + ", ");
            }
            if (nVideos != 0) {
                output.append(nVideos).append(" video" + p(nVideos) + ", ");
            }
            if (nScreenshots != 0) {
                output.append(nScreenshots).append(" screenshot" + p(nScreenshots) + ", ");
            }
            if (nScreenRecordings != 0) {
                output.append(nScreenRecordings).append(" screen recording" + p(nScreenRecordings) + ", ");
            }
            output.substring(0, output.length() - 3);
            output.append('.');
            return output.toString();
        }
    },
    MEME_SAVED {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I saved " + filteredList.size() + " meme" + p(filteredList.size()) + " that I found online.";
        }
    },
    MEME_CREATED {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I made " + filteredList.size() + " meme" + p(filteredList.size()) + " and published "
                    + (filteredList.size() == 1 ? "it" : "them") + " online.";
        }
    },
    WALLPAPER_SAVED {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I saved " + filteredList.size() + " wallpaper" + p(filteredList.size()) + " that I found online.";
        }
    },
    ARTWORK_SAVED {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I saved " + filteredList.size() + " piece" + p(filteredList.size())
                    + " of arwork that I found online.";
        }
    },
    OTHER_MEMESQUE_SAVED {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I saved " + filteredList.size() + " other thing" + p(filteredList.size())
                    + " that I found online and never managed to sort.";
        }
    },
    ACTIVITY {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            float km = ((DailyActivity) filteredList.get(0)).KILOMETERS_WALKED;
            return "I walked " + km + " kilometer" + p(Math.round(km)) + ".";
            // TODO update when more classes are finished
        }
    },
    MANUAL {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "Finally, I wanna note this event today: " + filteredList.get(0);
        }
    },
    WHATSAPP {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            int nSent = 0;
            int nRecieved = 0;
            for (DiaryEntry e : filteredList) {
                if (((Message) e).isByMe())
                    nSent++;
                else
                    nRecieved++;
            }
            return "I sent " + nSent + " message" + p(filteredList.size()) + " on WhatsApp, and received " + nRecieved
                    + ".";
        }
    },
    SKYPE {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            int nSent = 0;
            int nRecieved = 0;
            for (DiaryEntry e : filteredList) {
                if (((Message) e).isByMe())
                    nSent++;
                else
                    nRecieved++;
            }
            return "I sent " + nSent + " message" + p(filteredList.size()) + " on Skype, and received " + nRecieved
                    + ".";
        }
    },
    SPOTIFY {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            int nPlayed = 0;
            int nPlaylisted = 0;
            for (DiaryEntry e : filteredList) {
                if (e instanceof SpotifyPlayback)
                    nPlayed++;
                else if (e instanceof SpotifyPlaylisting)
                    nPlaylisted++;
            }
            return "I listened to " + nPlayed + " song" + p(filteredList.size()) + " on Spotify and added "
                    + nPlaylisted + " song" + p(filteredList.size()) + " to my playlists.";
        }
    },
    STEAM {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I bought " + filteredList.size() + " game" + p(filteredList.size()) + " on Steam.";
            // TODO update when more classes are finished
        }
    },
    REDDIT {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            int nPosts = 0;
            int nComments = 0;
            for (DiaryEntry e : filteredList) {
                if (e instanceof RedditPost)
                    nPosts++;
                else if (e instanceof RedditComment)
                    nComments++;
            }
            StringBuilder output = new StringBuilder();
            output.append("I posted ");
            if (nPosts > 0)
                output.append(nPosts).append(" post").append(p(nPosts));
            if (nPosts > 0 && nComments > 0)
                output.append(" and ");
            if (nComments > 0)
                output.append(nComments).append(" comment").append(p(nComments));
            output.append(" on Reddit.");
            return output.toString();
        }
    },
    WIKIMEDIA_EDIT {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I made " + filteredList.size() + " edit" + p(filteredList.size()) + " at Wikimedia.";
        }
    },;

    public abstract String describeInProse(ArrayList<DiaryEntry> filteredList);

    public int customOrder() {
        switch (this) {
            case MANUAL:
                return Integer.MAX_VALUE;
            default:
                return ordinal();
        }
    }

    private static String p(int v) {
        return Util.pluralSuffix(v);
    }

    public static DiaryEntryCategory[] valuesCustomOrder() {
        DiaryEntryCategory[] output = values().clone();
        Arrays.sort(output, new CustomOrder());
        return output;
    }

    private static class CustomOrder implements Comparator<DiaryEntryCategory> {

        @Override
        public int compare(DiaryEntryCategory o1, DiaryEntryCategory o2) {
            return Integer.compare(o1.customOrder(), o2.customOrder());
        }
    }
}