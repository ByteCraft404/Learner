package com.example.codelearner.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.codelearner.R;
import com.example.codelearner.utils.WindowInsetsHelper;

public class SplashMain extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_main);

        // Setup edge-to-edge display with proper window insets handling
        WindowInsetsHelper.setupEdgeToEdge(this, android.R.id.content);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashMain.this, Splashpage.class));
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}