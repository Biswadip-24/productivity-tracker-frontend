package com.example.productivitytracker.api;

import android.provider.CalendarContract;

import com.example.productivitytracker.models.Comment;
import com.example.productivitytracker.models.Event;
import com.example.productivitytracker.models.Events;
import com.example.productivitytracker.models.IdealData;
import com.example.productivitytracker.models.Score;
import com.example.productivitytracker.models.User;
import com.example.productivitytracker.models.UserPost;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    String BASE_URL = "http://10.0.2.2:8000"; // Need to be changed later

    @GET("/api/user/{userID}/")
    Call<User> getUserDetails(@Path("userID") int userId);

    @POST("/api/user/")
    Call<User> addUser(@Body RequestBody params);

    @PUT("/api/user/{userID}/")
    Call<User> updateUserDetails(@Path("userID") int userId, @Body RequestBody params);

    @GET("/api/user/{email}/")
    Call<List<User>> getUserDetailsByEmail(@Path("email") String emailId);

    @GET("/api/lastWeekProductivity/{userID}/")
    Call<Score> getLastWeekProductivity(@Path("userID") int userId);

    @POST("/api/idealData/")
    Call<IdealData> postIdealData(@Body RequestBody params);

    @PUT("/api/idealData/{id}/")
    Call<IdealData> updateIdealData(@Path("id") int id, @Body RequestBody params);

    @GET("/api/userIdealData/{userID}/")
    Call<List<IdealData>> getIdealData(@Path("userID") int userId);

    @GET("/api/event/{userID}/{timeStamp}/")
    Call<Events> getEvents(@Path("userID") int userId, @Path("timeStamp") long timeStamp);

    @POST("/api/addEvent/")
    Call<Event> addEvent(@Body RequestBody params);

    @GET("/api/getPost/")
    Call<List<UserPost>> getALlPosts();

    @GET("/api/getPost/{postID}")
    Call<UserPost> getPost(@Path("postID") int postID);

    @GET("/api/postComments/{postID}/")
    Call<List<Comment>> getPostComments(@Path("postID") int postID);

    @POST("/api/addComment/")
    Call<Comment> addComment(@Body RequestBody params);

    @POST("/api/post/")
    Call<UserPost> addPost(@Body RequestBody params);

    @POST("/api/likePost/{postID}/{userID}/")
    Call<UserPost> likePost(@Path("postID") int postID, @Path("userID") int userId);

    @POST("/api/unLikePost/{postID}/{userID}/")
    Call<UserPost> unLikePost(@Path("postID") int postID, @Path("userID") int userId);
}
