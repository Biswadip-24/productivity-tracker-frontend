package com.example.productivitytracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productivitytracker.R;
import com.example.productivitytracker.models.Recommendation;

import java.util.ArrayList;

public class RecommendationsAdapter extends RecyclerView.Adapter<RecommendationsAdapter.ViewHolder>{
    private ArrayList<Recommendation> recommendations;

    public RecommendationsAdapter(ArrayList<Recommendation> recommendations){
        this.recommendations = recommendations;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView postText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postText = itemView.findViewById(R.id.recommendations_text);
        }
    }

    @NonNull
    @Override
    public RecommendationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendations_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationsAdapter.ViewHolder holder, int position) {
        String body = recommendations.get(position).getBody();
        holder.postText.setText(body);
    }

    @Override
    public int getItemCount() {
        return recommendations.size();
    }
}
