package com.zihadrizkyef.oou;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_list, container, false);

        rvaChatRoomList = new RVAChatRoomList(getActivity(), null);
        rvChatList = (RecyclerView) rootView.findViewById(R.id.rvChatList);
        rvChatList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvChatList.setAdapter(rvaChatRoomList);

        apiClient = ApiHelper.getApiClient();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getString(R.string.shared_pref_name), MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);
        apiClient.chatRoomList(id).enqueue(new Callback<List<ChatRoom>>() {
            @Override
            public void onResponse(Call<List<ChatRoom>> call, Response<List<ChatRoom>> response) {
                if (response.isSuccessful()) {
                    Collections.sort(response.body(), new Comparator<ChatRoom>() {
                        @Override
                        public int compare(ChatRoom o1, ChatRoom o2) {
                            return Integer.compare(o1.getId(), o2.getId());
                        }
                    });
                    rvaChatRoomList.setChatRooms(response.body());
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
}
