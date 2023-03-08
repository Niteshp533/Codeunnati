	package exportkit.xd.View.Register;

	import android.app.Activity;
	import android.content.Intent;
	import android.os.Bundle;
	import android.text.InputType;
	import android.view.MotionEvent;
	import android.view.View;
	import android.widget.ImageView;
	import android.widget.RadioButton;
	import android.widget.RadioGroup;
	import android.widget.TextView;
	import android.widget.Toast;

	import exportkit.xd.Controller.UserController;
	import exportkit.xd.Model.User;
	import exportkit.xd.R;
	import exportkit.xd.View.Homepage_activity;
	import exportkit.xd.View.IAppViews;
	public class SignUp_activity extends Activity implements IAppViews{

		UserController userController;
		private TextView email, password, phone, name, username,signUpb;
		private RadioGroup radioSexGroup;
		private RadioButton radioSexButton;
		private ImageView hidden;
		@Override
		public void onCreate(Bundle savedInstanceState) {

			super.onCreate(savedInstanceState);
			setContentView(R.layout.signup);
			userController = new UserController(this);

			email= (TextView) findViewById(R.id.email_address);
			password= (TextView) findViewById(R.id.password_ek1);
			phone = (TextView) findViewById(R.id.phonenumber);
			name = (TextView) findViewById(R.id.name);
			username = (TextView) findViewById(R.id.username);
			signUpb = (TextView) findViewById(R.id.sign_up_ek6);
			radioSexGroup = (RadioGroup) findViewById(R.id.groupbutton);
			hidden = (ImageView) findViewById(R.id.hide);

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
					String Fullname = name.getText().toString();
					String Username = username.getText().toString();
					String Email = email.getText().toString();
					String Password = password.getText().toString();
					String Phone = phone.getText().toString();
					// get selected radio button from radioGroup
					int selectedId = radioSexGroup.getCheckedRadioButtonId();
					if (selectedId < 0) {
						Toast.makeText(getApplication(), "you should fill the empty fields", Toast.LENGTH_LONG).show();
					} else {
						// find the radiobutton by returned id
						radioSexButton = (RadioButton) findViewById(selectedId);

						String gender = radioSexButton.getText().toString();
						System.out.println("Gender = " + gender);
						if (Fullname.equalsIgnoreCase("")
								|| Username.equalsIgnoreCase("")
								|| Email.equalsIgnoreCase("")
								|| Password.equalsIgnoreCase("")
								|| Phone.equalsIgnoreCase("")
						) {
							Toast.makeText(getApplication(), "you should fill the empty fields", Toast.LENGTH_LONG).show();

						} else {
							User newUser = new User(Fullname, Username, Email, Phone, Password, gender);
							userController.signUp(newUser);

						}

					}}

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
	
	