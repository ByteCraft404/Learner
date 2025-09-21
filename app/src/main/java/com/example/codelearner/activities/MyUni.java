package com.example.codelearner.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // Import ContextCompat

import com.example.codelearner.R;
import com.example.codelearner.utils.WindowInsetsHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale; // Import Locale

public class MyUni extends AppCompatActivity {

    // Declare the ImageView for the back button
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize DrawerLayout and main content view
        setContentView(R.layout.activity_my_uni); // Set the content view to your layout

        // Setup edge-to-edge display with proper window insets handling
        WindowInsetsHelper.setupEdgeToEdge(this, android.R.id.content);

        // Initialize the back button ImageView
        backButton = findViewById(R.id.icon_grid);

        // Set OnClickListener for the back button
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                // This will finish the current activity and go back to the previous one in the stack
                finish();
            });
        }


        // Display Current Date
        TextView dateTextView = findViewById(R.id.text_date);
        if (dateTextView != null) {
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(currentDate);
            dateTextView.setText(formattedDate);
        }
    }
}