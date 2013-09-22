package de.ur.mi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;

public class SplashActivty extends Activity {

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivty.this, LoginSignupActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}
