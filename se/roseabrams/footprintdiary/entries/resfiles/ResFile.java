package se.roseabrams.footprintdiary.entries.resfiles;

import java.io.File;
import java.util.ArrayList;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.LocalContent;
import se.roseabrams.footprintdiary.interfaces.ContentOwner;

public class ResFile extends DiaryEntry implements ContentOwner {

    public final LocalContent CONTENT;
    private static final String RES_PATH = "D:\\Dropbox\\Public\\res\\";

    public ResFile(DiaryEntryCategory source, DiaryDateTime dd, File file) {
        super(source, dd);
        assert source == DiaryEntryCategory.MEME_SAVED || source == DiaryEntryCategory.MEME_CREATED
                || source == DiaryEntryCategory.WALLPAPER_SAVED || source == DiaryEntryCategory.ARTWORK_SAVED
                || source == DiaryEntryCategory.OTHER_MEMESQUE_SAVED;
        CONTENT = new LocalContent(file);
    }

    public String getName() {
        return CONTENT.getName();
    }

    public String getType() {
        return CONTENT.FILE_EXTENSION;
    }

    public String getSubfolder() {
        return CONTENT.FILE.getAbsolutePath().substring(RES_PATH.length(),
                CONTENT.FILE.getAbsolutePath().indexOf("\\", RES_PATH.length()));
    }

    @Override
    public String getStringSummary() {
        return getName();
    }

    @Override
    public Content getContent() {
        return CONTENT;
    }

    public static DiaryEntry[] createFromFiles() {
        ArrayList<DiaryEntry> output = createFromFilesRecursion(new File(RES_PATH), 1, null);
        return output.toArray(new DiaryEntry[output.size()]);
    }

    private static ArrayList<DiaryEntry> createFromFilesRecursion(File folder, int depth, String subfolder) {
        ArrayList<DiaryEntry> output = new ArrayList<>();
        for (File file : folder.listFiles()) {
            if (depth == 1) {
                subfolder = file.getName();
            }
            if (file.isDirectory()) {
                output.addAll(createFromFilesRecursion(file, depth + 1, subfolder));
            } else {
                DiaryDateTime dd = new DiaryDateTime(file.lastModified());
                DiaryEntryCategory c;
                switch (subfolder) {
                    case "lol":
                        c = DiaryEntryCategory.MEME_SAVED;
                        break;
                    case "oc":
                        c = DiaryEntryCategory.MEME_CREATED;
                        break;
                    case "wp":
                        c = DiaryEntryCategory.WALLPAPER_SAVED;
                        break;
                    case "anart":
                        c = DiaryEntryCategory.ARTWORK_SAVED;
                        break;
                    case "t":
                        c = DiaryEntryCategory.TORRENT;
                        break;
                    case "ai":
                    case "fl":
                    case "mus":
                    case "snd":
                    case "omgl":
                        continue;
                    default:
                        c = DiaryEntryCategory.OTHER_MEMESQUE_SAVED;
                        break;
                }
                DiaryEntry r = new ResFile(c, dd, file);
                output.add(r);
            }
        }
        return output;
    }
}
