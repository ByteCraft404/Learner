package com.example.codelearner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        etEmail = findViewById(R.id.et_email_signin);
        etPassword = findViewById(R.id.et_password_signin);
        btnSignin = findViewById(R.id.btn_signin_action);

        btnSignin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create LoginRequest object
            LoginRequest loginRequest = new LoginRequest(email, password);
            UserApi api = RetrofitClient.getRetrofitInstance().create(UserApi.class);
            Call<Student> call = api.loginUser(loginRequest);
            call.enqueue(new Callback<Student>() {
                @Override
                public void onResponse(Call<Student> call, Response<Student> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Student student = response.body();
                        Toast.makeText(SigninActivity.this, "Welcome " + student.getName(), Toast.LENGTH_SHORT).show();

                        // You can pass student info to next activity or save session
                        startActivity(new Intent(SigninActivity.this, DashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SigninActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Student> call, Throwable t) {
                    Toast.makeText(SigninActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        });
    }
}
