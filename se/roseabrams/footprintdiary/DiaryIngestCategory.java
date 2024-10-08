package se.roseabrams.footprintdiary;

public enum DiaryIngestCategory {
    CAMERA, RES, DISCORD, WHATSAPP, SPOTIFY, STEAM, APPLE_ACTIVITY, SKYPE, WIKIMEDIA, REDDIT, YOUTUBE, BANKING,
    APPLE_CALENDAR, FACEBOOK, MEDICAL, TINDER, EMAIL_HOTMAIL, EMAIL_GMAIL, TWITCH, WRITING, WORK_SCREENSHOTS,
    MIDJOURNEY, MYANIMELIST;

    /// quick swap for debug
    public boolean enabled() {
        switch (this) {
            default:
                return true;
        }
    }

    public String serializationFilename() {
        return "ingest " + this + ".ser";
    }
}
