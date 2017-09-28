package com.zihadrizkyef.oou;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.zihadrizkyef.oou.helper.ApiHelper;
import com.zihadrizkyef.oou.helper.OouApiClient;
import com.zihadrizkyef.oou.model.RegisterUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Register extends AppCompatActivity {
    ProgressBar pbLoading;
    EditText etName;
    EditText etUsername;
    EditText etPassword;
    EditText etPasswordConf;
    Button btRegister;
    TextView tvSignIn;
    View vRegisterForm;

    OouApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        apiClient = ApiHelper.getOouApiClient();

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        etName = (EditText) findViewById(R.id.etName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConf = (EditText) findViewById(R.id.etPasswordConf);
        btRegister = (Button) findViewById(R.id.btRegister);
        tvSignIn = (TextView) findViewById(R.id.tvSignIn);
        vRegisterForm = findViewById(R.id.scRegisterForm);

        etPasswordConf.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                register();
                return true;
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        SpannableString spnRegister = new SpannableString("Already have account? Sign In now");
        spnRegister.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(Activity_Register.this, Activity_Login.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
            }
        }, 22, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSignIn.setText(spnRegister);
        tvSignIn.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Activity_Register.this, Activity_Login.class));
        finish();
    }

    private void register() {
        final String username = etUsername.getText().toString(),
                password = etPassword.getText().toString(),
                confPassword = etPasswordConf.getText().toString(),
                name = etName.getText().toString();

        if (!username.equals("") && (password.length()>3 && password.equals(confPassword)) && !name.equals("")) {
            final ProgressDialog progressDialog = ProgressDialog.show(this, "Loading", "please wait ...", true, false);
            Call<RegisterUser> registCall = apiClient.registerUser(username, password, name, FirebaseInstanceId.getInstance().getToken());
            registCall.enqueue(new Callback<RegisterUser>() {
                @Override
                public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        String shrPrfName = getString(R.string.shared_pref_name);
                        SharedPreferences sharedPreferences = getSharedPreferences(shrPrfName, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("id", response.body().getId());
                        editor.putString("name", name);
                        editor.putString("username", username);
                        editor.putString("password", password);

                        editor.apply();

                        startActivity(new Intent(Activity_Register.this, Activity_Splash.class));
                        finish();
                    } else {
                        Toast.makeText(Activity_Register.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterUser> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(Activity_Register.this, "Server error", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }
    }
}