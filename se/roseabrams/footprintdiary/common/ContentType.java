package se.roseabrams.footprintdiary.common;

public enum ContentType {

    PLAINTEXT, PICTURE, VIDEO, AUDIO, GIF, SYSTEM, BOOK, DOCUMENT, TORRENT, COMICS, WEBPAGE, APPLICATION;

    public static ContentType parseExtension(String ext) {
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
            case "heic":
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
                return BOOK;
            case "doc":
            case "docx":
            case "xls":
            case "xlsx":
            case "ppt":
            case "pptx":
                return DOCUMENT;
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
            case "html":
            case "htm":
                return WEBPAGE;
            case "exe":
                return APPLICATION;
            default:
                System.err.println("Unrecognized filetype extension: " + ext.toLowerCase());
                return null;
        }
    }
}
