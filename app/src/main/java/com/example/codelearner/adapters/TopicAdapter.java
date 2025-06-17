package com.example.codelearner.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codelearner.R;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<String> topics;
    private Context context;
    private SharedPreferences prefs;

    public TopicAdapter(List<String> topics, Context context) {
        this.topics = topics;
        this.context = context;
        this.prefs = context.getSharedPreferences("topic_progress", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        String topic = topics.get(position);
        holder.topicText.setText(topic);

        boolean isChecked = prefs.getBoolean(topic, false);
        holder.topicCheckbox.setChecked(isChecked);

        holder.topicCheckbox.setOnCheckedChangeListener((buttonView, isCheckedNow) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(topic, isCheckedNow);
            editor.apply();
        });
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView topicText;
        CheckBox topicCheckbox;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topicText = itemView.findViewById(R.id.topicText);
            topicCheckbox = itemView.findViewById(R.id.topicCheckbox);
        }
    }
}
