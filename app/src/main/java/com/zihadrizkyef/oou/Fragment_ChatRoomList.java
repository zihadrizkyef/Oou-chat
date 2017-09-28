package com.zihadrizkyef.oou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zihadrizkyef.oou.adapter.RVAChatRoomList;
import com.zihadrizkyef.oou.helper.ApiHelper;
import com.zihadrizkyef.oou.helper.OouApiClient;
import com.zihadrizkyef.oou.model.ChatRoom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 05/07/17.
 */

public class Fragment_ChatRoomList extends Fragment {
    RecyclerView rvChatList;
    RVAChatRoomList rvaChatRoomList;

    OouApiClient apiClient;
    BroadcastReceiver notifReceiver;
    List<ChatRoom> chatRoomList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_list, container, false);

        chatRoomList = new ArrayList<>();

        rvaChatRoomList = new RVAChatRoomList(getActivity(), chatRoomList);
        rvChatList = (RecyclerView) rootView.findViewById(R.id.rvChatList);
        rvChatList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvChatList.setAdapter(rvaChatRoomList);

        apiClient = ApiHelper.getOouApiClient();

        createChatNotifReceiver();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getChatRoomList();
        LocalBroadcastManager
                .getInstance(getActivity())
                .registerReceiver(notifReceiver, new IntentFilter(Activity_ChatRoom.BROADCAST_FILTER_NEWMESSAGE));
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager
                .getInstance(getActivity())
                .unregisterReceiver(notifReceiver);
    }

    private void createChatNotifReceiver() {
        notifReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int chatRoomId = intent.getIntExtra("chatRoomId", -1);
                int indexChatRoom = getIndexChatRoomWhereId(chatRoomId);
                ChatRoom chatRoom = chatRoomList.get(indexChatRoom);
                chatRoom.setMessage(intent.getStringExtra("message"));
                chatRoom.setNotReaded(chatRoom.getNotReaded() + 1);
                chatRoomList.remove(indexChatRoom);
                rvaChatRoomList.notifyItemMoved(indexChatRoom, 0);
                rvaChatRoomList.notifyItemChanged(0);
                rvChatList.scrollToPosition(0);

                Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone rtg = RingtoneManager.getRingtone(getActivity(), notificationSound);
                rtg.play();

                updateUnreadToTablayout();
            }
        };
    }

    private void getChatRoomList() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getString(R.string.shared_pref_name), MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);
        apiClient.chatRoomList(id).enqueue(new Callback<List<ChatRoom>>() {
            @Override
            public void onResponse(Call<List<ChatRoom>> call, Response<List<ChatRoom>> response) {
                if (response.isSuccessful()) {
                    chatRoomList.clear();
                    chatRoomList.addAll(response.body());
                    Collections.sort(chatRoomList, new Comparator<ChatRoom>() {
                        @Override
                        public int compare(ChatRoom o1, ChatRoom o2) {
                            return o2.getLastActivity().compareTo(o1.getLastActivity());
                        }
                    });
                    rvaChatRoomList.setChatRooms(chatRoomList);
                    updateUnreadToTablayout();
                } else {
                    Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatRoom>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getIndexChatRoomWhereId(int id) {
        for (int i = 0; i < chatRoomList.size(); i++) {
            if (chatRoomList.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private void updateUnreadToTablayout() {
        int manyRoomUnread = 0;
        for (int i = 0; i < chatRoomList.size(); i++) {
            if (chatRoomList.get(i).getNotReaded() > 0) {
                manyRoomUnread++;
            }
        }
        ((Activity_Main) getActivity()).setChatUnread(manyRoomUnread);
    }
}
