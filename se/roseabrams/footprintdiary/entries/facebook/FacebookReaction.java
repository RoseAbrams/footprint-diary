package se.roseabrams.footprintdiary.entries.facebook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.roseabrams.footprintdiary.DiaryDateTime;

public class FacebookReaction extends FacebookWallEvent {
    /*
     * "likes_and_reactions_1.html",
     * "likes_and_reactions_2.html",
     * "likes_and_reactions_3.html"
     */

    public final Type TYPE;
    public final String PARENT_OP;

    public FacebookReaction(DiaryDateTime date, String op, Type type) {
        super(date);
        PARENT_OP = op.intern();
        TYPE = type;
    }

    public static enum Type {

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
        };

        public abstract String emoji();
    }

    public static FacebookReaction[] createFromHtml(File reaction) throws IOException {
        ArrayList<FacebookReaction> output = new ArrayList<>();
        Document d = Jsoup.parse(reaction);
        Elements reactionsE = d.select("div._a706 > div._3-95._a6-g");
        for (Element reactionE : reactionsE) {
            String opES = reactionE.selectFirst("div._a6-h._a6-i").text();
            String dateS = reactionE.selectFirst("div._3-94._a6-o > div._a72d").text();
            int opIndexStart = -1;
            if (opES.contains("liked ")) {
                opIndexStart = opES.indexOf("liked ") + "liked ".length();
            } else {
                opIndexStart = opES.indexOf("reacted to ") + "reacted to ".length();
            }
            int opIndexEnd = opES.lastIndexOf("'s ");
            String op = opES.substring(opIndexStart, opIndexEnd);
            String typeS = opES.substring(opIndexEnd + "'s ".length(), opES.lastIndexOf("."));
            output.add(new FacebookReaction(parseDate(dateS), op, Type.valueOf(typeS.toUpperCase())));
        }
        return output.toArray(new FacebookReaction[output.size()]);
    }
}
