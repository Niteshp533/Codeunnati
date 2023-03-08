package exportkit.xd.View.Scanner;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.opencv.android.CameraBridgeViewBase;

import exportkit.xd.Controller.CameraController;

import static exportkit.xd.DB.Constants.CameraConstants.CAMERA_REQUEST_CODE;
import static exportkit.xd.DB.Constants.CameraConstants.IMAGE_PICK_CAMERA_CODE;
import static exportkit.xd.DB.Constants.CameraConstants.IMAGE_PICK_GALLERY_CODE;
import static exportkit.xd.DB.Constants.CameraConstants.STORAGE_REQUEST_CODE;

public abstract class Camera extends AppCompatActivity {
    public CameraController cameraController;
    public CircularImageView uploadedImage; //in view/ xml file
    public CameraBridgeViewBase mOpenCvCameraView; //live camera ML

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //result of permission allowed/denied
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    //if allowed return true else false
                    boolean cameraAccepted= grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted= grantResults[1]== PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted){
                        //both permission allowed
                        cameraController.pickFromCamera();
                    }
                    else{
                        Toast.makeText(this, "Camera & Storage permissions are required...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    //if allowed return true else false
                    boolean storageAccepted= grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        //storage permission allowed
                        cameraController.pickFromGallery();
                    }
                    else{
                        Toast.makeText(this, "Storage permissions is required...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        //image picked from camera or gallery will be received here
        if(resultCode == RESULT_OK){
            //image is picked
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //picked from gallery
                //crop image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //picked from camera
                //crop image
                CropImage.activity(cameraController.imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                //cropped image received
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){
                    cameraController.imageUri= result.getUri();
                    //set image
                    uploadedImage.setImageURI(cameraController.imageUri);
                }
                else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    //error
                    Exception error= result.getError();
                    Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mOpenCvCameraView !=null){
            mOpenCvCameraView.disableView();
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mOpenCvCameraView !=null){
            mOpenCvCameraView.disableView();
        }

    }


}
