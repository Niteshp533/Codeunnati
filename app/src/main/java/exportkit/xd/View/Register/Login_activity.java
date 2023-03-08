package exportkit.xd.View.Register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import exportkit.xd.Controller.UserController;
import exportkit.xd.R;
import exportkit.xd.View.Homepage_activity;
import exportkit.xd.View.IAppViews;

public class Login_activity extends Activity implements IAppViews {

	UserController userController;
	private TextView email, password,
			loginb, signUpb;
	private ImageView hidden;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		userController = new UserController(this);
		email= (TextView) findViewById(R.id.email_or_username);
		password = (TextView) findViewById(R.id.password);

		loginb = (TextView) findViewById(R.id.login_ek1);
		signUpb= (TextView) findViewById(R.id._sign_up_ek2);
		hidden = (ImageButton) findViewById(R.id.hide);

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

		signUpb.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent nextScreen = new Intent(getApplicationContext(), SignUp_activity.class);
				startActivity(nextScreen);

			}
		});
		loginb.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String Email = email.getText().toString();
				String Pass = password.getText().toString() ;
				if (Email.equalsIgnoreCase("")
						|| Pass.equalsIgnoreCase(""))
				{
					Toast.makeText(getApplication(),"you should fill the empty fields",Toast.LENGTH_LONG).show();

				}
				else
				{
					System.out.println(Email +  "  " + Pass) ;
					userController.login(Email,Pass);
				}
			}
		});
	}

	@Override
	public void onSuccess(String message) {
		Toast.makeText(getApplication(),message,Toast.LENGTH_LONG).show();
		Intent nextScreen = new Intent(getApplicationContext(), Homepage_activity.class);
		startActivity(nextScreen);
	}
	@Override
	public void onError(String message) {
		Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
	}

}
	
	