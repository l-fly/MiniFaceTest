package immotor.com.minifacetest.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import immotor.com.minifacetest.FileUtil;

public class FaceView extends BaseCameraView {

    private static final String TAG = "ObjectDetectingView";


    private MatOfRect mObject;

    private FaceDetector mFaceDetector;

    @Override
    public void onOpenCVLoadSuccess() {
        Log.i(TAG, "onOpenCVLoadSuccess: ");

        mObject = new MatOfRect();

       // mObjectDetects = new ArrayList<>();
    }

    @Override
    public void onOpenCVLoadFail() {
        Log.i(TAG, "onOpenCVLoadFail: ");
    }

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    long faceTime = 0;
    boolean needPhotoTaken = true;
    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        // 子线程（非UI线程）
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        Rect[] faceArray = mFaceDetector.detectObject(mGray, mObject);
        if (faceArray != null && faceArray.length > 0) {

            final Rect rect = faceArray[0];

            if(     rect.width < 300 && rect.width > 50
                    && rect.height < 300 && rect.height > 50
                    && rect.x > 150 && rect.x + rect.width < 500
                    && rect .y > 100 && rect .y + rect.height < 450){
                long interval = System.currentTimeMillis() - faceTime;
                faceTime = System.currentTimeMillis();
                if (interval < 100  && needPhotoTaken) {
                    needPhotoTaken = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String phoneName = System.currentTimeMillis() + ".jpg";
                            boolean b = FileUtil.saveMat(mRgba,rect,phoneName);
                            Log.i("#####  b", "" +  b);

                        }
                    }).start();

                }else {
                    Imgproc.rectangle(mRgba, rect.tl(), rect.br(), mFaceDetector.getRectColor(), 3);
                }

            }

            // Log.i("#####", new Gson().toJson(rect));

        }

        return mRgba;
    }

    public void setFaceDetector(FaceDetector faceDetector){
        mFaceDetector = faceDetector;
    }

}
