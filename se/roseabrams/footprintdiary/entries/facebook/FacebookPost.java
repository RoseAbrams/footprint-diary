package se.roseabrams.footprintdiary.entries.facebook;

import se.roseabrams.footprintdiary.DiaryEntry;

public class FacebookPost extends FacebookWallEvent {

    public final String TEXT;

    public static enum Type {
        POST, PHOTO, VIDEO, LINK
    }
}
