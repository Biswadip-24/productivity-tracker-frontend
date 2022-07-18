package com.example.productivitytracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.productivitytracker.adapters.ActivityListAdapter;
import com.example.productivitytracker.databinding.FragmentHomeBinding;
import com.example.productivitytracker.models.Activity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList<BarEntry> barEntries;
    final ArrayList<String> weekDaysLabel = new ArrayList<>();
    ArrayList<Activity> userActivities = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        barChart = binding.weeklyBarChart;
        populateChart();
        getUserActivities();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners(){
        binding.userIcon.setOnClickListener(v -> openProfileActivity());
        binding.btRecommendations.setOnClickListener(v -> openRecommendationsActivity());
        binding.moreDetails.setOnClickListener(v -> openMetricsActivity());
    }

    private void populateChart(){

        getEntries();
        setWeeklyXAxisLabel();

        barDataSet = new BarDataSet(barEntries, "");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        styleBarChart();
    }

    private void getUserActivities(){
        setSampleUserActivities();
        ActivityListAdapter adapter = new ActivityListAdapter(userActivities);
        binding.rvActivities.setAdapter(adapter);
    }

    private void getEntries()
    {
        // These are all just test data. Later we fetch all these data from API call
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 20));
        barEntries.add(new BarEntry(2, 23));
        barEntries.add(new BarEntry(3, 75));
        barEntries.add(new BarEntry(4, 67));
        barEntries.add(new BarEntry(5, 40));
        barEntries.add(new BarEntry(6, 50));
        barEntries.add(new BarEntry(7, 25));
    }

    private void setSampleUserActivities(){
        userActivities.add(new Activity("Gym","", 3.1f,25));
        userActivities.add(new Activity("Screen Time","", 2.2f,20));
        userActivities.add(new Activity("Study","", 3,25));
        userActivities.add(new Activity("Meetings","", 2.3f,23));
    }

    private void setWeeklyXAxisLabel() {
        weekDaysLabel.add("M");
        weekDaysLabel.add("T");
        weekDaysLabel.add("W");
        weekDaysLabel.add("T");
        weekDaysLabel.add("F");
        weekDaysLabel.add("S");
        weekDaysLabel.add("S");


        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(xAxisFormatter);
    }

    private void styleBarChart()
    {
        Description description = new Description();
        description.setText("");

        barChart.setDescription(description);    // Hide the description
        //barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getLegend().setEnabled(false);   // Hide the legend
        barChart.setExtraOffsets(8,0,0,16);

        barDataSet.setDrawValues(false);
        barDataSet.setColor(ContextCompat.getColor(getContext(), R.color.bar_graph_color));
        barDataSet.setValueTextColor(Color.WHITE);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barDataSet.setValueTextSize(18f);

        barData.setBarWidth(0.5f);
    }

    public class DayAxisValueFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;
        public DayAxisValueFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }
        @Override
        public String getFormattedValue(float value) {
            return weekDaysLabel.get((int) value - 1);
        }
    }

    private void openProfileActivity(){
        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
        startActivity(intent);
    }

    private void openRecommendationsActivity(){
        Intent intent = new Intent(getActivity(), RecommendationsActivity.class);
        startActivity(intent);
    }

    private void openMetricsActivity(){
        Intent intent = new Intent(getActivity(), MetricsActivity.class);
        startActivity(intent);
    }
}
