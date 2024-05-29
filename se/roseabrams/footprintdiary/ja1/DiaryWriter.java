package ja1;

import java.io.File;
import java.io.IOException;

import ja1.entries.camera.CameraCapture;
import ja1.entries.discord.DiscordMessage;

public class DiaryWriter {
    public static final short MY_CUTOFF_YEAR = 2023;

    public static void main(String[] args) {
        DiaryBook d = new DiaryBook(new DiaryDate((short) 2012, (byte) 1, (byte) 1),
                new DiaryDate((short) 2021, (byte) 12, (byte) 31));

        try {
            d.add(CameraCapture.createFromLog(new File("/data/camera Camera.txt")));
            // d.add(CameraCapture.createFromLog(new File("/data/camera Kina.txt"))); //
            // dating will be harder with mixed modifieddate, maybe there's other metadata?
            d.add(DiscordMessage.createAllFromCsv(new File("/data/discord/messages")));
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
    }
}
