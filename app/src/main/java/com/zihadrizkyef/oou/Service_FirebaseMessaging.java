package com.zihadrizkyef.oou;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zihadrizkyef.oou.helper.api.ApiHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 01/08/17.
 */

public class Service_FirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();

        switch (data.get("action")) {
            case "chat_new":
                try {
                    ShortcutBadger.applyCount(this, Integer.parseInt(data.get("unreadedMessageCount")));

                    JSONObject message = new JSONObject(data.get("msg"));

                    if (CustomLifeCycleCallback.isApplicationInForeground()) {
                        Intent intent = new Intent(Activity_ChatRoom.BROADCAST_FILTER_NEWMESSAGE);

                        intent.putExtra("id", message.getInt("id"));
                        intent.putExtra("message", message.getString("message"));
                        intent.putExtra("senderId", message.getInt("sender_id"));
                        intent.putExtra("chatRoomId", message.getInt("chat_room_id"));
                        intent.putExtra("imageUrl", message.getString("image_url"));
                        intent.putExtra("readed", message.getInt("readed"));
                        intent.putExtra("createdAt", message.getString("created_at"));

                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    } else {
                        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
                        Intent intentMain = new Intent(this, Activity_Main.class);
                        Intent intentChat = new Intent(this, Activity_ChatRoom.class);
                        intentChat.putExtra("roomId", message.getInt("chat_room_id"));
                        intentChat.putExtra("roomName", data.get("title"));
                        PendingIntent pendingIntent = PendingIntent.getActivities(
                                this,
                                0,
                                new Intent[]{intentMain, intentChat},
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                        mBuilder.setContentTitle(data.get("title"))
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .setContentText(message.getString("message"))
                                .setGroup(message.getString("chat_room_id"))
                                .setSmallIcon(R.drawable.ic_notif_oou)
                                .setContentIntent(pendingIntent)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setVibrate(new long[]{0, 300, 75, 300});

                        try {
                            if (!message.getString("image_url").equals("")) {
                                Bitmap bitmap = Glide.with(getApplicationContext())
                                        .load(ApiHelper.API_BASE_URL + message.getString("image_url"))
                                        .asBitmap()
                                        .into((int) (getResources().getDisplayMetrics().density * 64), (int) (getResources().getDisplayMetrics().density * 64))
                                        .get();
                                mBuilder.setLargeIcon(bitmap);
                            } else {
                                Bitmap bitmap = Bitmap.createScaledBitmap(
                                        BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile_picture),
                                        (int) (getResources().getDisplayMetrics().density * 64),
                                        (int) (getResources().getDisplayMetrics().density * 64),
                                        true);
                                mBuilder.setLargeIcon(bitmap);
                            }
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(message.getInt("chat_room_id"), mBuilder.build());
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;


            case "chat_readed":
                try {
//                    ShortcutBadger.applyCount(this, Integer.parseInt(data.get("unreadedMessageCount")));
                    JSONObject message = new JSONObject(data.get("msg"));
                    if (CustomLifeCycleCallback.isApplicationInForeground()) {
                        Intent intent = new Intent(Activity_ChatRoom.BROADCAST_FILTER_READEDMESSAGE);
                        intent.putExtra("id", message.getInt("id"));
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;


            case "room_readed":
//                ShortcutBadger.applyCount(this, Integer.parseInt(data.get("unreadedMessageCount")));
                if (CustomLifeCycleCallback.isApplicationInForeground()) {
                    Intent intent = new Intent(Activity_ChatRoom.BROADCAST_FILTER_READEDROOM);
                    intent.putExtra("roomId", Integer.parseInt(data.get("room_id")));
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }
                break;


            case "edit_chat":
                try {
                    JSONObject message = new JSONObject(data.get("msg"));
                    if (CustomLifeCycleCallback.isApplicationInForeground()) {
                        Intent intent = new Intent(Activity_ChatRoom.BROADCAST_FILTER_EDITMESSAGE);
                        intent.putExtra("id", message.getInt("id"));
                        intent.putExtra("message", message.getString("message"));
                        intent.putExtra("roomId", message.getInt("chat_room_id"));
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}