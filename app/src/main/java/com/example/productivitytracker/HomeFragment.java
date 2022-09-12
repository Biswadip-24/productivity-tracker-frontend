package com.example.productivitytracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.productivitytracker.adapters.ActivityListAdapter;
import com.example.productivitytracker.databinding.FragmentHomeBinding;
import com.example.productivitytracker.models.Event;
import com.example.productivitytracker.models.Score;
import com.example.productivitytracker.models.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

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
    String emailId;
    String firstName, lastName;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    private String first_name;
    private String last_name;
    private String user_name;
    private String sleep_time;
    private String work_time;
    private String work_out_time;
    private String screen_time;

    public static HomeFragment newInstance(String emailId) {
        HomeFragment f = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("emailId", emailId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.fetchUserDataByEmail(emailId);
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
        emailId = args.getString("emailId");

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(requireActivity(), gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireActivity());
        if(account != null){
            if(account.getDisplayName().contains(" ")){
                firstName = account.getDisplayName().substring(0, account.getDisplayName().indexOf(' '));
                lastName = account.getDisplayName().substring(account.getDisplayName().indexOf(' ') + 1);
            }
            else{
                firstName = account.getDisplayName();
                lastName = "";
            }
        }

        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        setListeners();
        return binding.getRoot();
    }


    private void setListeners(){
        binding.userIcon.setOnClickListener(v -> openProfileActivity());
        binding.btRecommendations.setOnClickListener(v -> openRecommendationsActivity());
        binding.moreDetails.setOnClickListener(v -> openMetricsActivity());

        viewModel.getUserExists().observe(requireActivity(), this::createUser);
        viewModel.getTodayEvents().observe(requireActivity(), this::addUserEvents);
        viewModel.getUserData().observe(requireActivity(), this::getUserID);
        viewModel.getLastWeekScore().observe(requireActivity(), this::getLastWeekProductivity);
        viewModel.getProductiveHours().observe(requireActivity(), this::setProductHours);
        viewModel.getProductivityScore().observe(requireActivity(), this::setProductScore);
    }

    private void getUserID(User user){
        if(user == null) return;
        userID = user.userID;
        viewModel.fetchTodayEvents(userID);
        viewModel.fetchLastWeekProductivity(userID);
    }

    private void createUser(Boolean userExists)
    {
        if(!userExists){
            openDialog();
            viewModel.createUser(emailId, firstName,lastName, firstName + " " + lastName);
        }
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

    private void openDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Enter your details");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog, (ViewGroup) getView(), false);
        final EditText _firstName = view.findViewById(R.id.firstname);
        final EditText _lastName = view.findViewById(R.id.lastname);
        final EditText _userName = view.findViewById(R.id.user_name);
        final EditText _sleepTime = view.findViewById(R.id.sleep_hours);
        final EditText _workHours = view.findViewById(R.id.work_hours);
        final EditText _workoutHours = view.findViewById(R.id.workout_time);
        final EditText _screenHours = view.findViewById(R.id.screen_time);
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                first_name = _firstName.getText().toString();
                last_name = _lastName.getText().toString();
                user_name = _userName.getText().toString();
                screen_time = _screenHours.getText().toString();
                sleep_time = _sleepTime.getText().toString();
                work_out_time = _workoutHours.getText().toString();
                work_time = _workHours.getText().toString();

                viewModel.postIdealData(userID,
                        Integer.parseInt(sleep_time),
                        Integer.parseInt(work_time),
                        Integer.parseInt(screen_time),
                        Integer.parseInt(work_out_time));
                viewModel.updateUserDetails(userID,user_name,first_name,last_name,emailId);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void setProductHours(float productHours){
        binding.productiveHours.setText(String.format("%.1f",productHours));
    }

    private void setProductScore(float score){
        binding.productiveScore.setText((int) (score * 100.0) + "%");
    }

    private void setUserEvents(float totalDuration){
        ActivityListAdapter adapter = new ActivityListAdapter(userActivities, totalDuration);
        binding.rvActivities.setAdapter(adapter);
    }

    private void getLastWeekProductivity(Score score)
    {
        barEntries.clear();
        for(int i = 0;i < score.scores.size(); i++){
            barEntries.add(new BarEntry(i+1, score.scores.get(i)));
        }
        populateChart();
    }

    private void addUserEvents(List<Event> events){
        userActivities.clear();
        userActivities.addAll(events);

        float totalDuration = 0.0f;
        for(int i = 0;i < events.size(); i++) {
            long startTime = events.get(i).start_time;
            long endTime = events.get(i).end_time;

            float duration = (float) (endTime - startTime) / 3600.0f;
            totalDuration += duration;
        }
        setUserEvents(totalDuration);
    }

    private void setWeeklyXAxisLabel() {
        weekDaysLabel.clear();
        String[] dayOfWeek = {"S", "S", "M", "T", "W", "T", "F"};
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) % 7;
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
        Bundle b = new Bundle();
        b.putInt("userID", userID);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void openRecommendationsActivity(){
        Intent intent = new Intent(getActivity(), RecommendationsActivity.class);
        Bundle b = new Bundle();
        b.putInt("userID", userID);
        intent.putExtras(b);
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
