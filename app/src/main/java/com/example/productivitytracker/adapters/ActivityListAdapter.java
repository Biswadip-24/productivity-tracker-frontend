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
import com.example.productivitytracker.models.Event;

import java.util.ArrayList;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ViewHolder> {

    private ArrayList<Event> activities;
    private float totalDuration;

    public ActivityListAdapter(ArrayList<Event> activities, float totalDuration){
        this.activities = activities;
        this.totalDuration = totalDuration;
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
        String name = activities.get(position).title;
        long diff = activities.get(position).end_time - activities.get(position).start_time;
        //long diff = Long.parseLong(activities.get(position).end_time) - Long.parseLong(activities.get(position).start_time);
        float hr = diff / (float) (60 * 60);
        String timeDiff = String.format("%.1f", hr);
        int percentage = (int)((hr / (float) totalDuration) * 100.0);

        //double percentage = activities.get(position).getPercentage();

        holder.txName.setText(name);
        holder.time.setText(timeDiff + "hr");
        holder.percentage.setProgress(percentage);
        String eventType = activities.get(position).type;

        if(eventType.equalsIgnoreCase("Study")) holder.icon.setBackgroundResource(R.drawable.studying);
        else if(eventType.equalsIgnoreCase("Gym")) holder.icon.setBackgroundResource(R.drawable.weightlifter);
        else if(eventType.equalsIgnoreCase("Entertainment")) holder.icon.setBackgroundResource(R.drawable.popcorn);
        else holder.icon.setBackgroundResource(R.drawable.others);
    }
    @Override
    public int getItemCount() {
        return activities.size();
    }
}
