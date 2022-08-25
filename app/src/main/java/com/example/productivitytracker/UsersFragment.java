package com.example.productivitytracker;

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
import com.example.productivitytracker.models.UserPost;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment
{
    FragmentUsersBinding binding;
    UserPostsAdapter.OnItemClickListener listener;
    UserViewModel viewModel;

    int userID = -1;

    public static UsersFragment newInstance(int userID) {
        UsersFragment f = new UsersFragment();
        Bundle args = new Bundle();
        args.putInt("userID", userID);
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
        userID = args.getInt("userID", 1);

        fetchData();

        setListeners();
        return binding.getRoot();

    }

    private void fetchData(){
        viewModel.fetchPosts();
    }

    private void setListeners(){
        binding.fabAddPost.setOnClickListener(v -> {
            AddPostFragment addPostFragment = AddPostFragment.newInstance(userID);
            addPostFragment.show(getParentFragmentManager(), addPostFragment.getTag());
        });

        viewModel.getAllPosts().observe(requireActivity(), this::populatePosts);
    }

    private void populatePosts(List<UserPost> posts)
    {
        setOnPostClickListener();
        UserPostsAdapter adapter = new UserPostsAdapter(posts, listener);
        binding.rvUserPosts.setAdapter(adapter);
    }

    private void setOnPostClickListener(){
        listener = (v, position, postID) -> {
            PostDetailsFragment nextFrag= PostDetailsFragment.newInstance(userID, postID);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        };
    }

    private ArrayList<UserPost> getPosts(){
        ArrayList<UserPost> userPosts = new ArrayList<>();

        /*for(int i = 1;i <= 5; i++){
            UserPost post = new UserPost();
            post.setUserName("user name");
            post.setTimeStamp("9:00 PM");
            post.setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

            userPosts.add(post);
        }*/

        return userPosts;
    }
}