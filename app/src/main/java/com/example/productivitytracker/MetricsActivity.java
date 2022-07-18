package com.example.productivitytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.os.Bundle;

import com.example.productivitytracker.databinding.ActivityMetricsBinding;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MetricsActivity extends AppCompatActivity {

    ActivityMetricsBinding binding;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMetricsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pieChart = binding.pieChart;

        setupPieChart();
        loadPieChartData();
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
}