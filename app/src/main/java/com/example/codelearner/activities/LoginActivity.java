package com.example.codelearner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText; // Keep this for other EditTexts
import android.widget.SeekBar; // Import SeekBar
import android.widget.TextView; // Import TextView for the value display
import android.widget.Toast;
import com.example.codelearner.R;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codelearner.Models.LearningPath;
import com.example.codelearner.network.ApiClient;
import com.example.codelearner.network.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText studentIdInput, careerGoalInput, learningPrefInput; // Removed timeAvailableInput from here
    SeekBar timeAvailableSeekBar; // Declare SeekBar
    TextView timeAvailableValue; // Declare TextView for displaying SeekBar value
    Button submitBtn;
    ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        studentIdInput = findViewById(R.id.studentIdInput);
        careerGoalInput = findViewById(R.id.careerGoalInput);
        learningPrefInput = findViewById(R.id.learningPrefInput); // This is an AutoCompleteTextView but can be treated as EditText for getText()

        // Initialize the new SeekBar and TextView
        timeAvailableSeekBar = findViewById(R.id.timeAvailableSeekBar);
        timeAvailableValue = findViewById(R.id.timeAvailableValue); // Use the new ID for the TextView

        submitBtn = findViewById(R.id.submitBtn);

        api = ApiClient.getClient().create(ApiService.class);

        // Set up the SeekBar listener to update the TextView
        timeAvailableSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // The min value is 1, so the progress already directly reflects the hours.
                timeAvailableValue.setText(progress + " hours");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Code to execute when the user starts touching the seekbar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: Code to execute when the user stops touching the seekbar
            }
        });

        // Initialize the TextView with the current progress of the SeekBar
        timeAvailableValue.setText(timeAvailableSeekBar.getProgress() + " hours");


        submitBtn.setOnClickListener(v -> {
            String studentId = studentIdInput.getText().toString();
            String goal = careerGoalInput.getText().toString();
            int time = timeAvailableSeekBar.getProgress(); // Get value directly from SeekBar
            String pref = learningPrefInput.getText().toString();

            if (studentId.isEmpty() || goal.isEmpty() || time == 0) { // Check if time is 0 (or less than min value)
                Toast.makeText(this, "Please fill all fields and select time", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> body = new HashMap<>();
            body.put("studentId", studentId);
            body.put("careerGoal", goal);
            body.put("timeAvailable", time); // Use the integer value directly
            body.put("learningPreferences", pref);

            api.generateLearningPath(body).enqueue(new Callback<LearningPath>() {
                @Override
                public void onResponse(Call<LearningPath> call, Response<LearningPath> response) {
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra("studentId", studentId);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to generate plan: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LearningPath> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}