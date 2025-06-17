package com.example.codelearner.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color; // Import Color class

import androidx.appcompat.app.AppCompatActivity;

import com.example.codelearner.R;
import com.example.codelearner.api.RetrofitClient;
import com.example.codelearner.api.UserApi;
import com.example.codelearner.Models.Student;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText etFullName, etEmail, etPassword, etConfirmPassword;
    Button btnRegister;
    ImageView togglePasswordVisibility, toggleConfirmPasswordVisibility;
    TextView signInLinkBottom; // Declare the TextView for the "Sign In" link

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- START: Status Bar and Navigation Bar customization ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // Set Status Bar color to white
            window.setStatusBarColor(Color.WHITE);

            // Set Navigation Bar color to white (optional, as it's often black by default)
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

        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.edit_text_full_name);
        etEmail = findViewById(R.id.edit_text_email);
        etPassword = findViewById(R.id.edit_text_password);
        etConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        btnRegister = findViewById(R.id.login_button);

        togglePasswordVisibility = findViewById(R.id.toggle_password_visibility);
        toggleConfirmPasswordVisibility = findViewById(R.id.toggle_confirm_password_visibility);

        // Initialize the "Sign In" link TextView
        // IMPORTANT: Ensure R.id.sign_up_link_bottom is the correct ID for your "Sign In" TextView in activity_register.xml
        // If it's intended to be "Sign In" in this activity, its ID should probably be something like sign_in_link_bottom.
        // Double-check your activity_register.xml to confirm the ID for the "Sign In" link.
        signInLinkBottom = findViewById(R.id.sign_up_link_bottom); // Assuming this is the correct ID for the Sign In link

        btnRegister.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            Student student = new Student(name, email, password);
            registerUser(student);
        });

        togglePasswordVisibility.setOnClickListener(v -> togglePasswordVisibility(etPassword, togglePasswordVisibility));
        toggleConfirmPasswordVisibility.setOnClickListener(v -> togglePasswordVisibility(etConfirmPassword, toggleConfirmPasswordVisibility));

        findViewById(R.id.back_button_container).setOnClickListener(v -> finish());

        // Set OnClickListener for the "Sign In" link at the bottom
        signInLinkBottom.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, SigninActivity.class);
            startActivity(intent);
            // Optional: finish() this activity if you don't want it on the back stack after navigating to Signin
            // finish();
        });
    }

    private void registerUser(Student student) {
        UserApi api = RetrofitClient.getRetrofitInstance().create(UserApi.class);

        Call<Student> call = api.registerUser(student);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, SigninActivity.class));
                    finish(); // Finish current activity after successful registration
                } else {
                    String errorMessage = "Registration failed. Please try again.";
                    if (response.code() == 409) {
                        errorMessage = "Email already registered.";
                    } else if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    private void togglePasswordVisibility(EditText editText, ImageView toggleIcon) {
        if (editText.getTransformationMethod() == null) {
            editText.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
            toggleIcon.setImageResource(R.drawable.ic_eye); // Assuming ic_eye is the "visible" icon
        } else {
            editText.setTransformationMethod(null);
            toggleIcon.setImageResource(R.drawable.ic_eye_off); // Assuming ic_eye_off is the "hidden" icon
        }
        editText.setSelection(editText.getText().length());
    }
}