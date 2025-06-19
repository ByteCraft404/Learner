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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale; // Import Locale

public class MyUni extends AppCompatActivity {

    // Declare the ImageView for the back button
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set white status bar and navigation bar with dark icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_grey_background));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.light_grey_background));
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        // Initialize DrawerLayout and main content view
        setContentView(R.layout.activity_my_uni); // Set the content view to your layout



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