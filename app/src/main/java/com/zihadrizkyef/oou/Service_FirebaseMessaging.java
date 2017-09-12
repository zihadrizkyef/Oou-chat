package com.zihadrizkyef.oou;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 01/08/17.
 */

public class Service_FirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("frbs", "onMessageReceived()");

        Map<String, String> data = remoteMessage.getData();

        if (data.get("msgCode").equals("newChat")) {
            try {
                JSONObject message = new JSONObject(data.get("msg"));

                Intent intent = new Intent(Activity_ChatRoom.BROADCAST_FILTER);

                intent.putExtra("id", message.getInt("id"));
                intent.putExtra("message", message.getString("message"));
                intent.putExtra("senderId", message.getInt("sender_id"));
                intent.putExtra("chatRoomId", message.getInt("chat_room_id"));
                intent.putExtra("imageUrl", message.getString("image_url"));
                intent.putExtra("readed", message.getInt("readed"));
                intent.putExtra("createdAt", message.getString("created_at"));

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
