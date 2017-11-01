package com.zihadrizkyef.oou.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zihadrizkyef.oou.Activity_ChatRoom;
import com.zihadrizkyef.oou.Fragment_ContactList;
import com.zihadrizkyef.oou.R;
import com.zihadrizkyef.oou.helper.api.ApiHelper;
import com.zihadrizkyef.oou.helper.api.OouApiClient;
import com.zihadrizkyef.oou.helper.database.DBUserProfile;
import com.zihadrizkyef.oou.model.CreateChatRoom;
import com.zihadrizkyef.oou.model.DeleteFriend;
import com.zihadrizkyef.oou.model.UserProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 05/07/17.
 */

public class RVAContactList extends  RecyclerView.Adapter<RVHContactList> {
    private Context context;
    private Fragment_ContactList fragment;
    private List<UserProfile> userProfiles;
    private OouApiClient apiClient;

    public RVAContactList(Context context, Fragment_ContactList fragment, List<UserProfile> userProfiles) {
        this.context = context;
        this.fragment = fragment;
        this.userProfiles = userProfiles;
        apiClient = ApiHelper.getOouApiClient();
    }

    @Override
    public RVHContactList onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_contactlist, parent, false);
        return new RVHContactList(v);
    }

    @Override
    public void onBindViewHolder(final RVHContactList holder, int position) {
        final UserProfile userProfile = userProfiles.get(position);

        Glide.with(context)
                .load(ApiHelper.API_BASE_URL+userProfile.getImageUrl())
                .error(R.drawable.ic_profile_picture)
                .into(holder.ivPhoto);

        holder.tvName.setText(userProfile.getName());
        holder.tvBio.setText(userProfile.getBio());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_contact_detail, null);
                alertDialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = alertDialogBuilder.show();
                ((TextView) dialogView.findViewById(R.id.tvName)).setText(userProfile.getName());
                ((TextView) dialogView.findViewById(R.id.tvChat)).setText(userProfile.getBio());
                dialogView.findViewById(R.id.ibChat).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        fragment.showProgress(true);
                        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_pref_name), MODE_PRIVATE);
                        int id = sharedPreferences.getInt("id", -1);
                        apiClient.createChatRoom(id, userProfile.getId(), 0, null).enqueue(new Callback<CreateChatRoom>() {
                            @Override
                            public void onResponse(Call<CreateChatRoom> call, Response<CreateChatRoom> response) {
                                fragment.showProgress(false);
                                if (response.isSuccessful()) {
                                    int roomId = response.body().getRoomId();
                                    Intent intent = new Intent(context, Activity_ChatRoom.class);
                                    intent.putExtra("roomId", roomId);
                                    intent.putExtra("roomName", userProfile.getName());
                                    context.startActivity(intent);
                                } else {
                                    Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<CreateChatRoom> call, Throwable t) {
                                Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                                fragment.showProgress(false);
                            }
                        });
                    }
                });
                Glide.with(context)
                        .load(ApiHelper.API_BASE_URL+userProfile.getImageUrl())
                        .error(R.drawable.ic_profile_picture)
                        .into((ImageView) dialogView.findViewById(R.id.ivPhoto));
                dialogView.findViewById(R.id.ibDelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_pref_name), MODE_PRIVATE);
                        int id = sharedPreferences.getInt("id", -1);
                        fragment.showProgress(true);
                        apiClient.deleteFriend(id, userProfile.getId()).enqueue(new Callback<DeleteFriend>() {
                            @Override
                            public void onResponse(Call<DeleteFriend> call, Response<DeleteFriend> response) {
                                fragment.showProgress(false);
                                if (response.isSuccessful()) {
                                    if (response.body().getSuccess()) {
                                        DBUserProfile db = new DBUserProfile(context);
                                        db.deleteuserProfile(userProfile);
                                        db.close();
                                        userProfiles.remove(holder.getAdapterPosition());
                                        notifyItemRemoved(holder.getAdapterPosition());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DeleteFriend> call, Throwable t) {
                                fragment.showProgress(false);
                                Toast.makeText(context, "Server error,", Toast.LENGTH_SHORT);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return userProfiles==null?0:userProfiles.size();
    }

    public void setUserProfileList(List<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
        notifyDataSetChanged();
    }
}

class RVHContactList extends RecyclerView.ViewHolder {
    ImageView ivPhoto;
    TextView tvName, tvBio;

    RVHContactList(View itemView) {
        super(itemView);
        ivPhoto = itemView.findViewById(R.id.ivPhoto);
        tvName = itemView.findViewById(R.id.tvName);
        tvBio = itemView.findViewById(R.id.tvBio);
    }
}