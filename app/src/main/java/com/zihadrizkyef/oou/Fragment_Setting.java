package com.zihadrizkyef.oou;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.zihadrizkyef.oou.helper.ApiHelper;
import com.zihadrizkyef.oou.helper.OouApiClient;
import com.zihadrizkyef.oou.model.EditProfile;
import com.zihadrizkyef.oou.model.UserProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 21/08/17.
 */

public class Fragment_Setting extends Fragment {
    private static final int REQUEST_NAME_EDIT = 1;
    private static final int REQUEST_BIO_EDIT = 2;

    private OouApiClient apiClient;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor shaEditor;

    private ImageView ivProfilePicture;
    private TextView tvName;
    private TextView tvUsername;
    private TextView tvBio;

    private int id;
    private String name, username, bio, imageUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        apiClient = ApiHelper.getApiClient();
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_pref_name), MODE_PRIVATE);
        shaEditor = sharedPreferences.edit();

        setUpView(view);

        getProfileFromSharedPreferences();

        getProfileDataFromServer();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_NAME_EDIT) {
                name = data.getStringExtra("result");
                tvName.setText(name);
                updateProfileToServerDatabase();
            } else if (requestCode == REQUEST_BIO_EDIT) {
                bio = data.getStringExtra("result");
                tvBio.setText(bio);
                updateProfileToServerDatabase();
            }
        }
    }

    public void setUpView(View view) {
        ivProfilePicture = (ImageView) view.findViewById(R.id.ivPhotoProfile);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvBio = (TextView) view.findViewById(R.id.tvBio);

        ViewTreeObserver observer = tvBio.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int maxLines = tvBio.getHeight() / tvBio.getLineHeight();
                tvBio.setMaxLines(maxLines);
                tvBio.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_TextEditor.class);
                intent.putExtra("title", "Edit name");
                intent.putExtra("default", tvName.getText().toString());
                startActivityForResult(intent, REQUEST_NAME_EDIT);
            }
        });

        tvBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_TextEditor.class);
                intent.putExtra("title", "Edit bio");
                intent.putExtra("default", tvBio.getText().toString());
                startActivityForResult(intent, REQUEST_BIO_EDIT);
            }
        });
    }

    public void getProfileFromSharedPreferences() {
        id = sharedPreferences.getInt("id", -1);
        username = sharedPreferences.getString("username", "");
        imageUrl = sharedPreferences.getString("imageurl", "");
        name = sharedPreferences.getString("name", "");
        bio = sharedPreferences.getString("bio", "");

        Glide.with(this)
                .load(ApiHelper.API_BASE_URL + imageUrl)
                .into(ivProfilePicture);
        tvUsername.setText(username);
        tvName.setText(name);
        tvBio.setText(bio);
    }

    private void getProfileDataFromServer() {
        apiClient.readProfile(username).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()) {
                    UserProfile profile = response.body();

                    id = profile.getId();
                    username = profile.getUsername();
                    imageUrl = profile.getImageUrl();
                    name = profile.getName();
                    bio = profile.getBio();

                    updateProfileToSharedPreferences();

                    Glide.with(Fragment_Setting.this)
                            .load(ApiHelper.API_BASE_URL + imageUrl)
                            .error(R.drawable.ic_profile_picture)
                            .into(ivProfilePicture);
                    tvName.setText(name);
                    tvUsername.setText(username);
                    tvBio.setText(bio);
                } else {
                    Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfileToServerDatabase() {
        final ProgressDialog progress = ProgressDialog.show(getActivity(), "Updating profile", "please wait ...", true, false);
        apiClient.editProfile(id, name, bio, FirebaseInstanceId.getInstance().getToken())
                .enqueue(new Callback<EditProfile>() {
                    @Override
                    public void onResponse(Call<EditProfile> call, Response<EditProfile> response) {
                        progress.dismiss();
                        if (response.isSuccessful()) {
                            updateProfileToSharedPreferences();
                            getProfileFromSharedPreferences();
                            Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EditProfile> call, Throwable t) {
                        t.printStackTrace();
                        progress.dismiss();
                        Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateProfileToSharedPreferences() {
        shaEditor.putInt("id", id);
        shaEditor.putString("username", username);
        shaEditor.putString("imageurl", imageUrl);
        shaEditor.putString("name", name);
        shaEditor.putString("bio", bio);
        shaEditor.apply();
    }
}