package com.example.codelearner.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codelearner.Models.LearningPath;
import com.example.codelearner.R;
import com.example.codelearner.network.ApiClient;
import com.example.codelearner.network.ApiService;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText studentIdInput, studentNameInput, otherGoalInput;
    AutoCompleteTextView careerGoalInput, timeAvailableInput, learningPrefInput, experienceLevelInput, studyTimeInput;
    TextInputLayout otherGoalLayout;
    Button submitBtn;
    ApiService api;
    ProgressDialog progressDialog; // ✅ Progress dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI styling
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.white));
            getWindow().setNavigationBarColor(getResources().getColor(android.R.color.white));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            );
        }

        // Initialize views
        studentIdInput = findViewById(R.id.studentIdInput);
        studentNameInput = findViewById(R.id.studentNameInput);
        careerGoalInput = findViewById(R.id.careerGoalInput);
        otherGoalLayout = findViewById(R.id.otherGoalLayout);
        otherGoalInput = findViewById(R.id.otherGoalInput);
        timeAvailableInput = findViewById(R.id.timeAvailableInput);
        learningPrefInput = findViewById(R.id.learningPrefInput);
        experienceLevelInput = findViewById(R.id.experienceLevelInput);
        studyTimeInput = findViewById(R.id.studyTimeInput);
        submitBtn = findViewById(R.id.submitBtn);
        api = ApiClient.getClient().create(ApiService.class);

        // Progress dialog setup
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Generating your learning path...");
        progressDialog.setCancelable(false);

        // Get data from SignInActivity
        String passedStudentId = getIntent().getStringExtra("studentId");
        String passedStudentName = getIntent().getStringExtra("studentName");

        if (passedStudentName != null) {
            studentNameInput.setText(passedStudentName);
        }

        if (passedStudentId != null && !passedStudentId.isEmpty()) {
            studentIdInput.setText(passedStudentId);
            studentIdInput.setEnabled(false);
        }

        // Show "Other" input when needed
        careerGoalInput.setOnItemClickListener((adapterView, view, i, l) -> {
            String selected = adapterView.getItemAtPosition(i).toString();
            otherGoalLayout.setVisibility(selected.equalsIgnoreCase("Other") ? View.VISIBLE : View.GONE);
        });

        // Submit button click
        submitBtn.setOnClickListener(v -> {
            String studentId = studentIdInput.getText().toString().trim();
            String name = studentNameInput.getText().toString().trim();
            String careerGoal = careerGoalInput.getText().toString().trim();
            String timeAvailable = timeAvailableInput.getText().toString().trim();
            String learningPref = learningPrefInput.getText().toString().trim();
            String experience = experienceLevelInput.getText().toString().trim();
            String studyTime = studyTimeInput.getText().toString().trim();

            if (careerGoal.equalsIgnoreCase("Other")) {
                careerGoal = otherGoalInput.getText().toString().trim();
            }

            if (studentId.isEmpty() || name.isEmpty() || careerGoal.isEmpty() || timeAvailable.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> body = new HashMap<>();
            body.put("studentId", studentId);
            body.put("studentName", name);
            body.put("careerGoal", careerGoal);
            body.put("timeAvailable", timeAvailable);
            body.put("learningPreferences", learningPref);
            body.put("experienceLevel", experience);
            body.put("studyTime", studyTime);

            // Show progress dialog
            progressDialog.show();

            api.generateLearningPath(body).enqueue(new Callback<LearningPath>() {
                @Override
                public void onResponse(Call<LearningPath> call, Response<LearningPath> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra("studentId", studentId);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                            Log.e("API_ERROR", errorBody);
                            Toast.makeText(LoginActivity.this, "Failed: " + errorBody, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Unexpected error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LearningPath> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("NETWORK_FAILURE", t.getMessage(), t);
                    Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
