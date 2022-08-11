package com.example.productivitytracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.productivitytracker.adapters.CommentListAdapter;
import com.example.productivitytracker.databinding.FragmentPostDetailsBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class PostDetailsFragment extends Fragment
{
    FragmentPostDetailsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false);
        populateData();
        setListeners();
        return binding.getRoot();
    }

    private void populateData(){
        getComments();
    }

    private void getComments(){
        ArrayList<String> comments = new ArrayList<>();
        for(int i = 0;i < 6; i++){
            comments.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        }

        CommentListAdapter adapter = new CommentListAdapter(comments);
        binding.rvComments.setAdapter(adapter);
    }

    private void setListeners(){
        binding.addCommentLayout.setEndIconOnClickListener(v -> {
            addComment();
            binding.addCommentLayout.getEditText().setText("");
        });
    }

    private void addComment(){
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Comment Added", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}