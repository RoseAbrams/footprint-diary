package se.roseabrams.footprintdiary.common;

public enum ContentType {

    PLAINTEXT, PICTURE, VIDEO, AUDIO, GIF, SYSTEM, BOOK, DOCUMENT, TORRENT, COMICS, WEBPAGE, APPLICATION, ARCHIVE, DATA,
    MODEL, FONT, CODE, CONTACT, MISC_PROPRIETARY;

    public static ContentType parseExtension(String ext) {
        return parseExtension(ext, true);
    }

    public static ContentType parseExtension(String ext, boolean allowNull) {
        if (ext.contains("/") || ext.contains("\\"))
            throw new IllegalArgumentException(
                    "Filename contains seperator character(s) and is thus clearly incorrectly ingested");
        String extNormalized = ext.toLowerCase();
        switch (extNormalized) {
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
            case "ps":
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
            case "flv":
                return VIDEO;
            case "gif":
                return GIF;
            case "3mf":
                return MODEL;
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
            case "prc":
            case "azw3":
                return BOOK;
            case "doc":
            case "docx":
            case "xls":
            case "xlsx":
            case "ppt":
            case "pptx":
            case "rtf":
            case "fdxt":
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
            case "pkf":
            case "musx":
            case "opus":
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
            case "msi":
                return APPLICATION;
            case "zip":
            case "rar":
            case "7z":
            case "iso":
            case "daa":
                return ARCHIVE;
            case "json":
            case "csv":
            case "srt":
            case "log":
            case "xspf":
                return DATA;
            case "ttf":
            case "otf":
                return FONT;
            case "java":
            case "cpp":
                return CODE;
            case "vcf":
                return CONTACT;
            case "ggb": // Geogebra
            case "epr": // Adobe export preset
            case "stl": // Google Earth
            case "kml": // Google Earth
            case "sc2replay":
            case "sc2map":
            case "civ5mod":
                return MISC_PROPRIETARY; // so specialized that they're not worth understanding further
            default:
                if (allowNull) {
                    if (extNormalized.equals("glocalnet") || extNormalized.equals("wp")
                            || extNormalized.equals("demon") || extNormalized.equals("kdbx"))
                        // so obscure that it feels verbose to create a category
                        System.err.println("Known uncategorized filetype extension: " + ext);
                    else
                        System.err.println("Unrecognized filetype extension: " + ext);
                    return null;
                } else {
                    throw new IllegalArgumentException(
                            "Unrecognized filetype extension (null disallowed): " + ext);
                }
        }
    }
}
