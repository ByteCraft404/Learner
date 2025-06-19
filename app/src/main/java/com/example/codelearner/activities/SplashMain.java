package com.example.codelearner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codelearner.R;

public class SplashMain extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the status bar color to your desired color
        // For API 21+ (Lollipop and above)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bottom_nav_ripple_color_effect));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.bottom_nav_ripple_color_effect));
        }

        setContentView(R.layout.activity_splash_main);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashMain.this, Splashpage.class));
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}