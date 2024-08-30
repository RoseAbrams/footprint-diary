package se.roseabrams.footprintdiary;

public enum DiaryIngestCategory {
    CAMERA, RES, DISCORD, WHATSAPP, SPOTIFY, STEAM, APPLE_ACTIVITY, SKYPE, WIKIMEDIA, REDDIT, YOUTUBE, BANKING,
    APPLE_CALENDAR, FACEBOOK, MEDICAL, TINDER, EMAIL_HOTMAIL, EMAIL_GMAIL, TWITCH, WRITING, WORK_SCREENSHOTS,
    MIDJOURNEY;

    public String serializationFilename() {
        return "ingest " + this + ".ser";
    }
}
