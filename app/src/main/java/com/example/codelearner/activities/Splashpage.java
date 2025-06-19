package com.example.codelearner.activities; // Ensure this package matches your project structure

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
// Import View for findViewById and OnClickListener

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView; // Import CardView
import androidx.core.content.ContextCompat;

import com.example.codelearner.R;

public class Splashpage extends AppCompatActivity {

    // Declare the CardView for the "EXPLORE" button
    private CardView exploreButtonCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashpage);

        // Set white status bar and navigation bar with dark icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.start));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.start));
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        exploreButtonCard = findViewById(R.id.explore_button_card);

        // Set an OnClickListener for the "EXPLORE" CardView
        exploreButtonCard.setOnClickListener(v -> {
            // Create an Intent to start the SigninActivity
            Intent intent = new Intent(Splashpage.this, SigninActivity.class);
            startActivity(intent);
            // Optional: If you don't want the user to return to Splashpage, call finish()
            // finish();
        });

        // The logic for signInButton and createAccountButton has been removed
        // as per your request, assuming they are no longer part of this specific UI.
    }
}