package com.zihadrizkyef.oou;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zihadrizkyef.oou.helper.api.ApiHelper;
import com.zihadrizkyef.oou.helper.api.OouApiClient;
import com.zihadrizkyef.oou.helper.database.DBUserProfile;
import com.zihadrizkyef.oou.model.AddFriend;
import com.zihadrizkyef.oou.model.UserProfile;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_AddFriend extends AppCompatActivity {
    OouApiClient apiClient;
    UserProfile userToAdd;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        apiClient = ApiHelper.getOouApiClient();

        String shrPrfName = getString(R.string.shared_pref_name);
        SharedPreferences sharedPreferences = getSharedPreferences(shrPrfName, MODE_PRIVATE);
        userId = sharedPreferences.getInt("id", -1);

        final LinearLayout lyProfile = findViewById(R.id.lyProfile);
        final CircleImageView civPicture = findViewById(R.id.civPicture);
        final TextView tvName = findViewById(R.id.tvName);
        final TextView tvCantAddFriend = findViewById(R.id.tvCantAddFriend);
        final Button btAddFriend = findViewById(R.id.btAddFriend);

        final SearchView svIDSearch = findViewById(R.id.svIDSearch);
        svIDSearch.requestFocus();
        svIDSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                svIDSearch.clearFocus();
                apiClient.readProfile(query).enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                        if (response.isSuccessful()) {
                            lyProfile.setVisibility(View.VISIBLE);
                            userToAdd = response.body();
                            Glide.with(Activity_AddFriend.this)
                                    .load(ApiHelper.API_BASE_URL+userToAdd.getImageUrl())
                                    .into(civPicture);
                            tvName.setText(userToAdd.getName());
                            if (userToAdd.getId() == userId) {
                                tvCantAddFriend.setVisibility(View.VISIBLE);
                                btAddFriend.setVisibility(View.GONE);
                            } else {
                                tvCantAddFriend.setVisibility(View.GONE);
                                btAddFriend.setVisibility(View.VISIBLE);
                            }
                        } else {
                            userToAdd = null;
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                        userToAdd = null;
                        lyProfile.setVisibility(View.GONE);
                        Toast.makeText(Activity_AddFriend.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        btAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiClient.addFriend(userId, userToAdd.getId()).enqueue(new Callback<AddFriend>() {
                    @Override
                    public void onResponse(Call<AddFriend> call, Response<AddFriend> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess()) {
                                DBUserProfile db = new DBUserProfile(Activity_AddFriend.this);
                                db.addUserProfile(userToAdd);
                                finish();
                            } else {
                                Toast.makeText(Activity_AddFriend.this, "Server error", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Activity_AddFriend.this, "Server error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AddFriend> call, Throwable t) {
                        Toast.makeText(Activity_AddFriend.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}