package com.zihadrizkyef.oou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.zihadrizkyef.oou.adapter.RVAContactList;
import com.zihadrizkyef.oou.helper.ApiHelper;
import com.zihadrizkyef.oou.helper.DatabaseUserProfile;
import com.zihadrizkyef.oou.helper.OouApiClient;
import com.zihadrizkyef.oou.model.UserProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 05/07/17.
 */

public class Fragment_ContactList extends Fragment {
    RecyclerView rvContactList;
    RVAContactList rvaContactList;
    OouApiClient apiClient;

    ProgressBar pbLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);

        final DatabaseUserProfile db = new DatabaseUserProfile(getActivity());
        List<UserProfile> userProfiles = db.getAllUserProfiles();
        db.close();

        rvaContactList = new RVAContactList(getActivity(), this, userProfiles);
        rvContactList = (RecyclerView) rootView.findViewById(R.id.rvContactList);
        rvContactList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvContactList.setAdapter(rvaContactList);

        apiClient = ApiHelper.getApiClient();

        pbLoading = (ProgressBar) rootView.findViewById(R.id.pbLoading);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        showProgress(true);
        final DatabaseUserProfile db = new DatabaseUserProfile(getActivity());
        apiClient.friendList(Activity_Main.profileId).enqueue(new Callback<List<UserProfile>>() {
            @Override
            public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    List<UserProfile> userProfiles = response.body();
                    rvaContactList.setUserProfileList(userProfiles);
                    for (int i = 0; i < userProfiles.size(); i++) {
                        UserProfile userProfile = userProfiles.get(i);
                        if (db.getUserProfile(userProfile.getId()) == null) {
                            db.addUserProfile(userProfile);
                        } else {
                            db.updateuserProfile(userProfile.getId(), userProfile);
                        }
                    }
                    db.close();
                }
            }

            @Override
            public void onFailure(Call<List<UserProfile>> call, Throwable t) {
                showProgress(false);
                rvaContactList.setUserProfileList(db.getAllUserProfiles());
            }
        });

        db.close();
    }

    public void showProgress(boolean show) {
        if (show) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            pbLoading.animate().setDuration(shortAnimTime).scaleX(1).scaleY(1);
        } else {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            pbLoading.animate().setDuration(shortAnimTime).scaleX(0).scaleY(0);
        }
    }
}