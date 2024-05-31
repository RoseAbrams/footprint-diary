package se.roseabrams.footprintdiary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import se.roseabrams.footprintdiary.entries.camera.CameraPicture;
import se.roseabrams.footprintdiary.entries.camera.CameraVideo;
import se.roseabrams.footprintdiary.entries.camera.ScreenRecording;
import se.roseabrams.footprintdiary.entries.camera.Screenshot;
import se.roseabrams.footprintdiary.entries.health.DailyActivity;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlayback;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlaylisting;
import se.roseabrams.footprintdiary.interfaces.Message;

public enum DiaryEntrySource { // categorization intent is for human displaying
    DISCORD {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I sent " + filteredList.size() + " messages on Discord.";
        }
    },
    TORRENT {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I started downloading " + filteredList.size() + " torrents.";
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
                output.append(nPictures).append(" photos, ");
            }
            if (nVideos != 0) {
                output.append(nVideos).append(" videos, ");
            }
            if (nScreenshots != 0) {
                output.append(nScreenshots).append(" screenshots, ");
            }
            if (nScreenRecordings != 0) {
                output.append(nScreenRecordings).append(" screen recordings, ");
            }
            output.substring(0, output.length() - 3);
            output.append('.');
            return output.toString();
        }
    },
    MEME_SAVED {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I saved " + filteredList.size() + " memes that I found online.";
        }
    },
    MEME_CREATED {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I made " + filteredList.size() + " memes and published "
                    + (filteredList.size() == 1 ? "it" : "them") + " online.";
        }
    },
    WALLPAPER_SAVED {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I saved " + filteredList.size() + " wallpapers that I found online.";
        }
    },
    OTHER_MEMESQUE_SAVED {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I saved " + filteredList.size()
                    + " other things that I found online and never managed to sort them.";
        }
    },
    ACTIVITY {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I walked " + ((DailyActivity) filteredList.get(0)).DISTANCE_WALKED + " kilometers.";
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
            return "I sent " + nSent + " messages on WhatsApp, and received " + nRecieved + ".";
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
            return "I listened to " + nPlayed + " songs on Spotify, adding " + nPlaylisted + " of them to playlists.";
        }
    },
    STEAM {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> filteredList) {
            return "I bought " + filteredList.size() + " games on Steam.";
            // TODO update when more classes are finished
        }
    };

    public abstract String describeInProse(ArrayList<DiaryEntry> filteredList);

    public int customOrder() {
        switch (this) {
            case MANUAL:
                return Integer.MAX_VALUE;
            default:
                return ordinal();
        }
    }

    public static DiaryEntrySource[] valuesCustomOrder() {
        DiaryEntrySource[] output = values().clone();
        Arrays.sort(output, new CustomOrder());
        return output;
    }

    private static class CustomOrder implements Comparator<DiaryEntrySource> {

        @Override
        public int compare(DiaryEntrySource o1, DiaryEntrySource o2) {
            return Integer.compare(o1.customOrder(), o2.customOrder());
        }
    }
}