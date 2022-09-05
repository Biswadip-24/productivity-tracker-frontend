package com.example.productivitytracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.service.autofill.UserData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.productivitytracker.adapters.CommentListAdapter;
import com.example.productivitytracker.databinding.FragmentPostDetailsBinding;
import com.example.productivitytracker.models.Comment;
import com.example.productivitytracker.models.User;
import com.example.productivitytracker.models.UserPost;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDetailsFragment extends Fragment
{
    FragmentPostDetailsBinding binding;
    UserViewModel viewModel;
    int userID;
    int postID;
    ArrayList<Integer> likedPosts = new ArrayList<>();
    boolean liked = false;

    private int[] likeButtonImage = {
            R.drawable.like_selected,
            R.drawable.like_unselected,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    public static PostDetailsFragment newInstance(int userID, int postID) {
        PostDetailsFragment f = new PostDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("userID", userID);
        args.putInt("postID", postID);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        Bundle args = getArguments();
        userID = args.getInt("userID", 1);
        postID = args.getInt("postID",1);

        fetchData();
        //populateData();
        setListeners();
        return binding.getRoot();
    }

    private void fetchData(){
        viewModel.fetchUserData(userID);
        viewModel.fetchPostComments(postID);
        viewModel.fetchPost(postID);
    }


    private void showComments(List<Comment> comments){
        CommentListAdapter adapter = new CommentListAdapter(comments);
        binding.rvComments.setAdapter(adapter);
    }

    private void setListeners(){
        binding.addCommentLayout.setEndIconOnClickListener(v -> {
            addComment();
            binding.addCommentLayout.getEditText().setText("");
        });

        binding.likePost.setOnClickListener(v -> likeUnlikePost());

        viewModel.getPostComments().observe(requireActivity(), this::showComments);
        viewModel.getPost().observe(requireActivity(), this::populatePost);
        viewModel.getUserData().observe(requireActivity(), this::getUserPostLikes);
    }

    private void getUserPostLikes(User user)
    {
        likedPosts.clear();
        for(int i = 0;i < user.post_likes.size(); i++){
            likedPosts.add(Integer.parseInt(user.post_likes.get(i)));
        }
        updateLikeStatus();
    }

    private void updateLikeStatus(){
        if(likedPosts.contains(postID)){
            liked = true;
            binding.likePost.setBackgroundResource(likeButtonImage[0]);
        }
        else {
            liked = false;
            binding.likePost.setBackgroundResource(likeButtonImage[1]);
        }
    }

    private void populatePost(UserPost post){
        binding.authorName.setText(post.user + "");
        Date date = new Date(Long.parseLong(post.timestamp) * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d,yyyy h:mm a");
        String time = sdf.format(date);

        binding.postTime.setText(time);
        binding.postDescription.setText(post.body);

        binding.likesCount.setText(post.numLikes + "");
    }

    private void likeUnlikePost(){
        if(!liked){
            liked = true;
            binding.likePost.setBackgroundResource(likeButtonImage[0]);
            viewModel.likePost(userID,postID);
            int likesCount = Integer.parseInt(binding.likesCount.getText().toString());
            binding.likesCount.setText(Integer.toString(likesCount + 1));
            likedPosts.add(postID);
        }
        else{
            liked = false;
            binding.likePost.setBackgroundResource(likeButtonImage[1]);
            viewModel.unLikePost(userID,postID);
            int likesCount = Integer.parseInt(binding.likesCount.getText().toString());
            binding.likesCount.setText(Integer.toString(likesCount - 1));
            likedPosts.remove(Integer.valueOf(postID));
        }
    }

    private void addComment()
    {
        viewModel.addComment(userID, postID, binding.addCommentLayout.getEditText().getText().toString());
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Comment Added", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}