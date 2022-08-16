package com.example.productivitytracker;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.productivitytracker.api.ApiClient;
import com.example.productivitytracker.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel
{
    private String TAG = "UserViewModel";

    private final MutableLiveData<User> userData = new MutableLiveData<>();

    public void fetchUserData(String userID)
    {
        ApiClient.getInstance().getApiService().getUserDetails(userID).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Error getting UserData");
            }
        });
    }

    public MutableLiveData<User> getUserData() {
        return userData;
    }
}
