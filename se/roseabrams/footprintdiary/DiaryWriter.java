package se.roseabrams.footprintdiary;

import java.io.File;
import java.io.IOException;

import se.roseabrams.footprintdiary.entries.camera.CameraCapture;

public class DiaryWriter {
    public static final short MY_CUTOFF_YEAR = 2023;

    public static void main(String[] args) {
        DiaryBook d = new DiaryBook(new DiaryDate((short) 2012, (byte) 1, (byte) 1),
                new DiaryDate((short) 2021, (byte) 12, (byte) 31));

        try {
            d.add(CameraCapture.create(new File("/data/camera captures.txt")));
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
    }
}
