package immotor.com.minifacetest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.kongqw.listener.OnOpenCVLoadListener;
import com.yanzhenjie.sofia.Sofia;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Scalar;

import java.util.List;

import immotor.com.minifacetest.View.FaceDetector;
import immotor.com.minifacetest.View.FaceView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "####";
    private FaceView faceView;
    ImageView imgProCircle,imgProLine,imgFaceArea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sofia.with(this).statusBarBackgroundAlpha(0).statusBarDarkFont();
        /*getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/
        setContentView(R.layout.layout);


        boolean isAvilible =  isAvilible(this,"org.opencv.engine");
        if(isAvilible){

        }else {
            finish();
        }
        faceView = (FaceView) findViewById(R.id.faceView);
        faceView.setOnOpenCVLoadListener(new OnOpenCVLoadListener() {
            @Override
            public void onOpenCVLoadSuccess() {
                FaceDetector mFaceDetector = new FaceDetector(MainActivity.this, R.raw.haarcascade_frontalface_alt,
                        10, 0.2F, 0.2F, new Scalar(255,143,50,0.1));
                faceView.setFaceDetector(mFaceDetector);
            }

            @Override
            public void onOpenCVLoadFail() {

            }

            @Override
            public void onNotInstallOpenCVManager() {

            }
        });
        faceView.loadOpenCV(getApplicationContext());

        imgFaceArea = (ImageView)findViewById(R.id.img_face_area);


        ViewTreeObserver vto = imgFaceArea.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imgFaceArea.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int[] location = new  int[2] ;
                imgFaceArea.getLocationOnScreen(location);
                float x = imgFaceArea.getX();
                float y = imgFaceArea.getY();
                int width = imgFaceArea.getWidth();
                int height = imgFaceArea.getHeight();


                Log.i("###", " x:" + location[0] + " y:" + location[1] +" width:" + width +" height:" + height );
            }
        });



        animation();

    }

    private void animation(){
        imgProCircle = (ImageView)findViewById(R.id.img_pro_circle);
        imgProLine = (ImageView)findViewById(R.id.img_pro_line);
        rotateView(imgProCircle);

        translate(imgProLine);
    }
    private void translate(View view){
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.translate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        view.startAnimation(operatingAnim);

    }
    private void rotateView(View view){
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        view.startAnimation(operatingAnim);
    }
    private boolean isAvilible(Context context, String packageName ) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for ( int i = 0; i < pinfo.size(); i++ ) {
            if(pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }
}
