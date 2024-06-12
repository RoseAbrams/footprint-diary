package se.roseabrams.footprintdiary.entries.facebook;

public class FacebookReaction extends FacebookWallEvent {

    public final ReactionType TYPE;
    public final String PARENT_OP;

    public static enum ReactionType {

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
}
