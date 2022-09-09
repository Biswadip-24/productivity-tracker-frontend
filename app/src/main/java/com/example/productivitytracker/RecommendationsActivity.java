package com.example.productivitytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.productivitytracker.adapters.RecommendationsAdapter;
import com.example.productivitytracker.databinding.ActivityRecommendationsBinding;
import com.example.productivitytracker.models.Recommendation;

import java.util.ArrayList;
import java.util.List;

public class RecommendationsActivity extends AppCompatActivity
{
    ActivityRecommendationsBinding binding;
    UserViewModel viewModel;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecommendationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        Bundle b = getIntent().getExtras();
        if(b != null) userID = b.getInt("userID");

        viewModel.fetchUserData(userID);
        viewModel.fetchTodayEvents(userID);

        setListeners();
        //populateRecommendations();
    }

    private void setListeners(){
        viewModel.getRecommendations().observe(this, this::populateRecommendations);
    }

    private void populateRecommendations(List<String> recommendations){
        //RecommendationsAdapter adapter = new RecommendationsAdapter(getRecommendations());
        RecommendationsAdapter adapter = new RecommendationsAdapter(recommendations);
        binding.rvRecommendations.setAdapter(adapter);

    }

    // dummy data as of now
    ArrayList<Recommendation> getRecommendations(){
        ArrayList<Recommendation> recommendations = new ArrayList<>();
        for(int i = 1;i <= 6; i++){
            Recommendation r = new Recommendation();
            r.setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
            recommendations.add(r);
        }

        return recommendations;
    }


}