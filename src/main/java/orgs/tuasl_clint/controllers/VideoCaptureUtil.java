package orgs.tuasl_clint.controllers;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

public class VideoCaptureUtil {
    private VideoCapture camera;
    private OpenCVFrameConverter.ToMat converter;

    public VideoCaptureUtil() {
        camera = new VideoCapture(0); // الكاميرا رقم 0 (الافتراضية)
        converter = new OpenCVFrameConverter.ToMat();
    }

    public Frame grabFrame() {
        Mat mat = new Mat();
        if (camera.read(mat)) {
            return converter.convert(mat);
        }
        return null;
    }

    public void release() {
        camera.release();
    }
}