package com.zihadrizkyef.oou;

import android.app.NotificationManager;
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
import com.zihadrizkyef.oou.helper.api.ApiHelper;
import com.zihadrizkyef.oou.helper.api.OouApiClient;
import com.zihadrizkyef.oou.model.Chat;
import com.zihadrizkyef.oou.model.SendChat;
import com.zihadrizkyef.oou.model.SetChatReaded;
import com.zihadrizkyef.oou.model.SetRoomReaded;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_ChatRoom extends AppCompatActivity {
    static final String BROADCAST_FILTER_NEWMESSAGE = "Activity_ChatRoom.receiverNewMessage.newMessage";
    static final String BROADCAST_FILTER_EDITMESSAGE = "Activity_ChatRoom.receiverNewMessage.editMessage";
    static final String BROADCAST_FILTER_READEDMESSAGE = "Activity_ChatRoom.receiverNewMessage.readedMessage";
    static final String BROADCAST_FILTER_READEDROOM = "Activity_ChatRoom.receiverNewMessage.readedRoom";

    BroadcastReceiver receiverNewMessage;
    BroadcastReceiver receiverEditMessage;
    BroadcastReceiver receiverReadedMessage;
    BroadcastReceiver receiverReadedRoom;

    RVAChat rvaChat;
    RecyclerView rvChat;
    List<Chat> chatRowList;

    OouApiClient apiClient;

    int id;
    int roomId;
    String roomName;

    int offsetChat = 0;
    int limitChatLoad = 30;
    boolean isloadingChat = false;
    boolean allChatIsLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        String shrPrfName = getString(R.string.shared_pref_name);
        SharedPreferences sharedPreferences = getSharedPreferences(shrPrfName, MODE_PRIVATE);
        id = sharedPreferences.getInt("id", -1);
        roomId = getIntent().getIntExtra("roomId", -1);
        roomName = getIntent().getStringExtra("roomName");
        getSupportActionBar().setTitle(roomName);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(roomId);

        apiClient = ApiHelper.getOouApiClient();

        createBroadcastReceiver();

        setUpRecyclerView();

        setUpTextInputSendChat();

        setAllRecepientChatReaded();

        loadChatFromServer();
        isloadingChat = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerBroadcastReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterBroadcastReceiver();
    }

    private void loadChatFromServer() {
        Call<List<Chat>> listCall = apiClient.chatRowList(roomId, limitChatLoad, offsetChat);
        listCall.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                isloadingChat = false;
                if (response.isSuccessful()) {
                    List<Chat> chatFromServer = response.body();
                    chatRowList.clear();
                    chatRowList.addAll(chatFromServer);
                    rvaChat.notifyDataSetChanged();
                    rvChat.scrollToPosition(chatRowList.size() - 1);
                    offsetChat += chatFromServer.size();

                    Log.i("first offset", "" + offsetChat);

                    if (chatRowList.size() < limitChatLoad) {
                        allChatIsLoaded = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                isloadingChat = false;
                Toast.makeText(Activity_ChatRoom.this, "Server error", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void loadChatMoreFromServer() {
        Call<List<Chat>> listCall = apiClient.chatRowList(roomId, limitChatLoad, offsetChat);
        listCall.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                isloadingChat = false;
                if (response.isSuccessful()) {
                    List<Chat> chatFromServer = response.body();
                    chatRowList.addAll(0, chatFromServer);
                    rvaChat.notifyItemRangeInserted(0, chatFromServer.size());
                    offsetChat += chatFromServer.size();
                    Log.i("more offset", "" + offsetChat);

                    if (chatFromServer.size() < limitChatLoad) {
                        allChatIsLoaded = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                isloadingChat = false;
                Toast.makeText(Activity_ChatRoom.this, "Server error", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void setUpRecyclerView() {
        rvChat = findViewById(R.id.rvChat);
        chatRowList = new ArrayList<>();
        final LinearLayoutManager lyManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvaChat = new RVAChat(this, chatRowList);
        rvChat.setLayoutManager(lyManager);
        rvChat.setAdapter(rvaChat);
        rvChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (lyManager.findFirstVisibleItemPosition() == 0 && !isloadingChat) {
                    loadChatMoreFromServer();
                    isloadingChat = true;
                }
            }
        });
    }

    private void setUpTextInputSendChat() {
        final EditText etTextInput = findViewById(R.id.etTextInput);
        ImageButton ibTextSend = findViewById(R.id.ibTextSend);
        ibTextSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etTextInput.getText().toString();
                if (!text.equals("")) {
                    chatRowList.add(new Chat(0, text, id, roomId, "", 0, ""));
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

    private void createBroadcastReceiver() {
        receiverNewMessage = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Chat chat = new Chat(
                        intent.getIntExtra("id", -1),
                        intent.getStringExtra("message"),
                        intent.getIntExtra("senderId", -1),
                        intent.getIntExtra("chatRoomId", -1),
                        intent.getStringExtra("imageUrl"),
                        intent.getIntExtra("readed", -1),
                        intent.getStringExtra("createdAt")
                );
                chatRowList.add(chat);
                rvaChat.notifyItemInserted(chatRowList.size() - 1);
                rvChat.scrollToPosition(chatRowList.size() - 1);

                updateChatReaded(chat.getId());
            }
        };

        receiverReadedMessage = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int index = getIndexChatWhereId(intent.getIntExtra("id", -1));
                chatRowList.get(index).setReaded(1);
                rvaChat.notifyItemChanged(index);
            }
        };

        receiverReadedRoom = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (roomId == intent.getIntExtra("roomId", -1)) {
                    for (int i = 0; i < chatRowList.size(); i++) {
                        if (chatRowList.get(i).getSenderId() == id) {
                            chatRowList.get(i).setReaded(1);
                            rvaChat.notifyItemChanged(i);
                        }
                    }
                }
            }
        };

        receiverEditMessage = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (roomId == intent.getIntExtra("roomId", -1)) {
                    int index = getIndexChatWhereId(intent.getIntExtra("id", -1));
                    chatRowList.get(index).setMessage(intent.getStringExtra("message"));
                    rvaChat.notifyItemChanged(index);
                }
            }
        };
    }

    private void registerBroadcastReceiver() {
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(
                        receiverNewMessage,
                        new IntentFilter(BROADCAST_FILTER_NEWMESSAGE)
                );
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(
                        receiverReadedMessage,
                        new IntentFilter(BROADCAST_FILTER_READEDMESSAGE)
                );
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(
                        receiverEditMessage,
                        new IntentFilter(BROADCAST_FILTER_EDITMESSAGE)
                );
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(
                        receiverReadedRoom,
                        new IntentFilter(BROADCAST_FILTER_READEDROOM)
                );
    }

    private void unregisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverNewMessage);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverEditMessage);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverReadedMessage);
    }

    private void setAllRecepientChatReaded() {
        String shrPrfName = getString(R.string.shared_pref_name);
        SharedPreferences sharedPreferences = getSharedPreferences(shrPrfName, MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);
        apiClient.setRoomReaded(id, roomId).enqueue(new Callback<SetRoomReaded>() {
            @Override
            public void onResponse(Call<SetRoomReaded> call, Response<SetRoomReaded> response) {

            }

            @Override
            public void onFailure(Call<SetRoomReaded> call, Throwable t) {

            }
        });
    }

    private void updateChatReaded(int chatId) {
        apiClient.setChatReaded(chatId).enqueue(new Callback<SetChatReaded>() {
            @Override
            public void onResponse(Call<SetChatReaded> call, Response<SetChatReaded> response) {

            }

            @Override
            public void onFailure(Call<SetChatReaded> call, Throwable t) {

            }
        });
    }

    private int getIndexChatWhereId(int id) {
        for (int i = 0; i < chatRowList.size(); i++) {
            if (chatRowList.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
