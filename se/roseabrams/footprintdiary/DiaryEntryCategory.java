package se.roseabrams.footprintdiary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import se.roseabrams.footprintdiary.entries.health.DailyActivity;
import se.roseabrams.footprintdiary.entries.reddit.RedditComment;
import se.roseabrams.footprintdiary.entries.reddit.RedditPost;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlayback;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlaylisting;
import se.roseabrams.footprintdiary.interfaces.ContentOwner;
import se.roseabrams.footprintdiary.interfaces.Message;
import se.roseabrams.footprintdiary.interfaces.MoneyTransaction;

public enum DiaryEntryCategory { // categorization intent is for human displaying

    DISCORD {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            return "I sent " + fl.size() + " message" + p(fl) + " on Discord.";
        }
    },
    TORRENT {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            return "I started downloading " + fl.size() + " torrent" + p(fl) + ".";
        }
    },
    CAMERA {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            int nPictures = 0;
            int nVideos = 0;
            for (DiaryEntry e : fl) {
                ContentOwner co = (ContentOwner) e;
                switch (co.getContent().TYPE) {
                    case PICTURE:
                        nPictures++;
                        break;
                    case VIDEO:
                        nVideos++;
                        break;
                    default:
                    throw new AssertionError(); // should never happen under current setup
                }
            }

            StringBuilder output = new StringBuilder(40);
            output.append("I used my camera ").append(nPictures + nVideos)
                    .append(" times. I took ");
            if (nPictures != 0) {
                output.append(nPictures).append(" photo" + p(nPictures) + ", ");
            }
            if (nVideos != 0) {
                output.append(nVideos).append(" video" + p(nVideos) + ", ");
            }
            output.substring(0, output.length() - 3);
            output.append('.');
            return output.toString();
        }
    },
    MEME_SAVED {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            return "I saved " + fl.size() + " meme" + p(fl) + " that I found online.";
        }
    },
    MEME_CREATED {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            return "I made " + fl.size() + " meme" + p(fl) + " and published " + (fl.size() == 1 ? "it" : "them")
                    + " online.";
        }
    },
    WALLPAPER_SAVED {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            return "I saved " + fl.size() + " wallpaper" + p(fl) + " that I found online.";
        }
    },
    ARTWORK_SAVED {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            return "I saved " + fl.size() + " piece" + p(fl) + " of arwork that I found online.";
        }
    },
    OTHER_MEMESQUE_SAVED {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            return "I saved " + fl.size() + " other thing" + p(fl) + " that I found online but never sorted.";
        }
    },
    HEALTH {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            return "I did " + fl.size() + " healthy thing" + p(fl)
                    + ", but don't quite remember what. They're a bit of a mess.";
        }
    },
    DAILY_HEALTH {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            float km = ((DailyActivity) fl.get(0)).kmWalked();
            return "I walked " + km + " kilometer" + p(Math.round(km)) + ".";
        }
    },
    MANUAL {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            return "Finally, I wanna note this event today: " + fl.get(0);
        }
    },
    WHATSAPP {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            int nSent = 0;
            int nRecieved = 0;
            for (DiaryEntry e : fl) {
                if (((Message) e).isByMe())
                    nSent++;
                else
                    nRecieved++;
            }
            return "I sent " + nSent + " message" + p(nSent) + " on WhatsApp, and received " + nRecieved + ".";
        }
    },
    SKYPE {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            int nSent = 0;
            int nRecieved = 0;
            for (DiaryEntry e : fl) {
                if (((Message) e).isByMe())
                    nSent++;
                else
                    nRecieved++;
            }
            return "I sent " + nSent + " message" + p(nSent) + " on Skype, and received " + nRecieved + ".";
        }
    },
    SPOTIFY {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            int nPlayed = 0;
            int nPlaylisted = 0;
            for (DiaryEntry e : fl) {
                if (e instanceof SpotifyPlayback)
                    nPlayed++;
                else if (e instanceof SpotifyPlaylisting)
                    nPlaylisted++;
            }
            return "I listened to " + nPlayed + " song" + p(nPlayed) + " on Spotify and added " + nPlaylisted
                    + " song" + p(nPlaylisted) + " to my playlists.";
        }
    },
    STEAM {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            return "I bought " + fl.size() + " game" + p(fl) + " on Steam.";
            // TODO update when more classes are finished
        }
    },
    REDDIT {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            int nPosts = 0;
            int nComments = 0;
            for (DiaryEntry e : fl) {
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
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            return "I made " + fl.size() + " edit" + p(fl) + " at Wikimedia.";
        }
    },
    YOUTUBE {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            return "I watched " + fl.size() + " video" + p(fl) + " on YouTube.";
            // TODO update when more classes are finished
        }
    },
    SPAN_BOUNDARY {
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            StringBuilder output = new StringBuilder(50);
            output.append("(Side note:");
            for (DiaryEntry e : fl) {
                DiaryEntrySpanBoundary d = (DiaryEntrySpanBoundary) e;
                output.append(" Today is the " + (d.IS_START ? "first" : "last") + " day I have information about "
                        + d.DESCRIPTION + ".");
            }
            output.append(")");
            return output.toString();
        }
    },
    FINANCE { //
        @Override
        public String describeInProse(ArrayList<DiaryEntry> fl) {
            int nSent = 0;
            int nRecieved = 0;
            for (DiaryEntry e : fl) {
                if (((MoneyTransaction) e).moneySent())
                    nSent++;
                else
                    nRecieved++;
            }
            String output = "";
            if (nSent > 0)
                output += "I bought and paid " + nSent + " thing" + p(nSent) + ". ";
            if (nRecieved > 0)
                output += "I received money " + nRecieved + " times" + p(nRecieved) + ".";
            return output;
        }
    };

    public abstract String describeInProse(ArrayList<DiaryEntry> filteredList);

    public final boolean enabled() {
        switch (this) {
            case SPAN_BOUNDARY:
                return false;
            default:
                return true;
        }
    }

    public final int customOrder() {
        switch (this) {
            case SPAN_BOUNDARY:
                return Integer.MAX_VALUE;
            case MANUAL:
                return Integer.MAX_VALUE - 1;
            default:
                return ordinal();
        }
    }

    @SuppressWarnings("rawtypes")
    private static String p(ArrayList v) {
        return p(v.size());
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