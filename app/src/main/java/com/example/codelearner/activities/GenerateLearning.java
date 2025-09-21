package com.example.codelearner.activities;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ImageView; // Import ImageView if you add a back button icon

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.codelearner.R;
import com.example.codelearner.utils.WindowInsetsHelper;

public class GenerateLearning extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generate_learning);

        // Setup edge-to-edge display with proper window insets handling
        WindowInsetsHelper.setupEdgeToEdge(this, android.R.id.content);

        TextView greetingTextView = findViewById(R.id.greetingText);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String studentName = prefs.getString("studentName", "User");

        if (greetingTextView != null) {
            greetingTextView.setText("Hi, " + studentName);
        }

        // --- OPTIONAL: Handle a custom back button in the layout ---
        ImageView backButton = findViewById(R.id.backButton); // Assuming you have an ImageView with this ID
        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed(); // Call the system back method
                }
            });
        }
        // --- END OPTIONAL ---
    }

    // --- NEW: Override onBackPressed() to handle the system back button ---
    @Override
    public void onBackPressed() {
        super.onBackPressed(); // This will simply finish the current activity and go to the previous one
        // If you need specific logic, you'd put it here before super.onBackPressed()
        // For example, if you wanted to navigate to a specific activity:
        // Intent intent = new Intent(GenerateLearning.this, MainActivity.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clears activity stack
        // startActivity(intent);
        // finish();
    }
}