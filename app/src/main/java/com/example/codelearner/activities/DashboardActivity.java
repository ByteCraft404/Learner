package com.example.codelearner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codelearner.Models.LearningPlan;
import com.example.codelearner.Models.LearningPlanRequest;
import com.example.codelearner.R;
import com.example.codelearner.Models.LearningPath;
import com.example.codelearner.api.RetrofitClient;
import com.example.codelearner.network.ApiClient;
import com.example.codelearner.network.ApiService;
import com.example.codelearner.adapters.TopicAdapter;

import java.util.Arrays;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView learningPathRecycler;
    private TopicAdapter topicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        learningPathRecycler = findViewById(R.id.learningPathRecycler);
        TextView planTitle = findViewById(R.id.planTitle);

        String studentId = getIntent().getStringExtra("studentId");

        // ✅ Null check for studentId to prevent crash
        if (studentId == null || studentId.isEmpty()) {
            Toast.makeText(this, "Student ID is missing!", Toast.LENGTH_LONG).show();
            return; // Exit to prevent null usage
        }

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.generateLearningPath(Map.of("studentId", studentId)).enqueue(new Callback<LearningPath>() {
            @Override
            public void onResponse(Call<LearningPath> call, Response<LearningPath> response) {
                if (response.isSuccessful() && response.body() != null) {
                    topicAdapter = new TopicAdapter(response.body().getTopics(), DashboardActivity.this);
                    learningPathRecycler.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
                    learningPathRecycler.setAdapter(topicAdapter);
                } else {
                    Toast.makeText(DashboardActivity.this, "Failed to load plan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LearningPath> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Button feedbackButton = findViewById(R.id.feedbackButton);
        feedbackButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, FeedbackActivity.class);
            startActivity(intent);
        });

        Button searchBtn = findViewById(R.id.searchButton);
        searchBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        Button badgeBtn = findViewById(R.id.badgeButton);
        badgeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, BadgeActivity.class);
            startActivity(intent);
        });

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        LearningPlanRequest request = new LearningPlanRequest("Software Engineer", Arrays.asList("A", "B"), 10, "video");

        Call<LearningPlan> call = apiService.generatePlan(request);
        call.enqueue(new Callback<LearningPlan>() {
            @Override
            public void onResponse(Call<LearningPlan> call, Response<LearningPlan> response) {
                if (response.isSuccessful()) {
                    LearningPlan plan = response.body();
                    // update UI if needed
                }
            }

            @Override
            public void onFailure(Call<LearningPlan> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Failed to connect: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
