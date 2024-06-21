package se.roseabrams.footprintdiary.common;

public enum ContentType {

    PLAINTEXT, PICTURE, VIDEO, AUDIO, GIF, SYSTEM, BOOK, DOCUMENT, TORRENT, COMICS, WEBPAGE, APPLICATION, ARCHIVE, DATA;

    public static ContentType parseExtension(String ext) {
        return parseExtension(ext, true);
    }

    public static ContentType parseExtension(String ext, boolean allowNull) {
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
            case "tif":
                return PICTURE;
            case "mp4":
            case "mov":
            case "webm":
            case "ts":
            case "ogv":
            case "mkv":
            case "avi":
            case "hevc":
            case "m4r":
                return VIDEO;
            case "gif":
                return GIF;
            case "torrent":
                return TORRENT;
            case "ini":
            case "url":
            case "lnk":
            case "bat":
            case "cmd":
            case "search-ms":
            case "xmp":
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
            case "rtf":
                return DOCUMENT;
            case "mp3":
            case "flac":
            case "ogg":
            case "m3u":
            case "wav":
            case "mid":
            case "oga":
            case "m3u8":
            case "aiff":
            case "m4a":
                return AUDIO;
            case "cbr":
            case "cbz":
                return COMICS;
            case "html":
            case "htm":
            case "textile":
                return WEBPAGE;
            case "exe":
            case "swf":
                return APPLICATION;
            case "zip":
            case "rar":
            case "7z":
            case "iso":
                return ARCHIVE;
            case "json":
            case "csv":
            case "srt":
            case "log":
                return DATA;
            default:
                if (allowNull) {
                    System.err.println("Unrecognized filetype extension: " + ext.toLowerCase());
                    return null;
                } else {
                    throw new IllegalArgumentException(
                            "Unrecognized filetype extension (null disallowed): " + ext);
                }
        }
    }
}
