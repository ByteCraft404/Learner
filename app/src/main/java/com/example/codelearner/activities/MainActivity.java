package com.example.codelearner.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.codelearner.R;

public class MainActivity extends AppCompatActivity {

    TextView helloLisaTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.mainactivity));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.mainactivity));
        }

        setContentView(R.layout.activity_main2);


        String studentName = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("studentName", "Student");


        // Set the name in the helloLisaTextView
        helloLisaTextView = findViewById(R.id.helloLisaTextView);
        if (studentName != null && !studentName.isEmpty()) {
            helloLisaTextView.setText("Hello " + studentName);
        }

        // Find the Learning Hub CardView
        CardView learningHubCard = findViewById(R.id.learningHubCard);
        learningHubCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, learninghub.class);
            startActivity(intent);
        });

        // Find the Chat Bot CardView
        CardView chatBotCard = findViewById(R.id.chatBotCard);
        chatBotCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChatBotActivity.class);
            startActivity(intent);
        });

    }
}
