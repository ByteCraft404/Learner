package com.example.codelearner.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.codelearner.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View mainContentRoot; // Reference to your main ConstraintLayout
    private ImageButton menuButton;
    private NavigationView navigationViewDrawer; // Reference to your navigation drawer
    TextView helloLisaTextView; // Keep this for your existing logic
    private TextView drawerUsernameTextView; // Reference for the username in the drawer header

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to your updated layout
        setContentView(R.layout.activity_main2); // Assuming activity_main2 is your main dashboard layout

        // Set white status bar and navigation bar with dark icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainactivity));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.mainactivity));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        // Initialize DrawerLayout and main content view
        drawerLayout = findViewById(R.id.drawer_layout);
        mainContentRoot = findViewById(R.id.main_content_root); // The ConstraintLayout to move (though it won't move now)
        menuButton = findViewById(R.id.menuButton);
        navigationViewDrawer = findViewById(R.id.nav_view_drawer); // The NavigationView for the drawer

        // Get student name from SharedPreferences
        // NOTE: Ensure you are saving 'studentName' in SharedPreferences during login/registration
        String studentName = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("studentName", "Student");

        // Set the name in the helloLisaTextView (your dashboard greeting)
        helloLisaTextView = findViewById(R.id.helloLisaTextView);
        if (studentName != null && !studentName.isEmpty()) {
            helloLisaTextView.setText("Hello " + studentName);
        }

        // Access the header view from the NavigationView
        // Ensure nav_header.xml has a TextView with ID 'username_text'
        View headerView = navigationViewDrawer.getHeaderView(0);
        drawerUsernameTextView = headerView.findViewById(R.id.username_text);

        // Set the name in the drawer's username_text
        if (drawerUsernameTextView != null && studentName != null && !studentName.isEmpty()) {
            drawerUsernameTextView.setText(studentName);
        }

        // Set up the click listener for the menu button
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        // Set up the listener for the navigation drawer items
        navigationViewDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                // Handle menu item clicks (add Intents for your activities as needed)
                if (id == R.id.nav_discover) {
                    Toast.makeText(MainActivity.this, "Discover clicked", Toast.LENGTH_SHORT).show();
                    // startActivity(new Intent(MainActivity.this, DiscoverActivity.class)); // Example
                } else if (id == R.id.nav_post_gems) {
                    Toast.makeText(MainActivity.this, "Post Gems clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_bookmarks) {
                    Toast.makeText(MainActivity.this, "Bookmarks clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_messages) {
                    Toast.makeText(MainActivity.this, "Messages clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_profile) {
                    Toast.makeText(MainActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_settings) {
                    Toast.makeText(MainActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    Toast.makeText(MainActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
                    // Implement actual logout logic (clear session, navigate to login)
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // --- Removed the DrawerLayout.DrawerListener that caused the 'push' animation. ---
        // With the DrawerLayout and NavigationView configured as they are in your XML,
        // the default behavior is an overlay. The explicit translation was overriding this.

        // Existing click listeners for cards
        CardView learningHubCard = findViewById(R.id.learningHubCard);
        learningHubCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, learninghub.class);
            startActivity(intent);
        });

        CardView chatBotCard = findViewById(R.id.chatBotCard);
        chatBotCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChatBotActivity.class);
            startActivity(intent);
        });

        CardView myUniCard = findViewById(R.id.myUniCard);
        if (myUniCard != null) {
            myUniCard.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MyUni.class);
                startActivity(intent);
            });
        } else {
            System.err.println("MyUni CardView not found with ID R.id.myUniCard");
        }

        // Click listener for Generate Learning Path Card
        CardView generateLearningPathCard = findViewById(R.id.generateLearningPathCard);
        if (generateLearningPathCard != null) {
            generateLearningPathCard.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, GenerateLearning.class);
                startActivity(intent);
            });
        } else {
            System.err.println("GenerateLearningPath CardView not found with ID R.id.generateLearningPathCard");
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
