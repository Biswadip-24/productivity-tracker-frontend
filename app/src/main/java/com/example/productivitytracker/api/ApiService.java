package com.example.productivitytracker.api;

import com.example.productivitytracker.models.Event;
import com.example.productivitytracker.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    String BASE_URL = ""; // Need to be changed later

    @GET("/api/user/{userID}/")
    Call<User> getUserDetails(@Path("userID") String userId);

    @GET("/api/userTodayEvents/{userID}/")
    Call<List<Event>> getTodayEvents(@Path("userID") String userId);



}
