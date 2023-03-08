package exportkit.xd.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import exportkit.xd.R;
import exportkit.xd.View.Register.Login_activity;
import exportkit.xd.View.Register.SignUp_activity;

public class CoverPage_activity extends Activity {

    private Button signup;

    private Button login;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.coverpage);

        //find views
        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.login);

        signup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), SignUp_activity.class);
                startActivity(nextScreen);


            }
        });
        login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Login_activity.class);
                startActivity(nextScreen);
            }
        });

        //custom code goes here

    }
}

