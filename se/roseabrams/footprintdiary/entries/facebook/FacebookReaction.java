package se.roseabrams.footprintdiary.entries.facebook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.PersonalConstants;

public class FacebookReaction extends FacebookWallEvent {

    public final Reaction REACTION;
    public final String PARENT_OP;
    public final FacebookPost.Type PARENT_TYPE;

    public FacebookReaction(DiaryDateTime date, Reaction reaction, String op, FacebookPost.Type opType) {
        super(date);
        assert op == null || !op.isBlank();
        assert opType != null;
        REACTION = reaction;
        PARENT_OP = op != null ? op.intern() : null;
        PARENT_TYPE = opType;
    }

    @Override
    public String getStringSummary() {
        return REACTION.emoji();
    }

    public static enum Reaction {

        LIKE {
            @Override
            public String emoji() {
                return "👍";
            }
        },
        LOVE {
            @Override
            public String emoji() {
                return "❤";
            }
        },
        CARE {
            @Override
            public String emoji() {
                return "🤗";
            }
        },
        HAHA {
            @Override
            public String emoji() {
                return "😆";
            }
        },
        WOW {
            @Override
            public String emoji() {
                return "😲";
            }
        },
        SAD {
            @Override
            public String emoji() {
                return "😢";
            }
        },
        ANGRY {
            @Override
            public String emoji() {
                return "😠";
            }
        },
        DOROTHY {
            @Override
            public String emoji() {
                return "🌼";
            }
        };

        public abstract String emoji();
    }

    public static FacebookReaction[] createFromHtml(File reaction) throws IOException {
        ArrayList<FacebookReaction> output = new ArrayList<>();
        Document d = Jsoup.parse(reaction);
        Elements reactionsE = d.select("div._a706 > div._3-95._a6-g");
        for (Element reactionE : reactionsE) {
            String description = reactionE.selectFirst("div._a6-h._a6-i").text();
            String dateS = reactionE.selectFirst("div._3-94._a6-o div._a72d").text();
            String op;
            String opTypeS;
            if (description.contains(" own ")) {
                op = PersonalConstants.FACEBOOK_NAME;
                opTypeS = description.substring(description.lastIndexOf(" ") + 1, description.lastIndexOf("."));
            } else if (description.contains(" a ") || description.contains(" an ")) {
                // OP is deleted or accessdenied
                op = null;
                opTypeS = description.substring(description.lastIndexOf(" ") + 1, description.lastIndexOf("."));
            } else {
                int opIndexStart = -1;
                if (description.contains("liked "))
                    opIndexStart = description.indexOf("liked ") + "liked ".length();
                else
                    opIndexStart = description.indexOf("reacted to ") + "reacted to ".length();
                int opIndexEnd = description.lastIndexOf("'s ");
                op = description.substring(opIndexStart, opIndexEnd);
                opTypeS = description.substring(opIndexEnd + "'s ".length(), description.lastIndexOf("."));
            }
            String reactionL = reactionE.selectFirst("div._2ph_._a6-p img").attr("src");
            String reactionS = reactionL.substring(reactionL.lastIndexOf("/") + 1, reactionL.lastIndexOf("."));
            if (reactionS.equals("none"))
                reactionS = "like"; // go default
            if (reactionS.equals("sorry"))
                reactionS = "sad";
            if (reactionS.equals("anger"))
                reactionS = "angry";

            FacebookReaction f = new FacebookReaction(parseDate(dateS), Reaction.valueOf(reactionS.toUpperCase()),
                    op, FacebookPost.Type.parse(opTypeS.replace(" ", "_")));
            output.add(f);
        }
        return output.toArray(new FacebookReaction[output.size()]);
    }
}
