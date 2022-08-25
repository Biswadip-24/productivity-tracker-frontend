package com.example.productivitytracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.productivitytracker.adapters.CommentListAdapter;
import com.example.productivitytracker.databinding.FragmentPostDetailsBinding;
import com.example.productivitytracker.models.Comment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class PostDetailsFragment extends Fragment
{
    FragmentPostDetailsBinding binding;
    UserViewModel viewModel;
    int userID;
    int postID;

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

        viewModel.fetchPostComments(postID);
    }

    /*private void populateData(){
        getComments();
    }*/

    private void showComments(List<Comment> comments){
        /*ArrayList<String> comments = new ArrayList<>();
        for(int i = 0;i < 6; i++){
            comments.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        }*/

        CommentListAdapter adapter = new CommentListAdapter(comments);
        binding.rvComments.setAdapter(adapter);
    }

    private void setListeners(){
        binding.addCommentLayout.setEndIconOnClickListener(v -> {
            addComment();
            binding.addCommentLayout.getEditText().setText("");
        });

        viewModel.getPostComments().observe(requireActivity(), this::showComments);
    }

    private void addComment()
    {
        viewModel.addComment(userID, postID, binding.addCommentLayout.getEditText().getText().toString());
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Comment Added", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}