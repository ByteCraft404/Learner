package com.example.codelearner.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.codelearner.adapters.BadgeAdapter;
import com.example.codelearner.Models.Badge;
import java.util.ArrayList;
import com.example.codelearner.R;
import java.util.List;

public class BadgeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BadgeAdapter adapter;
    List<Badge> badgeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);

        recyclerView = findViewById(R.id.badgeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        badgeList = new ArrayList<>();
        badgeList.add(new Badge("Week 1 Complete", "You completed your first study week!", true));
        badgeList.add(new Badge("7-Day Streak", "You studied 7 days in a row!", false));
        badgeList.add(new Badge("Resource Explorer", "Searched for learning materials", true));

        adapter = new BadgeAdapter(this, badgeList);
        recyclerView.setAdapter(adapter);
    }
}
