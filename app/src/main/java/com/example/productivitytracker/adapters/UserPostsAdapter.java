package com.example.productivitytracker.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productivitytracker.PostDetailsFragment;
import com.example.productivitytracker.R;
import com.example.productivitytracker.models.UserPost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserPostsAdapter extends RecyclerView.Adapter<UserPostsAdapter.ViewHolder> {

    private List<UserPost> userPosts;
    private OnItemClickListener listener;

    public UserPostsAdapter(List<UserPost> userPosts, OnItemClickListener listener){
        this.userPosts = userPosts;
        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView postText;
        private final ImageView authorImage;
        private final TextView postTime;
        private final TextView authorName;
        private final TextView commentsCount;
        private final TextView likesCount;
        private int postID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postText = itemView.findViewById(R.id.post_body);
            authorImage = itemView.findViewById(R.id.author_image);
            authorName = itemView.findViewById(R.id.author_name);
            postTime = itemView.findViewById(R.id.post_time);
            commentsCount = itemView.findViewById(R.id.comments);
            likesCount = itemView.findViewById(R.id.likes);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition(), postID);
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
        String name = userPosts.get(position).user + "";
        String body = userPosts.get(position).body;

        Date date = new Date(Long.parseLong(userPosts.get(position).timestamp) * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d,yyyy h:mm a");
        String time = sdf.format(date);

        int commentCount = userPosts.get(position).numComments;
        int likesCount = userPosts.get(position).numLikes;

        holder.authorName.setText(name);
        holder.postTime.setText(time);
        holder.postText.setText(body);
        holder.commentsCount.setText(commentCount + " comments");
        holder.likesCount.setText(likesCount + " likes");
        holder.postID = userPosts.get(position).postID;

    }

    @Override
    public int getItemCount() {
        return userPosts.size();
    }

    public interface OnItemClickListener{
        void onClick(View v, int position, int postID);
    }

}
