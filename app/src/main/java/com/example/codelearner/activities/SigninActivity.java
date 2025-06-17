package com.example.codelearner.activities;

import android.content.Intent;
import android.os.Build; // Import Build for version checks
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Window; // Import Window
import android.view.WindowManager; // Import WindowManager
import android.graphics.Color; // Import Color class
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.codelearner.Models.Student;
import com.example.codelearner.R;
import com.example.codelearner.api.RetrofitClient;
import com.example.codelearner.api.UserApi;
import com.example.codelearner.Models.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignin;
    private ImageView togglePasswordVisibility;
    private CardView backButtonContainer;
    private TextView signUpLinkBottom; // Variable for the "Sign up" link at the bottom

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- START: Status Bar and Navigation Bar customization ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // Set Status Bar color to white
            window.setStatusBarColor(Color.WHITE);

            // Set Navigation Bar color to white (optional, but good for consistency)
            window.setNavigationBarColor(Color.WHITE);

            // Make status bar icons dark for better visibility on a light background
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(
                        window.getDecorView().getSystemUiVisibility() |
                                android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR |
                                android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            }
        }
        // --- END: Status Bar and Navigation Bar customization ---
        setContentView(R.layout.activity_signin);



        // Initialize views
        etEmail = findViewById(R.id.edit_text_email);
        etPassword = findViewById(R.id.edit_text_password);
        btnSignin = findViewById(R.id.login_button);

        togglePasswordVisibility = findViewById(R.id.toggle_password_visibility);
        backButtonContainer = findViewById(R.id.back_button_container);
        signUpLinkBottom = findViewById(R.id.sign_up_link_bottom); // Initialize the Sign up link

        btnSignin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest loginRequest = new LoginRequest(email, password);
            UserApi api = RetrofitClient.getRetrofitInstance().create(UserApi.class);
            Call<Student> call = api.loginUser(loginRequest);
            call.enqueue(new Callback<Student>() {
                @Override
                public void onResponse(Call<Student> call, Response<Student> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Student student = response.body();
                        Toast.makeText(SigninActivity.this, "Welcome " + student.getName(), Toast.LENGTH_SHORT).show();

                        // Navigate to Dashboard or home activity on successful login
                        startActivity(new Intent(SigninActivity.this, DashboardActivity.class));
                        finish(); // Finish SigninActivity so user cannot go back to it
                    } else {
                        String errorMessage = "Invalid email or password. Please try again.";
                        if (response.errorBody() != null) {
                            try {
                                String apiError = response.errorBody().string();
                                if (!apiError.isEmpty()) {
                                    errorMessage = apiError;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(SigninActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Student> call, Throwable t) {
                    Toast.makeText(SigninActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }
            });
        });

        togglePasswordVisibility.setOnClickListener(v -> togglePasswordVisibility(etPassword, togglePasswordVisibility));
        backButtonContainer.setOnClickListener(v -> finish());

        // Set OnClickListener for the "Sign up" link
        signUpLinkBottom.setOnClickListener(v -> {
            startActivity(new Intent(SigninActivity.this, RegisterActivity.class));
            // Optional: finish(); if you want to remove SigninActivity from the back stack when going to Register
            // For a typical flow, you might keep it, but if you want Register to be the only thing on top, use finish()
        });
    }

    private void togglePasswordVisibility(EditText editText, ImageView toggleIcon) {
        if (editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
            editText.setTransformationMethod(null);
            toggleIcon.setImageResource(R.drawable.ic_eye_off); // Assuming you have an ic_eye_off drawable
        } else {
            editText.setTransformationMethod(new PasswordTransformationMethod());
            toggleIcon.setImageResource(R.drawable.ic_eye);
        }
        editText.setSelection(editText.getText().length());
    }
}