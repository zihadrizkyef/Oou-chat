 package com.zihadrizkyef.oou.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zihadrizkyef.oou.Activity_ChatRoom;
import com.zihadrizkyef.oou.R;
import com.zihadrizkyef.oou.helper.ApiHelper;
import com.zihadrizkyef.oou.model.ChatRoom;

import java.util.List;

 /**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 16/07/17.
 */

public class RVAChatRoomList extends RecyclerView.Adapter<RVHChatRoomList> {
    private Context context;
    private List<ChatRoom> chatRooms;

     public RVAChatRoomList(Context context, List<ChatRoom> chatRooms) {
         this.context = context;
         this.chatRooms = chatRooms;
     }

     @Override
    public RVHChatRoomList onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_chatlist, parent, false);
        return new RVHChatRoomList(v);
    }

    @Override
    public void onBindViewHolder(RVHChatRoomList holder, int position) {
        final ChatRoom chatRoom = chatRooms.get(position);

        holder.tvName.setText(chatRoom.getName());
        holder.tvText.setText(chatRoom.getMessage());

        Log.i("URL", ApiHelper.API_BASE_URL + chatRoom.getImageUrl());
        Glide.with(context)
                .load(ApiHelper.API_BASE_URL+chatRoom.getImageUrl())
                .error(R.drawable.ic_profile_picture)
                .into(holder.ivPhoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_ChatRoom.class);
                intent.putExtra("roomId", chatRoom.getId());
                intent.putExtra("roomName", chatRoom.getName());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatRooms ==null?0: chatRooms.size();
    }

     public void setChatRooms(List<ChatRoom> chatRooms) {
         this.chatRooms = chatRooms;
         notifyDataSetChanged();
     }
 }

class RVHChatRoomList extends RecyclerView.ViewHolder {
    ImageView ivPhoto;
    TextView tvName, tvText;

    RVHChatRoomList(View itemView) {
        super(itemView);

        ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvText = (TextView) itemView.findViewById(R.id.tvText);
    }
}
