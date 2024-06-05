package se.roseabrams.footprintdiary;

public enum Filetype {

    PLAINTEXT, PICTURE, VIDEO, AUDIO, GIF, SYSTEM, DOCUMENT_A, DOCUMENT_B, TORRENT, COMICS;

    public static Filetype parseExtension(String ext) {
        switch (ext.toLowerCase()) {
            case "jpg":
            case "jpeg":
            case "png":
            case "webp":
            case "jfif":
            case "svg":
            case "psd":
            case "ai":
            case "bmp":
                return PICTURE;
            case "mp4":
            case "mov":
            case "webm":
            case "ts":
            case "ogv":
            case "mkv":
            case "avi":
                return VIDEO;
            case "gif":
                return GIF;
            case "torrent":
                return TORRENT;
            case "ini":
            case "url":
            case "lnk":
                return SYSTEM;
            case "txt":
                return PLAINTEXT;
            case "pdf":
            case "epub":
            case "mobi":
            case "djvu":
                return DOCUMENT_A;
            case "doc":
            case "docx":
            case "xls":
            case "xlsx":
            case "ppt":
            case "pptx":
                return DOCUMENT_B;
            case "mp3":
            case "flac":
            case "ogg":
            case "m3u":
            case "wav":
            case "mid":
            case "oga":
            case "m3u8":
                return AUDIO;
            case "cbr":
            case "cbz":
                return COMICS;
            default:
                System.err.println("Unrecognized filetype extension: " + ext);
                return null;
        }
    }
}
