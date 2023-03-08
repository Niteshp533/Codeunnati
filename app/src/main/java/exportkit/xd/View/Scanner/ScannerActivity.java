package exportkit.xd.View.Scanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.ArrayList;

import exportkit.xd.Controller.CameraController;
import exportkit.xd.Controller.MLModelController;
import exportkit.xd.R;
import exportkit.xd.View.Homepage_activity;

public class ScannerActivity extends Camera implements CameraBridgeViewBase.CvCameraViewListener2{
    private static final String TAG="Homepage_activity";
    CameraController cameraController;
    private Mat mRgba;
    Button back, doneBtn ;

    MLModelController MLModelController;
    private BaseLoaderCallback mLoaderCallback =new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface
                        .SUCCESS:{
                    Log.i(TAG,"OpenCv Is loaded");
                    mOpenCvCameraView.enableView();
                }
                default:
                {
                    super.onManagerConnected(status);

                }
                break;
            }
        }
    };

    public ArrayList<String> ingredients;

    public ScannerActivity(){
        Log.i(TAG,"Instantiated new "+this.getClass());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        cameraController= new CameraController(this);
        cameraController.openLiveCamera();
        setContentView(R.layout.activity_camera);
        mOpenCvCameraView=(CameraBridgeViewBase) findViewById(R.id.frame_Surface);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        back = (Button)findViewById(R.id.back_button);
        doneBtn = (Button)findViewById(R.id.done_button);

        ingredients = new ArrayList<String>() ;

        try{
            // input size is 300 for this model
            MLModelController =new MLModelController(getAssets(), "detect.tflite", "classes.txt",300);
            Log.d(TAG,"Model is successfully loaded");
        }
        catch (IOException e){
            Log.d(TAG,"Getting some error");
            e.printStackTrace();
        }
        doneBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ingredients= MLModelController.predictions;
                Intent nextScreen = new Intent(getApplicationContext(), TrackIngredients_activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("list",ingredients.toString());
                nextScreen.putExtras(bundle);
                startActivity(nextScreen);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Homepage_activity.class);
                startActivity(nextScreen);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()){
            //if load success
            Log.d(TAG,"Opencv initialization is done");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else{
            //if not loaded
            Log.d(TAG,"Opencv is not loaded. try again");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0,this,mLoaderCallback);
        }
    }
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
        mRgba=inputFrame.rgba();
        Mat out=new Mat();
        out= MLModelController.recognizeImage(mRgba);
        return out;
    }
    @Override
    public void onCameraViewStarted(int width ,int height){
        mRgba=new Mat(height,width, CvType.CV_8UC4);
    }
    @Override
    public void onCameraViewStopped(){
        mRgba.release();
    }


}