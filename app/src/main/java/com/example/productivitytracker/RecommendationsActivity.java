package com.example.productivitytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.productivitytracker.adapters.RecommendationsAdapter;
import com.example.productivitytracker.databinding.ActivityRecommendationsBinding;
import com.example.productivitytracker.models.Recommendation;

import java.util.ArrayList;

public class RecommendationsActivity extends AppCompatActivity
{
    ActivityRecommendationsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecommendationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        populateRecommendations();
    }

    private void populateRecommendations(){
        RecommendationsAdapter adapter = new RecommendationsAdapter(getRecommendations());
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