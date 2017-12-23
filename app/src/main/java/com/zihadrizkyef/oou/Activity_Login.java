package com.zihadrizkyef.oou;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.zihadrizkyef.oou.helper.api.ApiHelper;
import com.zihadrizkyef.oou.helper.api.OouApiClient;
import com.zihadrizkyef.oou.model.EditProfile;
import com.zihadrizkyef.oou.model.LoginUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.inputmethod.EditorInfo.IME_ACTION_GO;

/**
 * A login screen that offers login via email/password.
 */
public class Activity_Login extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvRegister;
    private Button btSignIn;

    private OouApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiClient = ApiHelper.getOouApiClient();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvRegister = findViewById(R.id.tvRegister);
        btSignIn = findViewById(R.id.btSignIn);

        etPassword.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == IME_ACTION_GO) {
                attemptLogin();
                return true;
            }
            return false;
        });

        btSignIn.setOnClickListener(view -> attemptLogin());

        SpannableString spnRegister = new SpannableString("Don't have account yet? Register now");
        int startLinkChar = spnRegister.toString().indexOf("Register");
        int endLinkChar = startLinkChar + ("Register").length();
        spnRegister.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(Activity_Login.this, Activity_Register.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
            }
        }, startLinkChar, endLinkChar, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegister.setText(spnRegister);
        tvRegister.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void attemptLogin() {
        etUsername.setError(null);
        etPassword.setError(null);

        final String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (password.length() < 4) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            etUsername.setError(getString(R.string.error_field_required));
            focusView = etUsername;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            final ProgressDialog progressDialog = ProgressDialog.show(this, "Loading", "please wait ...", true, false);
            Call<LoginUser> loginUser = apiClient.loginUser(username, password);
            loginUser.enqueue(new Callback<LoginUser>() {
                @Override
                public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        startActivity(new Intent(Activity_Login.this, Activity_Splash.class));
                        String shrPrfName = getString(R.string.shared_pref_name);
                        SharedPreferences sharedPreferences = getSharedPreferences(shrPrfName, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("id", response.body().getId());
                        editor.putString("username", username);
                        editor.putString("name", response.body().getName());
                        editor.putString("bio", response.body().getBio());
                        editor.putString("imageurl", response.body().getImageurl());
                        editor.apply();

                        Call<EditProfile> editProfile = apiClient.editProfile(
                                response.body().getId(),
                                response.body().getName(),
                                response.body().getBio(),
                                FirebaseInstanceId.getInstance().getToken()
                        );
                        editProfile.enqueue(new Callback<EditProfile>() {
                            @Override
                            public void onResponse(Call<EditProfile> call, Response<EditProfile> response) {

                            }

                            @Override
                            public void onFailure(Call<EditProfile> call, Throwable t) {

                            }
                        });

                        finish();
                    } else {
                        Toast.makeText(Activity_Login.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginUser> call, Throwable t) {
                    Toast.makeText(Activity_Login.this, "Server error", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    t.printStackTrace();
                    progressDialog.dismiss();
                }
            });
        }
    }
}