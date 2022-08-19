package com.example.productivitytracker;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.productivitytracker.api.ApiClient;
import com.example.productivitytracker.models.Event;
import com.example.productivitytracker.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel
{
    private String TAG = "UserViewModel";

    private final MutableLiveData<User> userData = new MutableLiveData<>();
    private final MutableLiveData<List<Event>> todayEvents = new MutableLiveData<>();
    private final MutableLiveData<List<Event>> events = new MutableLiveData<>();

    public void fetchUserData(int userID)
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

    public void fetchTodayEvents(int userID)
    {
        String timeStamp = Long.toString(System.currentTimeMillis() / 1000);
        ApiClient.getInstance().getApiService().getEvents(userID, timeStamp).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                todayEvents.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.e(TAG, "Error getting Today's Events");
            }
        });
    }

    public void fetchEvents(int userID, String timeStamp){
        ApiClient.getInstance().getApiService().getEvents(userID, timeStamp).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                events.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.e(TAG, "Error getting Events");
            }
        });
    }



    public MutableLiveData<User> getUserData() {
        return userData;
    }
    public MutableLiveData<List<Event>> getTodayEvents() {return todayEvents;}
    public MutableLiveData<List<Event>> getEvents() {return events; }

}
