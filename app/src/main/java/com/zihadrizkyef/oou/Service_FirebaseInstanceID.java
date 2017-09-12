package com.zihadrizkyef.oou;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.zihadrizkyef.oou.helper.ApiHelper;
import com.zihadrizkyef.oou.helper.OouApiClient;
import com.zihadrizkyef.oou.model.EditProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 01/08/17.
 */

public class Service_FirebaseInstanceID extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String shrPrfName = getString(R.string.shared_pref_name);
        SharedPreferences sharedPreferences = getSharedPreferences(shrPrfName, MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);
        String name = sharedPreferences.getString("name", null);
        String bio = sharedPreferences.getString("bio", null);
        String frbsNotifId = FirebaseInstanceId.getInstance().getToken();

        Log.i("Oou frbs", "new frbs token=" + frbsNotifId);

        if (frbsNotifId != null) {
            if (id != -1) {
                OouApiClient apiClient = ApiHelper.getApiClient();
                apiClient.editProfile(id, name, bio, frbsNotifId).enqueue(new Callback<EditProfile>() {
                    @Override
                    public void onResponse(Call<EditProfile> call, Response<EditProfile> response) {
                        Log.i("Oou frbs", "frbs token updated");
                    }

                    @Override
                    public void onFailure(Call<EditProfile> call, Throwable t) {
                        Log.i("Oou frbs", "fail to update frbs");
                    }
                });
            }
        }
    }
}