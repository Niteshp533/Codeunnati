package exportkit.xd.View.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;

import exportkit.xd.Controller.CameraController;
import exportkit.xd.Controller.UserController;
import exportkit.xd.DB.SessionManager;
import exportkit.xd.Model.User;
import exportkit.xd.R;
import exportkit.xd.View.IAppViews;
import exportkit.xd.View.Scanner.Camera;

public class EditProfile_activity extends Camera implements IAppViews {
    UserController userController;

    private TextView email, password, phone, name, username;
    private ImageButton editButton, hidden;
    private Button cancel ;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        userController = new UserController((IAppViews) this);
        cameraController = new CameraController(this);

        SessionManager session= new SessionManager(this);
        int loggedUserID = (int) session.getUserFromSession();
        User user= userController.getUser((int)loggedUserID);

        //find views
        uploadedImage= (CircularImageView)findViewById(R.id.avatar);
        email= (TextView) findViewById(R.id.editmail);
        password= (TextView) findViewById(R.id.editpass);
        phone = (TextView) findViewById(R.id.editphonenumber);
        name = (TextView) findViewById(R.id.editname);
        username = (TextView) findViewById(R.id.editusername);
        editButton = (ImageButton) findViewById(R.id.done);
        cancel = (Button)findViewById(R.id.cancel);
        hidden = (ImageButton) findViewById(R.id.pass);

        //set current information for logged user
        if(user.getAvatar() != null){
            uploadedImage.setImageURI(Uri.parse(user.getAvatar()));
            cameraController.imageUri= Uri.parse(user.getAvatar());
        }
        name.setText(user.getName());
        username.setText(user.getUsername());
        email.setText(user.getEmail()) ;
        password.setText(user.getPassword()) ;
        phone.setText(user.getPhoneNumber()) ;

        //Buttons
        uploadedImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //show image pick dialog
                cameraController.imagePickDialog();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Fullname = name.getText().toString();
                String Username = username.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString() ;
                String Phone = phone.getText().toString();
                if (Fullname.equalsIgnoreCase("")
                        || Username.equalsIgnoreCase("")
                        || Email.equalsIgnoreCase("")
                        || Password.equalsIgnoreCase("")
                        || Phone.equalsIgnoreCase(""))
                {
                    Toast.makeText(getApplication(),"you should fill the empty fields",Toast.LENGTH_LONG).show();
                }
                else
                {
                    User user = new User(Fullname, Username, Email, Phone, Password);
                    user.setId(loggedUserID);
                    user.setAvatar(""+ cameraController.imageUri);
                    userController.EditProfile(user);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Profile_activity.class);
                startActivity(nextScreen);
            }
        });

        hidden.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_UP:
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        password.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(getApplication(),message,Toast.LENGTH_LONG).show();
        Intent nextScreen = new Intent(getApplicationContext(), Profile_activity.class);
        startActivity(nextScreen);
    }
    @Override
    public void onError(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();

    }

}
