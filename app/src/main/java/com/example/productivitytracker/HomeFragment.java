package com.example.productivitytracker;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.productivitytracker.databinding.FragmentHomeBinding;
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
    ArrayList barEntries;
    final ArrayList<String> weekDaysLabel = new ArrayList<>();

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
        return binding.getRoot();
    }

    private void populateChart(){

        getEntries();
        setWeeklyXAxisLabel();

        barDataSet = new BarDataSet(barEntries, "");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        styleBarChart();

    }

    private void getEntries() {
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 2));
        barEntries.add(new BarEntry(2, 1));
        barEntries.add(new BarEntry(3, 1));
        barEntries.add(new BarEntry(4, 3));
        barEntries.add(new BarEntry(5, 4));
        barEntries.add(new BarEntry(6, 3));
        barEntries.add(new BarEntry(7, 1));
    }

    private void setWeeklyXAxisLabel(){
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
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getLegend().setEnabled(false);   // Hide the legend
        barChart.setExtraOffsets(0,0,0,16);

        barDataSet.setDrawValues(false);
        barDataSet.setColor(ContextCompat.getColor(getContext(), R.color.bar_graph_color));
        barDataSet.setValueTextColor(Color.WHITE);
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
}
