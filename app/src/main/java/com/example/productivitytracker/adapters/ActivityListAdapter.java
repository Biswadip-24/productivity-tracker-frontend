package com.example.productivitytracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productivitytracker.R;
import com.example.productivitytracker.models.Activity;

import java.util.ArrayList;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ViewHolder> {

    private ArrayList<Activity> activities;

    public ActivityListAdapter(ArrayList<Activity> activities){
        this.activities = activities;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txName;
        private ImageView icon;
        private ProgressBar percentage;
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txName = itemView.findViewById(R.id.activity_name);
            icon = itemView.findViewById(R.id.activity_icon);
            percentage = itemView.findViewById(R.id.activity_percentage);
            time = itemView.findViewById(R.id.activity_time);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = activities.get(position).getName();
        float time = activities.get(position).getTime();
        double percentage = activities.get(position).getPercentage();

        holder.txName.setText(name);
        holder.time.setText(Float.toString(time) + "hr");
        holder.percentage.setProgress((int)percentage);
    }
    @Override
    public int getItemCount() {
        return activities.size();
    }
}
