package com.example.productivitytracker;

import android.util.Log;

import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel
{
    private String TAG = "UserViewModel";
    public UserViewModel(){
        Log.d(TAG, "ViewModel created");
    }
}
