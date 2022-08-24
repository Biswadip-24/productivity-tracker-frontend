package com.example.productivitytracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.productivitytracker.R;
import com.example.productivitytracker.models.Event;
import com.example.productivitytracker.models.TimeLineModel;
import com.github.vipulasri.timelineview.TimelineView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Event> timeLineModelList;
    private Context context;

    public TimeLineAdapter(Context context, List<Event> timeLineModelList) {
        this.timeLineModelList = timeLineModelList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_timeline_layout, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).textView.setText(timeLineModelList.get(position).title);
        ((ViewHolder) holder).textViewDescription.setText(timeLineModelList.get(position).description);

        SimpleDateFormat tf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String formattedDate = tf.format(timeLineModelList.get(position).start_time * 1000);
        ((ViewHolder)holder).textViewTime.setText(formattedDate);

        long duration = timeLineModelList.get(position).end_time - timeLineModelList.get(position).start_time;
        String durationInHours = String.format("%.1f",duration / 3600.0) + " hr";

        ((ViewHolder)holder).textViewDuration.setText(durationInHours);
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return timeLineModelList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TimelineView timelineView;
        TextView textView, textViewDescription, textViewTime, textViewDuration;

        ViewHolder(View itemView, int viewType) {
            super(itemView);
            timelineView = itemView.findViewById(R.id.row_timeline_layout_time_marker);
            textView = itemView.findViewById(R.id.row_timeline_layout_text_view_name);
            textViewDescription = itemView.findViewById(R.id.row_timeline_layout_text_view_description);
            textViewTime = itemView.findViewById(R.id.row_timeline_layout_text_view_time);
            textViewDuration = itemView.findViewById(R.id.row_timeline_layout_text_view_duration);

            timelineView.initLine(viewType);
        }
    }
}

