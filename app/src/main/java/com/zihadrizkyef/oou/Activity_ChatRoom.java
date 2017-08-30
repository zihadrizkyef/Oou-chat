package com.zihadrizkyef.oou;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.zihadrizkyef.oou.adapter.RVAChat;
import com.zihadrizkyef.oou.helper.ApiHelper;
import com.zihadrizkyef.oou.helper.OouApiClient;
import com.zihadrizkyef.oou.model.Chat;

import java.util.ArrayList;
import java.util.List;

public class Activity_ChatRoom extends AppCompatActivity {
    RVAChat rvaChat;
    RecyclerView rvChat;
    OouApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        String name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);

        rvChat = (RecyclerView) findViewById(R.id.rvChat);

        apiClient = ApiHelper.getApiClient();

        LinearLayoutManager lyManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvaChat = new RVAChat(this, null);
        rvChat.setLayoutManager(lyManager);
        rvChat.setAdapter(rvaChat);

        final EditText etTextInput = (EditText) findViewById(R.id.etTextInput);
        ImageButton ibTextSend = (ImageButton) findViewById(R.id.ibTextSend);
        ibTextSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etTextInput.getText().toString();
                if (!text.equals("")) {
//                    chats.add(new Chat(0, "Zihad", null, 0, text));
//                    rvaChat.notifyItemInserted(chats.size()-1);
//                    etTextInput.setText("");
//                    rvChat.scrollToPosition(chats.size()-1);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
