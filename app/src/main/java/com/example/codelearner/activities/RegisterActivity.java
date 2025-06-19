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
import android.graphics.Color;

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
    TextView signInLinkBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.start));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.start));
        }


        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.edit_text_full_name);
        etEmail = findViewById(R.id.edit_text_email);
        etPassword = findViewById(R.id.edit_text_password);
        etConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        btnRegister = findViewById(R.id.login_button);
        togglePasswordVisibility = findViewById(R.id.toggle_password_visibility);
        toggleConfirmPasswordVisibility = findViewById(R.id.toggle_confirm_password_visibility);
        signInLinkBottom = findViewById(R.id.sign_up_link_bottom); // Confirm this ID in XML

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

            // Create and populate Student object correctly
            Student student = new Student();
            student.setFullName(name);
            student.setEmail(email);
            student.setPassword(password);

            registerUser(student);
        });

        togglePasswordVisibility.setOnClickListener(v -> togglePasswordVisibility(etPassword, togglePasswordVisibility));
        toggleConfirmPasswordVisibility.setOnClickListener(v -> togglePasswordVisibility(etConfirmPassword, toggleConfirmPasswordVisibility));

        findViewById(R.id.back_button_container).setOnClickListener(v -> finish());

        signInLinkBottom.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, SigninActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser(Student student) {
        UserApi api = RetrofitClient.getRetrofitInstance().create(UserApi.class);

        Call<Student> call = api.registerUser(student);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "Registration successful. Redirecting to login...", Toast.LENGTH_SHORT).show();

                    new android.os.Handler().postDelayed(() -> {
                        Intent intent = new Intent(RegisterActivity.this, SigninActivity.class);
                        intent.putExtra("email", student.getEmail());
                        startActivity(intent);
                        finish();
                    }, 2000);
                } else {
                    String errorMessage = "Registration failed. Please try again.";

                    if (response.code() == 409) {
                        Toast.makeText(RegisterActivity.this, "Email already registered. Redirecting to login...", Toast.LENGTH_SHORT).show();
                        new android.os.Handler().postDelayed(() -> {
                            Intent intent = new Intent(RegisterActivity.this, SigninActivity.class);
                            intent.putExtra("email", student.getEmail());
                            startActivity(intent);
                            finish();
                        }, 2000);
                        return;
                    }

                    if (response.errorBody() != null) {
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
            toggleIcon.setImageResource(R.drawable.ic_eye); // visible icon
        } else {
            editText.setTransformationMethod(null);
            toggleIcon.setImageResource(R.drawable.ic_eye_off); // hidden icon
        }
        editText.setSelection(editText.getText().length());
    }
}
