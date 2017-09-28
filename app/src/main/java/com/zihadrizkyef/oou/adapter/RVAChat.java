package com.zihadrizkyef.oou.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zihadrizkyef.oou.R;
import com.zihadrizkyef.oou.helper.ApiHelper;
import com.zihadrizkyef.oou.model.Chat;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 05/07/17.
 */

public class RVAChat extends  RecyclerView.Adapter<RVHChat> {
    private Context context;
    private List<Chat> chats;

    public RVAChat(Context context, List<Chat> chats) {
        this.context = context;
        this.chats = chats;
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = chats.get(position);

        String shrPrfName = context.getString(R.string.shared_pref_name);
        SharedPreferences sharedPreferences = context.getSharedPreferences(shrPrfName, MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);

        return chat.getSenderId() == id ? 0 : 1;
    }

    @Override
    public RVHChat onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(context).inflate(R.layout.rv_chatrow_self, parent, false);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.rv_chatrow_other, parent, false);
        }
        return new RVHChat(v);
    }

    @Override
    public void onBindViewHolder(RVHChat holder, int position) {
        Chat chat = chats.get(position);

        if (holder.getItemViewType() != 0) {
            Glide.with(context)
                    .load(ApiHelper.API_BASE_URL + "/" + chat.getImageUrl())
                    .error(R.drawable.ic_profile_picture)
                    .into(holder.ivPhoto);
        } else {
            if (chat.getReaded() == 0) {
                holder.ivCheck.setVisibility(View.GONE);
            } else {
                holder.ivCheck.setVisibility(View.VISIBLE);
            }
        }

        holder.tvText.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return chats == null ? 0 : chats.size();
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }
}

class RVHChat extends RecyclerView.ViewHolder {
    ImageView ivPhoto;
    TextView tvText;
    ImageView ivCheck;

    RVHChat(View itemView) {
        super(itemView);
        ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
        tvText = (TextView) itemView.findViewById(R.id.tvText);
        ivCheck = (ImageView) itemView.findViewById(R.id.ivCheck);
    }
}