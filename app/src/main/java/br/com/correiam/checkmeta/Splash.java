package br.com.correiam.checkmeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.FacebookSdk;

/**
 * Created by Misael Correia on 24/04/15.
 * misaelsco@gmail.com
 */
public class Splash extends Activity {
    static final int TIME_SPLASH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FacebookSdk.sdkInitialize(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splash = new Intent(Splash.this, Login.class);
                Splash.this.startActivity(splash);
                Splash.this.finish();
            }
        }, TIME_SPLASH);
    }
}
