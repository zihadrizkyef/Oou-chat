package com.zihadrizkyef.oou;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zihadrizkyef.oou.helper.ApiHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 01/08/17.
 */

public class Service_FirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.i("notifz", "onMessageC");

        Map<String, String> data = remoteMessage.getData();

        if (data.get("action").equals("new_chat")) {
            Log.i("notifz", "if");
            try {
                JSONObject message = new JSONObject(data.get("msg"));

                if (CustomLifeCycleCallback.isApplicationInForeground()) {
                    Log.i("notifz", "foreground");
                    Intent intent = new Intent(Activity_ChatRoom.BROADCAST_FILTER);

                    intent.putExtra("id", message.getInt("id"));
                    intent.putExtra("message", message.getString("message"));
                    intent.putExtra("senderId", message.getInt("sender_id"));
                    intent.putExtra("chatRoomId", message.getInt("chat_room_id"));
                    intent.putExtra("imageUrl", message.getString("image_url"));
                    intent.putExtra("readed", message.getInt("readed"));
                    intent.putExtra("createdAt", message.getString("created_at"));

                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                } else {
                    Log.i("notifz", "else");
                    final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
                    Intent intent = new Intent(this, Activity_Splash.class);
                    PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentTitle(data.get("title"))
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setContentText(message.getString("message"))
                            .setGroup(message.getString("chat_room_id"))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pendingIntent)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setVibrate(new long[]{0, 300, 75, 300});

                    try {
                        Log.i("scrsize", String.valueOf(getResources().getDisplayMetrics().density));
                        Bitmap bitmap = Glide.with(getApplicationContext())
                                .load(ApiHelper.API_BASE_URL + message.getString("image_url"))
                                .asBitmap()
                                .into((int) (getResources().getDisplayMetrics().density * 64), (int) (getResources().getDisplayMetrics().density * 64))
                                .get();
                        mBuilder.setLargeIcon(bitmap);
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(Integer.valueOf(message.getString("chat_room_id")), mBuilder.build());
                        Log.i("notifz", "notify");
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (data.get("action").equals("edit_chat")) {
        }
    }
}