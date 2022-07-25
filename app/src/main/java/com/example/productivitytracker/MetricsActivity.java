package com.example.productivitytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.productivitytracker.adapters.TimeLineAdapter;
import com.example.productivitytracker.databinding.ActivityMetricsBinding;
import com.example.productivitytracker.models.TimeLineModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MetricsActivity extends AppCompatActivity {

    ActivityMetricsBinding binding;
    RecyclerView rvTimeLine;
    FloatingActionButton addEvent;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMetricsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pieChart = binding.pieChart;
        rvTimeLine = binding.timelineRecyclerView;
        addEvent = binding.fab;

        addListeners();
        setupPieChart();
        loadPieChartData();
        populateTimeLine();
    }

    private void addListeners(){
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEventFragment addEventFragment = new AddEventFragment();
                addEventFragment.show(getSupportFragmentManager(), addEventFragment.getTag());
            }
        });
    }

    private void setupPieChart()
    {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);

        pieChart.setEntryLabelTextSize(18);
        pieChart.setEntryLabelColor(ContextCompat.getColor(this, R.color.colorPrimary));

        pieChart.setCenterText("41.6");
        pieChart.setCenterTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        pieChart.setCenterTextTypeface(ResourcesCompat.getFont(this, R.font.poppins_bold));
        pieChart.setCenterTextSize(36);

        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
    }

    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0.2f, "Screen Time"));
        entries.add(new PieEntry(0.15f, "Gym"));
        entries.add(new PieEntry(0.10f, "Travel"));
        entries.add(new PieEntry(0.25f, "Study"));
        entries.add(new PieEntry(0.3f, "Meetings"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense Category");
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

    private void populateTimeLine(){
        String[] name = {"Study", "Gym", "Screen Time", "Meetings", "Event 5"};
        String[] status = {"active", "inactive", "inactive", "inactive", "inactive"};
        String[] description = {"Preparing for upcoming Test on Monday","Working out in gym","Entertainment time spent on phone", "Attended couple of meetings to catch up with the project progress.", "Description 5"};
        String[] time = {"11:00 PM", "10:03 AM", "10:03 PM", " 1:15 PM", " 2:10 PM"};

        List<TimeLineModel> timeLineModelList;
        TimeLineModel[] timeLineModel;

        timeLineModelList = new ArrayList<>();
        int size = name.length;
        timeLineModel = new TimeLineModel[size];

        for (int i = 0; i < size; i++) {
            timeLineModel[i] = new TimeLineModel();
            timeLineModel[i].setName(name[i]);
            timeLineModel[i].setStatus(status[i]);
            timeLineModel[i].setDescription(description[i]);
            timeLineModel[i].setTime(time[i]);
            timeLineModelList.add(timeLineModel[i]);
        }

        rvTimeLine.setAdapter(new TimeLineAdapter(rvTimeLine.getContext(), timeLineModelList));
    }
}