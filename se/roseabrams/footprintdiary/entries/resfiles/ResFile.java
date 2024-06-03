package se.roseabrams.footprintdiary.entries.resfiles;

import java.io.File;
import java.util.ArrayList;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.interfaces.LocalResource;

public abstract class ResFile extends DiaryEntry implements LocalResource {

    public final File FILE;
    private static final String RES_PATH = "D:\\Dropbox\\Public\\res\\";

    public ResFile(DiaryEntryCategory source, DiaryDateTime dd, File file) {
        super(source, dd);
        assert source == DiaryEntryCategory.MEME_SAVED || source == DiaryEntryCategory.MEME_CREATED
                || source == DiaryEntryCategory.WALLPAPER_SAVED || source == DiaryEntryCategory.ARTWORK_SAVED
                || source == DiaryEntryCategory.OTHER_MEMESQUE_SAVED;
        FILE = file;
    }

    public String getName() {
        return FILE.getName();
    }

    public String getType() {
        return FILE.getName().substring(FILE.getName().lastIndexOf(".")+1);
    }

    public String getSubfolder() {
        return FILE.getAbsolutePath().substring(RES_PATH.length(),
                FILE.getAbsolutePath().indexOf("\\", RES_PATH.length()));
    }

    @Override
    public String getStringSummary() {
        return FILE.getName();
    }

    @Override
    public String getPathToResource() {
        return FILE.getAbsolutePath();
    }

    @Override
    public File getFileOfResource() {
        return FILE;
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
                String filetype = file.getName().substring(file.getName().lastIndexOf(".")+1);
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
                DiaryEntry r;
                switch (filetype.toLowerCase()) {
                    case "jpg":
                    case "jpeg":
                    case "png":
                    case "webp":
                        r = new ResPicture(c, dd, file);
                        break;
                    case "mp4":
                    case "mov":
                    case "webm":
                        r = new ResVideo(c, dd, file);
                        break;
                    case "gif":
                        r = new ResGif(c, dd, file);
                        break;
                    case "torrent":
                        r = new Torrent(dd, file);
                        break;
                    default:
                        System.err.println("Unrecognized filetype: " + filetype);
                        continue;
                }
                output.add(r);
            }
        }
        return output;
    }

    /*
     * public static DiaryEntry[] createFromLog(File logfile) throws IOException {
     * ArrayList<DiaryEntry> output = new ArrayList<>(50000);
     * Scanner s = new Scanner(logfile);
     * while (s.hasNextLine()) {
     * Scanner sFile = new Scanner(s.nextLine());
     * sFile.useDelimiter(":");
     * String filename = sFile.next();
     * String filetype = filename.substring(filename.lastIndexOf('.') + 1);
     * long timestamp = sFile.nextLong();
     * DiaryDateTime dd = new DiaryDateTime(timestamp);
     * Scanner sPath = new Scanner(sFile.next());
     * sPath.useDelimiter("\\");
     * sPath.next(); // first is blank
     * String subfolderName = sPath.next();
     * if (subfolderName.equals("mus") || subfolderName.equals("snd") ||
     * subfolderName.equals("omgl")
     * || subfolderName.equals("oo") || subfolderName.equals("fl") ||
     * subfolderName.equals("ai")) {
     * continue;
     * }
     * 
     * DiaryEntryCategory subfolderSource;
     * switch (subfolderName) {
     * case "lol":
     * subfolderSource = DiaryEntryCategory.MEME_SAVED;
     * break;
     * case "oc":
     * subfolderSource = DiaryEntryCategory.MEME_CREATED;
     * break;
     * case "wp":
     * subfolderSource = DiaryEntryCategory.WALLPAPER_SAVED;
     * break;
     * case "anart":
     * subfolderSource = DiaryEntryCategory.ARTWORK_SAVED;
     * break;
     * case "t":
     * subfolderSource = DiaryEntryCategory.TORRENT;
     * break;
     * default:
     * subfolderSource = DiaryEntryCategory.OTHER_MEMESQUE_SAVED;
     * break;
     * }
     * 
     * DiaryEntry r;
     * switch (filetype.toLowerCase()) {
     * case "jpg":
     * case "jpeg":
     * case "png":
     * case "webp":
     * r = new ResPicture(subfolderSource, dd, file);
     * break;
     * case "mp4":
     * case "mov":
     * case "webm":
     * r = new ResVideo(subfolderSource, dd, file);
     * break;
     * case "gif":
     * r = new ResGif(subfolderSource, dd, file);
     * break;
     * case "torrent":
     * r = new Torrent(dd, file);
     * break;
     * default:
     * System.err.println("Unrecognized filetype: " + filetype);
     * continue;
     * }
     * 
     * output.add(r);
     * }
     * s.close();
     * return output.toArray(new DiaryEntry[output.size()]);
     * }
     */
}
