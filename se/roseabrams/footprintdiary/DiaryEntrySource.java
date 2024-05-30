package se.roseabrams.footprintdiary;

import java.util.ArrayList;

import se.roseabrams.footprintdiary.entries.health.DailyActivity;

public enum DiaryEntrySource { // categorization intent is for human displaying
    DISCORD {
        @Override
        public String prose(ArrayList<DiaryEntry> filteredList) {
            return "I sent " + filteredList.size() + " messages on Discord.";
        }
    },
    TORRENT {
        @Override
        public String prose(ArrayList<DiaryEntry> filteredList) {
            return "I started downloading " + filteredList.size() + " torrents.";
        }
    },
    CAMERA {
        @Override
        public String prose(ArrayList<DiaryEntry> filteredList) {
            StringBuilder output = new StringBuilder(40);
            output.append("I used my camera ").append(totals[0]).append(" times. I took ");
            if (totals[1] != 0) {
                output.append(totals[1]).append(" photos, ");
            }
            if (totals[2] != 0) {
                output.append(totals[2]).append(" videos, ");
            }
            if (totals[3] != 0) {
                output.append(totals[3]).append(" screenshots, ");
            }
            if (totals[4] != 0) {
                output.append(totals[4]).append(" screen recordings, ");
            }
            output.substring(0, output.length() - 3);
            output.append('.');
            String outputS = output.toString();
            return outputS;
        }
    },
    MEME_SAVED {
        @Override
        public String prose(ArrayList<DiaryEntry> filteredList) {
            return "I saved " + filteredList.size() + " memes that I found online.";
        }
    },
    MEME_CREATED {
        @Override
        public String prose(ArrayList<DiaryEntry> filteredList) {
            return "I made " + filteredList.size() + " memes that I published online.";
        }
    },
    WALLPAPER_SAVED {
        @Override
        public String prose(ArrayList<DiaryEntry> filteredList) {
            return "I saved " + filteredList.size() + " wallpapers that I found online.";
        }
    },
    OTHER_MEMESQUE_SAVED {
        @Override
        public String prose(ArrayList<DiaryEntry> filteredList) {
            return "I saved " + filteredList.size() + " things that I found online, but never managed to quite sort them so not sure what they are.";
        }
    },
    HEALTH {
        @Override
        public String prose(ArrayList<DiaryEntry> filteredList) {
            return "I walked " + ((DailyActivity) filteredList.get(0)).DISTANCE_WALKED + " kilometers.";
        }
    },
    MANUAL {
        @Override
        public String prose(ArrayList<DiaryEntry> filteredList) {
            return "Finally, I wanna note this event today: " + totals[0];
        }
    },
    WHATSAPP {
        @Override
        public String prose(ArrayList<DiaryEntry> filteredList) {
            return "I sent " + totals[0] + " messages on WhatsApp.";
        }
    },
    SPOTIFY {
        @Override
        public String prose(ArrayList<DiaryEntry> filteredList) {
            return "I listened to " + totals[0] + " songs on Spotify, adding " + totals[1] + " of them to playlists.";
        }
    },
    STEAM {
        @Override
        public String prose(ArrayList<DiaryEntry> filteredList) {
            return "I bought " + totals[0] + " games on Steam.";
        }
    };

    public int getOrdering() {
        switch (this) {
            case MANUAL:
                return Integer.MAX_VALUE;
            default:
                return ordinal();
        }
    }

    public abstract String prose(ArrayList<DiaryEntry> filteredList);
}