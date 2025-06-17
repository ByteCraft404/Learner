package com.example.codelearner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.codelearner.R;
import com.example.codelearner.Models.Badge;
import java.util.List;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder> {

    Context context;
    List<Badge> badgeList;

    public BadgeAdapter(Context context, List<Badge> badgeList) {
        this.context = context;
        this.badgeList = badgeList;
    }

    @NonNull
    @Override
    public BadgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_badge, parent, false);
        return new BadgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeViewHolder holder, int position) {
        Badge badge = badgeList.get(position);
        holder.title.setText(badge.getTitle());
        holder.description.setText(badge.getDescription());

        if (!badge.isEarned()) {
            holder.itemView.setAlpha(0.4f); // Greyed out if not earned
        }
    }

    @Override
    public int getItemCount() {
        return badgeList.size();
    }

    public static class BadgeViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public BadgeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.badgeTitle);
            description = itemView.findViewById(R.id.badgeDescription);
        }
    }
}
