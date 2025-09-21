package com.example.codelearner.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.example.codelearner.R;
import com.example.codelearner.utils.WindowInsetsHelper;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    EditText searchInput;
    Button searchButton;
    ListView searchResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Setup edge-to-edge display with proper window insets handling
        WindowInsetsHelper.setupEdgeToEdge(this, android.R.id.content);

        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);
        searchResultsList = findViewById(R.id.searchResultsList);

        searchButton.setOnClickListener(v -> {
            String query = searchInput.getText().toString().toLowerCase();

            List<String> dummyResults = getDummyResults(query);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    dummyResults
            );
            searchResultsList.setAdapter(adapter);
        });
    }

    private List<String> getDummyResults(String query) {
        List<String> results = new ArrayList<>();

        if (query.contains("java")) {
            results.add("Java Loops - YouTube: https://youtube.com/example1");
            results.add("OOP in Java - Article: https://example.com/java-oop");
        } else if (query.contains("python")) {
            results.add("Python Lists - YouTube: https://youtube.com/example2");
            results.add("Python Loops - Article: https://example.com/python-loops");
        } else {
            results.add("No resources found. Try another keyword.");
        }

        return results;
    }
}
