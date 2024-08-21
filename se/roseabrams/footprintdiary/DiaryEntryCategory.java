package se.roseabrams.footprintdiary;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import se.roseabrams.footprintdiary.common.ContentContainer;
import se.roseabrams.footprintdiary.common.Message;
import se.roseabrams.footprintdiary.common.MoneyTransaction;
import se.roseabrams.footprintdiary.entries.apple.DailyActivity;
import se.roseabrams.footprintdiary.entries.facebook.FacebookComment;
import se.roseabrams.footprintdiary.entries.facebook.FacebookFriend;
import se.roseabrams.footprintdiary.entries.facebook.FacebookPost;
import se.roseabrams.footprintdiary.entries.facebook.FacebookReaction;
import se.roseabrams.footprintdiary.entries.medical.MedicalRecord;
import se.roseabrams.footprintdiary.entries.reddit.RedditComment;
import se.roseabrams.footprintdiary.entries.reddit.RedditPost;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlayback;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlaylisting;

public enum DiaryEntryCategory { // categorization intent is for human displaying

    DISCORD {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            return "I sent " + fl.size() + " message" + p(fl) + " on Discord.";
        }
    },
    TORRENT {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            return "I started downloading " + fl.size() + " torrent" + p(fl) + ".";
        }
    },
    CAMERA {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            int nPictures = 0;
            int nVideos = 0;
            for (DiaryEntry e : fl) {
                ContentContainer co = (ContentContainer) e;
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
            output.append("I used my camera ").append(nPictures + nVideos).append(" time").append(p(fl))
                    .append(". I took ");
            if (nPictures > 0)
                output.append(nPictures).append(" photo" + p(nPictures) + ", ");
            if (nVideos > 0)
                output.append(nVideos).append(" video" + p(nVideos) + ", ");
            String outputS = output.substring(0, output.length() - 3);
            outputS += ".";
            return outputS;
        }
    },
    MEME_SAVED {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            return "I saved " + fl.size() + " meme" + p(fl) + " that I found online.";
        }
    },
    MEME_CREATED {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            return "I made " + fl.size() + " meme" + p(fl) + " and published " + (fl.size() == 1 ? "it" : "them")
                    + " online.";
        }
    },
    WALLPAPER_SAVED {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            return "I saved " + fl.size() + " wallpaper" + p(fl) + " that I found online.";
        }
    },
    ARTWORK_SAVED {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            return "I saved " + fl.size() + " piece" + p(fl) + " of arwork that I found online.";
        }
    },
    OTHER_MEMESQUE_SAVED {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            return "I saved " + fl.size() + " other thing" + p(fl) + " that I found online but never sorted.";
        }
    },
    HEALTH {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            return "I did " + fl.size() + " healthy thing" + p(fl)
                    + ", but don't quite remember what. They're a bit of a mess.";
        }
    },
    DAILY_HEALTH {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            float km = ((DailyActivity) fl.get(0)).kmWalked();
            return "I walked " + km + " kilometer" + p(Math.round(km)) + ".";
        }
    },
    MANUAL {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            return "Finally, I wanna note this event today: " + fl.get(0);
        }
    },
    WHATSAPP {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            int[] n = countMessages(fl);
            return "I sent " + n[0] + " message" + p(n[0]) + " on WhatsApp, and received " + n[1] + ".";
        }
    },
    SKYPE {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            int[] n = countMessages(fl);
            return "I sent " + n[0] + " message" + p(n[0]) + " on Skype, and received " + n[1] + ".";
        }
    },
    SPOTIFY {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            int nPlayed = 0;
            int nPlaylisted = 0;
            for (DiaryEntry e : fl) {
                if (e instanceof SpotifyPlayback)
                    nPlayed++;
                else if (e instanceof SpotifyPlaylisting)
                    nPlaylisted++;
            }
            return "I listened to " + nPlayed + " song" + p(nPlayed) + " on Spotify and added " + nPlaylisted + " song"
                    + p(nPlaylisted) + " to my playlists.";
        }
    },
    STEAM {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            return "I bought " + fl.size() + " game" + p(fl) + " on Steam.";
            // TODO update when more classes are finished
        }
    },
    REDDIT {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
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
        public String describeInProse(List<DiaryEntry> fl) {
            return "I made " + fl.size() + " edit" + p(fl) + " at Wikimedia projects.";
        }
    },
    YOUTUBE {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            return "I watched " + fl.size() + " video" + p(fl) + " on YouTube.";
            // TODO update when more classes are finished
        }
    },
    SPAN_BOUNDARY {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            StringBuilder output = new StringBuilder(50);
            output.append("By the way...");
            for (DiaryEntry e : fl) {
                DiaryEntrySpanBoundary d = (DiaryEntrySpanBoundary) e;
                output.append(" Today is the " + (d.IS_START ? "first" : "last") + " day I have information about "
                        + d.DESCRIPTION + ".");
            }
            return output.toString();
        }
    },
    FINANCE {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
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
                output += "I received money " + nRecieved + " time" + p(nRecieved) + ".";
            return output;
        }
    },
    CALENDAR {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            return "I went through " + fl.size() + " item" + p(fl) + " on my calendar.";
        }
    },
    FACEBOOK_WALL {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            int nPosts = 0;
            int nComments = 0;
            int nReactions = 0;
            for (DiaryEntry e : fl) {
                if (e instanceof FacebookPost)
                    nPosts++;
                else if (e instanceof FacebookComment)
                    nComments++;
                else if (e instanceof FacebookReaction)
                    nReactions++;
            }

            StringBuilder output = new StringBuilder(40);
            output.append("I did ").append(fl.size()).append(" interaction").append(p(fl))
                    .append(" on Facebook. I made ");
            if (nPosts > 0)
                output.append(nPosts).append(" post" + p(nPosts) + ", ");
            if (nComments > 0)
                output.append(nComments).append(" comments" + p(nComments) + ", ");
            if (nReactions > 0)
                output.append(nReactions).append(" reactions" + p(nReactions) + ", ");
            output.substring(0, output.length() - 3);
            output.append('.');
            return output.toString();
        }
    },
    FACEBOOK_MESSAGE {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            int[] n = countMessages(fl);
            return "I sent " + n[0] + " message" + p(n[0]) + " on Facebook, and received " + n[1] + ".";
        }
    },
    FACEBOOK_FRIEND {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            int[] n = { 0, 0, 0, 0, 0 };
            for (DiaryEntry e : fl) {
                n[((FacebookFriend) e).STATUS.ordinal()]++;
            }

            assert n[0] > 0; // temporary and might not happen, it's just hard to briefly describe the other types of events
            return "I added " + n[0] + " new friend" + p(n[0]) + " on Facebook.";
        }
    },
    WEB_HISTORY {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            return "I visited " + fl.size() + " website" + p(fl) + ".";
        }
    },
    EMAIL {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            int[] n = countMessages(fl);
            String output = "I recieved " + n[1] + " email" + p(n[1]);
            if (n[0] > 0)
                output += " and sent " + n[0];
            return output + ".";
        }
    },
    MEDICAL {
        @Override
        public String describeInProse(List<DiaryEntry> fl) {
            StringBuilder output = new StringBuilder(100);
            output.append("I interacted with healthcare ").append(fl.size()).append(" time").append(p(fl)).append(",");
            TreeSet<String> providers = new TreeSet<>();
            TreeSet<String> authors = new TreeSet<>();
            for (DiaryEntry e : fl) {
                providers.add(((MedicalRecord) e).PROVIDER);
                authors.add(((MedicalRecord) e).AUTHOR);
            }
            output.append(" at ");
            if (providers.size() == 1)
                output.append(providers.first());
            else
                output.append(providers.size()).append(" different places");
            output.append(" with ");
            if (authors.size() == 1)
                output.append(authors.first());
            else
                output.append(authors.size()).append(" different people");
            output.append(".");
            return output.toString();
        }
    };

    public abstract String describeInProse(List<DiaryEntry> filteredList);

    /// quick debug swapping of what's being printed
    public final boolean enabled() {
        switch (this) {
            case SPAN_BOUNDARY:
            case WEB_HISTORY:
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

    public static int[] countMessages(List<DiaryEntry> l) {
        int[] output = { 0, 0 };
        for (DiaryEntry e : l) {
            assert e instanceof Message;
            if (((Message) e).isByMe())
                output[0]++;
            else
                output[1]++;
        }
        return output;
    }

    @SuppressWarnings("rawtypes")
    private static String p(List v) {
        return p(v.size());
    }

    /// plural ending of nouns
    private static String p(int v) {
        return v == 1 ? "" : "s";
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