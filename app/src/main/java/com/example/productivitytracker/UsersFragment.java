package com.example.productivitytracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.productivitytracker.adapters.ActivityListAdapter;
import com.example.productivitytracker.adapters.UserPostsAdapter;
import com.example.productivitytracker.databinding.FragmentUsersBinding;
import com.example.productivitytracker.models.UserPost;

import java.util.ArrayList;

public class UsersFragment extends Fragment
{
    FragmentUsersBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        populatePosts();
        return binding.getRoot();

    }

    private void populatePosts(){
        ArrayList<UserPost> userPosts = getPosts();
        UserPostsAdapter adapter = new UserPostsAdapter(userPosts);
        binding.rvUserPosts.setAdapter(adapter);
    }

    private ArrayList<UserPost> getPosts(){
        ArrayList<UserPost> userPosts = new ArrayList<>();

        for(int i = 1;i <= 5; i++){
            UserPost post = new UserPost();
            post.setUserName("user name");
            post.setTimeStamp("9:00 PM");
            post.setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

            userPosts.add(post);
        }

        return userPosts;
    }
}