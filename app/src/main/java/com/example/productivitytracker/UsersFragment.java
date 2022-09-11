package com.example.productivitytracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.productivitytracker.adapters.ActivityListAdapter;
import com.example.productivitytracker.adapters.UserPostsAdapter;
import com.example.productivitytracker.databinding.FragmentUsersBinding;
import com.example.productivitytracker.models.User;
import com.example.productivitytracker.models.UserPost;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment
{
    FragmentUsersBinding binding;
    UserPostsAdapter.OnItemClickListener listener;
    UserPostsAdapter.OnAuthorClickListener authorClickListener;
    UserViewModel viewModel;

    int userID = -1;
    String emailId;

    public static UsersFragment newInstance(String emailId) {
        UsersFragment f = new UsersFragment();
        Bundle args = new Bundle();
        args.putString("emailId", emailId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        Bundle args = getArguments();
        emailId = args.getString("emailId");
        //userID = args.getInt("userID", 1);

        fetchData();

        setListeners();
        return binding.getRoot();

    }

    private void fetchData(){
        viewModel.fetchUserDataByEmail(emailId);
        viewModel.fetchPosts();
    }

    private void setListeners(){
        binding.fabAddPost.setOnClickListener(v -> {
            AddPostFragment addPostFragment = AddPostFragment.newInstance(userID);
            addPostFragment.show(getParentFragmentManager(), addPostFragment.getTag());
        });

        viewModel.getUserData().observe(requireActivity(), this::getUserID);
        viewModel.getAllPosts().observe(requireActivity(), this::populatePosts);
    }

    private void getUserID(User user){
        userID = user.userID;
    }

    private void populatePosts(List<UserPost> posts)
    {
        setOnPostClickListener();
        UserPostsAdapter adapter = new UserPostsAdapter(posts, listener, authorClickListener);
        binding.rvUserPosts.setAdapter(adapter);
    }

    private void setOnPostClickListener(){
        listener = (v, position, postID, authorID) -> {
            PostDetailsFragment nextFrag= PostDetailsFragment.newInstance(userID, postID, authorID);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        };

        authorClickListener = (v, position, postID, userID) -> {
            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
            Bundle b = new Bundle();
            b.putInt("userID", userID);
            b.putBoolean("viewer",true);
            intent.putExtras(b);
            startActivity(intent);
        };
    }
}