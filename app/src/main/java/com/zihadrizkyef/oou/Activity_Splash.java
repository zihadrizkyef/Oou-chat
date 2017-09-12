package com.zihadrizkyef.oou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Activity_Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String shrPrfName = getString(R.string.shared_pref_name);
        SharedPreferences sharedPreferences = getSharedPreferences(shrPrfName, MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);

        if (id == -1) {
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    Intent intent = new Intent(Activity_Splash.this, Activity_Login.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        } else {
            Intent intent = new Intent(this, Activity_Main.class);
            startActivity(intent);
            finish();
        }
    }
}
