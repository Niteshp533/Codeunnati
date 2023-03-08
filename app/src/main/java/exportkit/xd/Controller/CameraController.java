package exportkit.xd.Controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import exportkit.xd.DB.Constants.CameraConstants;

public class CameraController{
    Activity view;
    CameraConstants constants = new CameraConstants();

    //arrays of permissions
    public String[] cameraPermissions; //camera and storage
    public String[] storagePermissions; //storage only
    //variables (will contain data to save)
    public Uri imageUri;

    //----------------------------------------------------------------------------------------------

    public CameraController(Activity view) {
        this.view= view;

        /*** init permission array */
        cameraPermissions= new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    public void imagePickDialog(){
        //options to display in dialogs
        String[] options= {"Camera", "Gallery"};
        //dialog
        AlertDialog.Builder builder= new AlertDialog.Builder(this.view);
        //title
        builder.setTitle("Pick Image From");
        //set items/options
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(which==0){
                    //camera clicked
                    if(!checkCameraPermission())
                        requestCameraPermission();
                    else
                        pickFromCamera();
                }
                else if(which==1){
                    if(!checkStoragePermission())
                        requestStoragePermission();
                    else
                        pickFromGallery();
                }
            }
        });
        //create/show dialog
        builder.create().show();
    }

    public void openLiveCamera(){
        if(!checkCameraPermission())
            requestCameraPermission();
    }

    //---------------------------------------------------------------------------------------------

    public boolean checkStoragePermission(){
        boolean result= ContextCompat.checkSelfPermission((Context)this.view, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    public void requestStoragePermission(){
        ActivityCompat.requestPermissions(this.view, storagePermissions, constants.STORAGE_REQUEST_CODE);
    }

    public boolean checkCameraPermission(){
        boolean result= ContextCompat.checkSelfPermission((Context)this.view,Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission((Context)this.view,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    public void requestCameraPermission(){
        ActivityCompat.requestPermissions(this.view, cameraPermissions, constants.CAMERA_REQUEST_CODE);
    }

    //---------------------------------------------------------------------------------------------

    public void pickFromCamera(){
        //intent to pick image from camera, the image will be returned in onActivityResults method
        ContentValues values= new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image Description");
        //put image uri
        imageUri= view.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //intent to open camera
        Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        view.startActivityForResult(cameraIntent, constants.IMAGE_PICK_CAMERA_CODE);

    }

    public void pickFromGallery(){
        //intent to pick image from gallery, the image will be returned in onActivityResults method
        Intent galleryIntent= new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*"); //we want only images
        view.startActivityForResult(galleryIntent, constants.IMAGE_PICK_GALLERY_CODE);
    }

    //----------------------------------------------------------------------------------------------

}
