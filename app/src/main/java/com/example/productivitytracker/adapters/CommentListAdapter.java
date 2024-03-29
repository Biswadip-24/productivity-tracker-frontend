package com.example.productivitytracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productivitytracker.R;
import com.example.productivitytracker.models.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder>{
    private List<Comment> comments;

    public CommentListAdapter(List<Comment> comments){
        this.comments = comments;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView tvComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.commentText);
        }
    }

    @NonNull
    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListAdapter.ViewHolder holder, int position) {
        String body = comments.get(position).body;
        holder.tvComment.setText(body);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
