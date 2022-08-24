package com.example.productivitytracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.productivitytracker.adapters.ActivityListAdapter;
import com.example.productivitytracker.databinding.FragmentHomeBinding;
import com.example.productivitytracker.models.Event;
import com.example.productivitytracker.models.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList<BarEntry> barEntries = new ArrayList<>();
    ArrayList<String> weekDaysLabel = new ArrayList<>();
    ArrayList<Event> userActivities = new ArrayList<>();

    UserViewModel viewModel;
    int userID;

    public static HomeFragment newInstance(int userID) {
        HomeFragment f = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("userID", userID);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        barChart = binding.weeklyBarChart;

        Bundle args = getArguments();
        userID = args.getInt("userID", 1);

        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        fetchData();

        setUserEvents();
        setListeners();
        return binding.getRoot();
    }

    private void fetchData(){
        viewModel.fetchUserData(userID);
        viewModel.fetchTodayEvents(userID);
    }

    private void setListeners(){
        binding.userIcon.setOnClickListener(v -> openProfileActivity());
        binding.btRecommendations.setOnClickListener(v -> openRecommendationsActivity());
        binding.moreDetails.setOnClickListener(v -> openMetricsActivity());

        viewModel.getTodayEvents().observe(requireActivity(), this::addUserEvents);
        viewModel.getUserData().observe(requireActivity(), this::getLastWeekProductivity);
        viewModel.getProductiveHours().observe(requireActivity(), this::setProductHours);
        viewModel.getProductivityScore().observe(requireActivity(), this::setProductScore);
    }

    private void populateChart()
    {
        setWeeklyXAxisLabel();
        barDataSet = new BarDataSet(barEntries, "");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        styleBarChart();
        barChart.invalidate();
    }

    private void setProductHours(float productHours){
        binding.productiveHours.setText(String.format("%.1f",productHours));
    }

    private void setProductScore(float score){
        binding.productiveScore.setText((int) (score * 100.0) + "%");
    }

    private void setUserEvents(){
        ActivityListAdapter adapter = new ActivityListAdapter(userActivities);
        binding.rvActivities.setAdapter(adapter);
    }

    private void getLastWeekProductivity(User userData)
    {
        barEntries.clear();
        for(int i = 0;i < userData.lastWeekProductivity.size(); i++){
            barEntries.add(new BarEntry(i+1, userData.lastWeekProductivity.get(i)));
        }
        populateChart();
    }

    private void addUserEvents(List<Event> events){
        userActivities.clear();
        userActivities.addAll(events);
        setUserEvents();
    }

    private void setWeeklyXAxisLabel() {
        weekDaysLabel.clear();
        String[] dayOfWeek = {"S", "S", "M", "T", "W", "T", "F"};
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int count = 1;
        while(count <= 7){
            weekDaysLabel.add(dayOfWeek[day]);
            day = (day - 1)%7;
            if(day < 0) day += 7;
            count++;
        }

        Collections.reverse(weekDaysLabel);

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
        if(getContext() != null){
            barDataSet.setColor(ContextCompat.getColor(getContext(), R.color.bar_graph_color));
        }
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
        Bundle b = new Bundle();
        b.putInt("userID", userID);
        intent.putExtras(b);
        startActivity(intent);
    }
}
