package com.example.codelearner.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.codelearner.Models.LoginRequest;
import com.example.codelearner.Models.LoginResponse;
import com.example.codelearner.R;
import com.example.codelearner.api.RetrofitClient;
import com.example.codelearner.api.UserApi;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignin;
    private ImageView togglePasswordVisibility;
    private CardView backButtonContainer;
    private TextView signUpLinkBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the status bar color to your desired color
        // For API 21+ (Lollipop and above)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.start));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.start));
        }

        setContentView(R.layout.activity_signin);

        // Initialize views
        etEmail = findViewById(R.id.edit_text_email);
        etPassword = findViewById(R.id.edit_text_password);
        btnSignin = findViewById(R.id.login_button);
        togglePasswordVisibility = findViewById(R.id.toggle_password_visibility);
        backButtonContainer = findViewById(R.id.back_button_container);
        signUpLinkBottom = findViewById(R.id.sign_up_link_bottom);

        // Prefill email if passed from RegisterActivity
        String passedEmail = getIntent().getStringExtra("email");
        if (passedEmail != null && !passedEmail.isEmpty()) {
            etEmail.setText(passedEmail);
        }

        // Sign-in button logic
        btnSignin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest loginRequest = new LoginRequest(email, password);
            UserApi api = RetrofitClient.getRetrofitInstance().create(UserApi.class);
            Call<LoginResponse> call = api.loginUser(loginRequest);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();

                        Log.d("FULL_LOGIN_RESPONSE", new Gson().toJson(loginResponse));

                        String studentName = loginResponse.getFullName();
                        if (studentName == null || studentName.isEmpty()) {
                            studentName = loginResponse.getEmail();
                        }


                        getSharedPreferences("user_prefs", MODE_PRIVATE)
                                .edit()
                                .putString("studentName", studentName)
                                .apply();

                        Toast.makeText(SigninActivity.this, "Welcome " + studentName, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                        intent.putExtra("studentId", loginResponse.getId());
                        startActivity(intent);
                        finish();
                    }
                    else {
                        handleLoginError(response);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(SigninActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("LOGIN_FAILURE", "Error: ", t);
                }
            });
        });

        // Toggle password visibility
        togglePasswordVisibility.setOnClickListener(v -> togglePasswordVisibility(etPassword, togglePasswordVisibility));

        // Back button
        backButtonContainer.setOnClickListener(v -> finish());

        // Sign up link
        signUpLinkBottom.setOnClickListener(v -> {
            startActivity(new Intent(SigninActivity.this, RegisterActivity.class));
        });
    }

    private void togglePasswordVisibility(EditText editText, ImageView toggleIcon) {
        if (editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
            editText.setTransformationMethod(null);
            toggleIcon.setImageResource(R.drawable.ic_eye_off);
        } else {
            editText.setTransformationMethod(new PasswordTransformationMethod());
            toggleIcon.setImageResource(R.drawable.ic_eye);
        }
        editText.setSelection(editText.getText().length());
    }

    private void handleLoginError(Response<LoginResponse> response) {
        String errorMessage = "Invalid email or password.";
        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                if (!errorBody.isEmpty()) {
                    errorMessage = errorBody;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(SigninActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
