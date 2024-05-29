package ja1.entries.camera;

import java.io.File;

import ja1.DiaryDate;
import ja1.interfaces.Video;

public class CameraVideo extends CameraCapture implements Video {

    public CameraVideo(DiaryDate date, File file) {
        super(date, file);
    }
}
