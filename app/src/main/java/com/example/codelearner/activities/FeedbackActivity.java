package com.example.codelearner.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codelearner.R;
import com.example.codelearner.utils.WindowInsetsHelper;

public class FeedbackActivity extends AppCompatActivity {

    RatingBar ratingBar;
    EditText commentEditText;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Setup edge-to-edge display with proper window insets handling
        WindowInsetsHelper.setupEdgeToEdge(this, android.R.id.content);

        ratingBar = findViewById(R.id.ratingBar);
        commentEditText = findViewById(R.id.commentEditText);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = commentEditText.getText().toString();

            if (rating == 0) {
                Toast.makeText(this, "Please select a rating.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save feedback locally for now
            SharedPreferences prefs = getSharedPreferences("feedback", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putFloat("rating", rating);
            editor.putString("comment", comment);
            editor.apply();

            Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity
        });
    }
}
