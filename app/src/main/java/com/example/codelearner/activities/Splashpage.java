package com.example.codelearner.activities; // Ensure this package matches your project structure

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
// Import View for findViewById and OnClickListener

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView; // Import CardView

import com.example.codelearner.R;

public class Splashpage extends AppCompatActivity {

    // Declare the CardView for the "EXPLORE" button
    private CardView exploreButtonCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the status bar color to your desired color
        // For API 21+ (Lollipop and above)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.start));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.start));
        }

        setContentView(R.layout.activity_splashpage);
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