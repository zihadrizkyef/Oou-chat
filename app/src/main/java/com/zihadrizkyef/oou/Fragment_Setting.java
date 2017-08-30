package com.zihadrizkyef.oou;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zihadrizkyef.oou.helper.ApiHelper;
import com.zihadrizkyef.oou.helper.OouApiClient;
import com.zihadrizkyef.oou.model.UserProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 21/08/17.
 */

public class Fragment_Setting extends Fragment {
    ImageView ivProfilePicture;
    TextView tvName;
    TextView tvUsername;
    TextView tvBio;

    String name, username, bio, imageurl;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        ivProfilePicture = (ImageView) view.findViewById(R.id.ivPhotoProfile);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvBio = (TextView) view.findViewById(R.id.tvBio);

        ViewTreeObserver observer = tvBio.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int maxLines = (int) tvBio.getHeight()/tvBio.getLineHeight();
                tvBio.setMaxLines(maxLines);
                tvBio.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        String shrPrfName = getString(R.string.shared_pref_name);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(shrPrfName, MODE_PRIVATE);
        name = sharedPreferences.getString("name", "");
        username = sharedPreferences.getString("username", "");
        bio = sharedPreferences.getString("bio", "");
        imageurl = sharedPreferences.getString("imageurl", "");

        Glide.with(this)
                .load(ApiHelper.API_BASE_URL + imageurl)
                .into(ivProfilePicture);
        tvName.setText(name);
        tvUsername.setText(username);
        tvBio.setText(bio);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        OouApiClient apiClient = ApiHelper.getApiClient();
        apiClient.readProfile(username).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()) {
                    UserProfile profile = response.body();

                    String shrPrfName = getString(R.string.shared_pref_name);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(shrPrfName, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("id", profile.getId());
                    editor.putString("username", username);
                    editor.putString("name", profile.getName());
                    editor.putString("bio", profile.getBio());
                    editor.putString("imageurl", profile.getImageUrl());
                    editor.apply();

                    name = profile.getName();
                    username = profile.getUsername();
                    bio = profile.getBio();
                    imageurl = profile.getImageUrl();

                    Glide.with(Fragment_Setting.this)
                            .load(ApiHelper.API_BASE_URL+imageurl)
                            .error(R.drawable.ic_profile_picture)
                            .into(ivProfilePicture);
                    tvName.setText(name);
                    tvUsername.setText(username);
                    tvBio.setText(bio);
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
            }
        });
    }
}
