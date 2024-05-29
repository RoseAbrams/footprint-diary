package ja1.entries.camera;

import java.io.File;

import ja1.DiaryDate;
import ja1.interfaces.Picture;

public class CameraPicture extends CameraCapture implements Picture {

    public CameraPicture(DiaryDate date, File file) {
        super(date, file);
    }
}
