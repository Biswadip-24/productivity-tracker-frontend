package com.example.productivitytracker.api;

import com.example.productivitytracker.models.Event;
import com.example.productivitytracker.models.User;
import com.example.productivitytracker.models.UserPost;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    String BASE_URL = "http://10.0.2.2:8000"; // Need to be changed later

    @GET("/api/user/{userID}/")
    Call<User> getUserDetails(@Path("userID") int userId);

    @GET("/api/event/{userID}/{timeStamp}")
    Call<List<Event>> getEvents(@Path("userID") int userId, @Path("timeStamp") long timeStamp);

    @POST("/api/event/")
    Call<Event> addEvent(@Body RequestBody params);

    @GET("/api/post/")
    Call<List<UserPost>> getALlPosts();
}
