package com.example.productivitytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.productivitytracker.adapters.TimeLineAdapter;
import com.example.productivitytracker.databinding.ActivityMetricsBinding;
import com.example.productivitytracker.models.Event;
import com.example.productivitytracker.models.TimeLineModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MetricsActivity extends AppCompatActivity {

    ActivityMetricsBinding binding;
    RecyclerView rvTimeLine;
    FloatingActionButton addEvent;
    PieChart pieChart;

    UserViewModel viewModel;
    float score = 0.0f;
    private int userID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMetricsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pieChart = binding.pieChart;
        rvTimeLine = binding.timelineRecyclerView;
        addEvent = binding.fab;

        Bundle b = getIntent().getExtras();
        if(b != null) userID = b.getInt("userID");

        setUp();
        setListeners();
        setupPieChart();
    }

    private void setUp(){
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
        SimpleDateFormat tf = new SimpleDateFormat("h:mm a", Locale.getDefault());

        String formattedDate = df.format(Calendar.getInstance().getTime());
        String formattedTime = tf.format(Calendar.getInstance().getTime());

        binding.tvDate.setText(formattedDate);
        binding.tvTime.setText(formattedTime);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        viewModel.fetchUserData(userID);
        viewModel.fetchTodayEvents(userID);
    }

    private void setListeners(){
        addEvent.setOnClickListener(v -> {
            AddEventFragment addEventFragment = AddEventFragment.newInstance(userID);
            addEventFragment.show(getSupportFragmentManager(), addEventFragment.getTag());
        });

        viewModel.getTypes().observe(this, this::loadPieChartData);
        viewModel.getTodayEvents().observe(this, this::populateTimeLine);
        viewModel.getProductivityScore().observe(this, this::setProductivityScore);
    }

    private void setProductivityScore(float s){
        pieChart.setCenterText((int) (s * 100.0) + "%");
        score = s;
    }

    private void setupPieChart()
    {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);

        pieChart.setEntryLabelTextSize(18);
        pieChart.setEntryLabelColor(ContextCompat.getColor(this, R.color.colorPrimary));

        pieChart.setCenterText((int) (score * 100.0) + "%");
        pieChart.setCenterTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        pieChart.setCenterTextTypeface(ResourcesCompat.getFont(this, R.font.poppins_bold));
        pieChart.setCenterTextSize(36);

        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
    }

    private ArrayList<PieEntry> getPieChartEntries(HashMap<String,Long> eventTypes){
        long totalTime = 0L;
        for (Map.Entry<String, Long> entry : eventTypes.entrySet()) {
            long value = entry.getValue();
            totalTime += value;
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Long> entry : eventTypes.entrySet()) {
            String key = entry.getKey();
            long value = entry.getValue();
            float percentage = (float)value / (float)totalTime;
            entries.add(new PieEntry(percentage, key));
        }

        return entries;
    }

    private void loadPieChartData(HashMap<String,Long> eventTypes) {
        ArrayList<PieEntry> entries = getPieChartEntries(eventTypes);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "User Events");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(18f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(ResourcesCompat.getFont(this, R.font.poppins_bold));

        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }

    private void populateTimeLine(List<Event> events){
        events.sort(eventComparator);
        rvTimeLine.setAdapter(new TimeLineAdapter(rvTimeLine.getContext(), events));
    }

    public Comparator<Event> eventComparator
            = (o1, o2) -> (int)(o1.start_time - o2.start_time);
}