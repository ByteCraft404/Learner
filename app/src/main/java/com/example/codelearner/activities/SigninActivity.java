package com.example.codelearner.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

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
    private ImageView togglePasswordVisibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Set white status bar and nav bar
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.start));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));

        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

        // Initialize views
        etEmail = findViewById(R.id.edit_text_email);
        etPassword = findViewById(R.id.edit_text_password);
        Button btnSignin = findViewById(R.id.login_button);
        togglePasswordVisibility = findViewById(R.id.toggle_password_visibility);
        CardView backButtonContainer = findViewById(R.id.back_button_container);
        TextView signUpLinkBottom = findViewById(R.id.sign_up_link_bottom);

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
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();

                        Log.d("FULL_LOGIN_RESPONSE", new Gson().toJson(loginResponse));

                        String studentName = loginResponse.getFullName();
                        if (studentName == null || studentName.isEmpty()) {
                            studentName = loginResponse.getEmail();
                        }

                        // Store in SharedPreferences
                        SharedPreferences sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("studentName", studentName);
                        editor.putString("studentId", loginResponse.getId());
                        editor.putString("studentEmail", loginResponse.getEmail());
                        editor.apply();

                        Toast.makeText(SigninActivity.this, "Welcome " + studentName, Toast.LENGTH_SHORT).show();

                        // Go to MainActivity
                        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        handleLoginError(response);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    Toast.makeText(SigninActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("LOGIN_FAILURE", "Error: ", t);
                }
            });
        });

        // Toggle password visibility
        togglePasswordVisibility.setOnClickListener(v -> togglePasswordVisibility(etPassword, togglePasswordVisibility));

        // Back button logic
        backButtonContainer.setOnClickListener(v -> finish());

        // Navigate to RegisterActivity
        signUpLinkBottom.setOnClickListener(v -> {
            Intent intent = new Intent(SigninActivity.this, RegisterActivity.class);
            startActivity(intent);
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
