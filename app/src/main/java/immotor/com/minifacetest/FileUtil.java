package immotor.com.minifacetest;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    public static boolean saveMat(Mat frameData, Rect rect,String name) {
        boolean isSave;
        Bitmap bitmap = Bitmap.createBitmap(frameData.width(), frameData.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(frameData, bitmap);
        bitmap = cropBitmapCustom(bitmap,rect);
        String filePath = Environment.getExternalStorageDirectory()+ File.separator + "face" ;
        Log.i("####",filePath);
        File dir = new File(filePath);
        if(!dir.exists()){
            dir.mkdir();
        }
        FileOutputStream outputStream = null;
        try {
            String fileName = dir.toString() + File.separator + name;
            outputStream = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            isSave = true;
        } catch (IOException e) {
            isSave = false;
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSave;
    }

    public static Bitmap cropBitmapCustom(Bitmap srcBitmap,Rect rect) {
        int firstPixelX = 0;
        int firstPixelY = 0;
        int add = 40;
        if(rect.x > add){
            firstPixelX = rect.x -add;
        }
        if(rect.y > add){
            firstPixelY = rect.y -add;
        }
        int needWidth = rect.width + add*2;
        int needHeight = rect.height + add *2;
        if(firstPixelX + needWidth > srcBitmap.getWidth()){
            needWidth = srcBitmap.getWidth() - firstPixelX;
        }
        if(firstPixelY + needHeight > srcBitmap.getHeight()){
            needHeight = srcBitmap.getHeight() - firstPixelY;
        }
        /**裁剪关键步骤*/
        Bitmap cropBitmap = Bitmap.createBitmap(srcBitmap, firstPixelX, firstPixelY, needWidth, needHeight);

        return cropBitmap;
    }
}
