package com.example.codelearner.activities; // Ensure this package matches your project structure

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button; // Import Button
import android.widget.TextView; // In case "create account" is a TextView in your real splash (though here it's a Button)

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.codelearner.R;

public class Splashpage extends AppCompatActivity {

    // Declare the buttons
    private Button signInButton;
    private Button createAccountButton; // It's a Button in your XML

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splashpage); // Assuming activity_splashpage.xml is your provided XML

        // Apply window insets (ensure your root layout in activity_splashpage.xml has id="@+id/main")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the buttons using their IDs from the XML
        signInButton = findViewById(R.id.sign_in_button);
        createAccountButton = findViewById(R.id.create_account_button);

        // Set an OnClickListener for the "Sign in" button
        signInButton.setOnClickListener(v -> {
            // Create an Intent to start the SigninActivity
            Intent intent = new Intent(Splashpage.this, SigninActivity.class);
            startActivity(intent);
            // Optional: If you don't want the user to return to Splashpage, call finish()
            // finish();
        });

        // Set an OnClickListener for the "Create an account" button
        createAccountButton.setOnClickListener(v -> {
            // Create an Intent to start the RegisterActivity
            Intent intent = new Intent(Splashpage.this, RegisterActivity.class);
            startActivity(intent);
            // Optional: If you don't want the user to return to Splashpage, call finish()
            // finish();
        });
    }
}