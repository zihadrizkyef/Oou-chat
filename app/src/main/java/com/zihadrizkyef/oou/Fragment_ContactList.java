package com.zihadrizkyef.oou;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zihadrizkyef.oou.adapter.RVAContactList;
import com.zihadrizkyef.oou.helper.api.ApiHelper;
import com.zihadrizkyef.oou.helper.api.OouApiClient;
import com.zihadrizkyef.oou.helper.database.DBUserProfile;
import com.zihadrizkyef.oou.model.UserProfile;

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

public class Fragment_ContactList extends Fragment {
    RecyclerView rvContactList;
    RVAContactList rvaContactList;
    OouApiClient apiClient;

    int userId;

    ProgressBar pbLoading;
    TextView tvContactEmpty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);

        String shrPrfName = getString(R.string.shared_pref_name);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(shrPrfName, MODE_PRIVATE);
        userId = sharedPreferences.getInt("id", -1);

        final DBUserProfile db = new DBUserProfile(getActivity());
        List<UserProfile> userProfiles = db.getAllUserProfiles();
        db.close();

        rvaContactList = new RVAContactList(getActivity(), this, userProfiles);
        rvContactList = rootView.findViewById(R.id.rvContactList);
        rvContactList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvContactList.setAdapter(rvaContactList);

        apiClient = ApiHelper.getOouApiClient();

        pbLoading = rootView.findViewById(R.id.pbLoading);
        pbLoading.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);

        tvContactEmpty = rootView.findViewById(R.id.tvContactEmpty);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        showProgress(true);
        final DBUserProfile db = new DBUserProfile(getActivity());
        apiClient.friendList(userId).enqueue(new Callback<List<UserProfile>>() {
            @Override
            public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    List<UserProfile> userProfiles = response.body();
                    Collections.sort(userProfiles, new Comparator<UserProfile>() {
                        @Override
                        public int compare(UserProfile o1, UserProfile o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    rvaContactList.setUserProfileList(userProfiles);

                    db.deleteAllUserProfile();
                    for (int i = 0; i < userProfiles.size(); i++) {
                        db.addUserProfile(userProfiles.get(i));
                    }

                    if (userProfiles.size() <= 0) {
                        tvContactEmpty.setVisibility(View.VISIBLE);
                    } else {
                        tvContactEmpty.setVisibility(View.GONE);
                    }
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