package entries.camera;

import java.io.File;

import DiaryDate;
import interfaces.Picture;

public class CameraPicture extends CameraCapture implements Picture {

    public CameraPicture(DiaryDate date, File file) {
        super(date, file);
    }
}
