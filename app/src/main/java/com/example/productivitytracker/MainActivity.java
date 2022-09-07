package com.example.productivitytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.productivitytracker.databinding.ActivityMainBinding;
import com.example.productivitytracker.models.User;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private UserViewModel viewModel;
    int userID = 1; // change this id according to your local database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        setContentView(binding.getRoot());

        //setUp();
        replaceFragment(HomeFragment.newInstance(userID));
        setListeners();
    }

    private void setListeners(){
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(HomeFragment.newInstance(userID));
                    break;
                case R.id.users:
                    replaceFragment(UsersFragment.newInstance(userID));
                    break;
            }

            return true;
        });
    }

    private void setUp(){
        viewModel.fetchUserData(userID);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
