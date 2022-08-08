package com.example.productivitytracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productivitytracker.R;
import com.example.productivitytracker.models.UserPost;

import java.util.ArrayList;

public class UserPostsAdapter extends RecyclerView.Adapter<UserPostsAdapter.ViewHolder> {

    private ArrayList<UserPost> userPosts;

    public UserPostsAdapter(ArrayList<UserPost> userPosts){
        this.userPosts = userPosts;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView postText;
        private final ImageView authorImage;
        private final TextView postTime;
        private final TextView authorName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postText = itemView.findViewById(R.id.post_body);
            authorImage = itemView.findViewById(R.id.author_image);
            authorName = itemView.findViewById(R.id.author_name);
            postTime = itemView.findViewById(R.id.post_time);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = userPosts.get(position).getUserName();
        String body = userPosts.get(position).getBody();
        String time = userPosts.get(position).getTimeStamp();

        holder.authorName.setText(name);
        holder.postTime.setText(time);
        holder.postText.setText(body);
    }

    @Override
    public int getItemCount() {
        return userPosts.size();
    }

}
