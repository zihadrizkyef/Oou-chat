package com.zihadrizkyef.oou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zihadrizkyef.oou.adapter.RVAChat;
import com.zihadrizkyef.oou.helper.ApiHelper;
import com.zihadrizkyef.oou.helper.OouApiClient;
import com.zihadrizkyef.oou.model.Chat;
import com.zihadrizkyef.oou.model.SendChat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_ChatRoom extends AppCompatActivity {
    static final String BROADCAST_FILTER = "Activity_ChatRoom.notifReceiver";
    RVAChat rvaChat;
    RecyclerView rvChat;
    List<Chat> chatRowList;
    OouApiClient apiClient;
    BroadcastReceiver notifReceiver;

    int id;
    int roomId;
    String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        String shrPrfName = getString(R.string.shared_pref_name);
        SharedPreferences sharedPreferences = getSharedPreferences(shrPrfName, MODE_PRIVATE);
        id = sharedPreferences.getInt("id", -1);
        roomId = getIntent().getIntExtra("roomId", -1);
        roomName = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(roomName);

        rvChat = (RecyclerView) findViewById(R.id.rvChat);

        chatRowList = new ArrayList<>();

        apiClient = ApiHelper.getApiClient();

        notifReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("frbs", "notifReceiver active");
                Chat chat = new Chat(
                        intent.getIntExtra("id", -1),
                        intent.getStringExtra("message"),
                        intent.getIntExtra("senderId", -1),
                        intent.getIntExtra("roomId", -1),
                        intent.getStringExtra("imageUrl"),
                        intent.getIntExtra("readed", -1),
                        intent.getStringExtra("createdAt")
                );
                chatRowList.add(chat);
                rvaChat.notifyItemInserted(chatRowList.size() - 1);
                rvChat.scrollToPosition(chatRowList.size() - 1);
            }
        };

        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(
                        notifReceiver,
                        new IntentFilter(BROADCAST_FILTER)
                );

        LinearLayoutManager lyManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvaChat = new RVAChat(this, chatRowList);
        rvChat.setLayoutManager(lyManager);
        rvChat.setAdapter(rvaChat);

        final EditText etTextInput = (EditText) findViewById(R.id.etTextInput);
        ImageButton ibTextSend = (ImageButton) findViewById(R.id.ibTextSend);
        ibTextSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etTextInput.getText().toString();
                if (!text.equals("")) {
                    chatRowList.add(new Chat(0, text, id, 0, "", roomId, ""));
                    rvaChat.notifyItemInserted(chatRowList.size() - 1);
                    rvChat.scrollToPosition(chatRowList.size() - 1);
                    etTextInput.setText("");

                    apiClient.sendChat(id, roomId, text).enqueue(new Callback<SendChat>() {
                        @Override
                        public void onResponse(Call<SendChat> call, Response<SendChat> response) {
                            if (response.isSuccessful()) {
                                SendChat chatFromServer = response.body();
                                Chat chat = chatRowList.get(chatRowList.size() - 1);
                                chat.setId(chatFromServer.getId());
                                chat.setMessage(chatFromServer.getMessage());
                                chat.setSenderId(chatFromServer.getSenderId());
                                chat.setChatRoomId(chatFromServer.getChatRoomId());
                                chat.setCreatedAt(chatFromServer.getCreatedAt());
                                chat.setReaded(chatFromServer.getReaded());
                            }
                        }

                        @Override
                        public void onFailure(Call<SendChat> call, Throwable t) {
                            t.printStackTrace();
                            Toast.makeText(Activity_ChatRoom.this, "Server error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Call<List<Chat>> listCall = apiClient.chatRowList(roomId, 20);
        listCall.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                if (response.isSuccessful()) {
                    chatRowList.clear();
                    chatRowList.addAll(response.body());
                    rvaChat.notifyDataSetChanged();
                    rvChat.scrollToPosition(chatRowList.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Toast.makeText(Activity_ChatRoom.this, "Server error", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

//        unregisterReceiver(notifReceiver);
    }
}
