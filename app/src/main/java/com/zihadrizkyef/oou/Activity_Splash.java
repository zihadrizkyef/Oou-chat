package com.zihadrizkyef.oou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity_Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = getIntent();
        if (intent.hasExtra("msgCode")) {
            String msgCode = intent.getStringExtra("msgCode");
            if (msgCode.equals("newChat")) {
                intent.removeExtra("msgCode");

                try {
                    JSONObject message = new JSONObject(intent.getStringExtra("msg"));
                    Intent intentToChat = new Intent(this, Activity_ChatRoom.class);

                    intentToChat.putExtra("id", message.getInt("id"));
                    intentToChat.putExtra("message", message.getString("message"));
                    intentToChat.putExtra("senderId", message.getInt("sender_id"));
                    intentToChat.putExtra("chatRoomId", message.getInt("chat_room_id"));
                    intentToChat.putExtra("imageUrl", message.getString("image_url"));
                    intentToChat.putExtra("readed", message.getInt("readed"));
                    intentToChat.putExtra("createdAt", message.getString("created_at"));

                    startActivity(intentToChat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String shrPrfName = getString(R.string.shared_pref_name);
        SharedPreferences sharedPreferences = getSharedPreferences(shrPrfName, MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);

        if (id == -1) {
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    Intent intentToLogin = new Intent(Activity_Splash.this, Activity_Login.class);
                    startActivity(intentToLogin);
                    finish();
                }
            }, 1000);
        } else {
            Intent intentToMain = new Intent(this, Activity_Main.class);
            startActivity(intentToMain);
            finish();
        }
    }
}
