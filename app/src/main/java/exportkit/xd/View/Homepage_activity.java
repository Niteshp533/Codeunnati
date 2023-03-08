package exportkit.xd.View;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import org.opencv.android.OpenCVLoader;

import exportkit.xd.Controller.UserController;
import exportkit.xd.DB.SessionManager;
import exportkit.xd.Model.User;
import exportkit.xd.R;
import exportkit.xd.View.Profile.Profile_activity;
import exportkit.xd.View.Recipe.AddRecipe_activity;
import exportkit.xd.View.Scanner.ScannerActivity;
import exportkit.xd.View.Search.SearchUser_activity;

public class Homepage_activity extends Activity implements IAppViews{
	private TextView name;
	private ImageButton addRecipeButton, cameraButton;
	private CircularImageView ProfileButton;
	private Button SearchButton;

	UserController userController;
	static {
		if(OpenCVLoader.initDebug()){
			Log.d("Homepage_activity : ","Opencv is loaded");
		}
		else {
			Log.d("Homepage_activity : ","Opencv failed to load");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);

		SessionManager session= new SessionManager(this);
		long loggedUserID= session.getUserFromSession();
		if(loggedUserID>0) {

			userController = new UserController(this);
			User user = userController.getUser((int) loggedUserID);


			name = (TextView) findViewById(R.id.hello);
			addRecipeButton = (ImageButton) findViewById(R.id.addrecipebutton);
			cameraButton = findViewById(R.id.openCamera);
			ProfileButton = findViewById(R.id.profile1);
			SearchButton = (Button) findViewById(R.id.ellipse_ek22);


			//set current information for logged user
			if (user.getAvatar() != null) {
				ProfileButton.setImageURI(Uri.parse(user.getAvatar()));
			}
			name.setText("Hello " + user.getName());

			ProfileButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent nextScreen = new Intent(getApplicationContext(), Profile_activity.class);
					startActivity(nextScreen);
				}
			});
			SearchButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent nextScreen = new Intent(getApplicationContext(), SearchUser_activity.class);
					startActivity(nextScreen);
					Toast.makeText(getApplication(), "Now You are On Search User Mode", Toast.LENGTH_LONG).show();

				}
			});
			addRecipeButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent nextScreen = new Intent(getApplicationContext(), AddRecipe_activity.class);
					startActivity(nextScreen);
				}
			});
			cameraButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					startActivity(new Intent(getApplication(), ScannerActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

				}
			});
		}
		else{
			Intent nextScreen = new Intent(getApplicationContext(), CoverPage_activity.class);
			startActivity(nextScreen);
		}
	}

	@Override
	public void onSuccess(String message) {
		Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
	}
	@Override
	public void onError(String message) {
		Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
	}

}
	
	