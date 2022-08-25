package com.example.productivitytracker;

import android.util.ArrayMap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.productivitytracker.api.ApiClient;
import com.example.productivitytracker.models.Comment;
import com.example.productivitytracker.models.Event;
import com.example.productivitytracker.models.User;
import com.example.productivitytracker.models.UserPost;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel
{
    private String TAG = "UserViewModel";

    private final MutableLiveData<User> userData = new MutableLiveData<>();
    private final MutableLiveData<List<Event>> todayEvents = new MutableLiveData<>();
    private final MutableLiveData<List<Event>> events = new MutableLiveData<>();
    private final MutableLiveData<Float> productiveHours = new MutableLiveData<>();
    private final MutableLiveData<HashMap<String, Long>> types = new MutableLiveData<>();
    private final MutableLiveData<Float> productivityScore = new MutableLiveData<>();
    private final MutableLiveData<List<UserPost>> allPosts = new MutableLiveData<>();
    private final MutableLiveData<UserPost> post = new MutableLiveData<>();
    private final MutableLiveData<List<Comment>> postComments = new MutableLiveData<>();

    public void fetchUserData(int userID)
    {
        ApiClient.getInstance().getApiService().getUserDetails(userID).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void fetchTodayEvents(int userID)
    {
        ApiClient.getInstance().getApiService().getEvents(userID, System.currentTimeMillis()/1000).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                todayEvents.setValue(response.body());
                calculateProductiveHours();
                calculateTypes();
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.e(TAG, "Error getting Today's Events");
            }
        });
    }

    public void fetchEvents(int userID, long timeStamp){
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

    public void addEvent(Map<String, Object> jsonParams, int userID){

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
        ApiClient.getInstance().getApiService().addEvent(body).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                Log.e(TAG, "Event successfully added");
                fetchTodayEvents(userID);
            }
            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Log.e(TAG, "Error adding Event");
            }
        });

    }

    public void fetchPosts(){
        ApiClient.getInstance().getApiService().getALlPosts().enqueue(new Callback<List<UserPost>>() {
            @Override
            public void onResponse(Call<List<UserPost>> call, Response<List<UserPost>> response) {
                allPosts.setValue(response.body());
            }
            @Override
            public void onFailure(Call<List<UserPost>> call, Throwable t) {
                Log.e(TAG, "Error getting all posts");
            }
        });
    }

    public void fetchPost(int postID){
       ApiClient.getInstance().getApiService().getPost(postID).enqueue(new Callback<UserPost>() {
           @Override
           public void onResponse(Call<UserPost> call, Response<UserPost> response) {
               post.setValue(response.body());
           }
           @Override
           public void onFailure(Call<UserPost> call, Throwable t) {
                Log.e(TAG, "Failed to get post "+ postID);
           }
       });
    }

    public void fetchPostComments(int postID){
        ApiClient.getInstance().getApiService().getPostComments(postID).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                postComments.setValue(response.body());
                Log.d(TAG, response.body().toString());
            }
            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.e(TAG, "Failed to get comments");
            }
        });
    }

    public void addComment(int userID, int postID, String text){
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("post", postID);
        jsonParams.put("user", userID);
        jsonParams.put("body", text);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        ApiClient.getInstance().getApiService().addComment(body).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                fetchPostComments(postID);
                Log.d(TAG, "Comment added");
            }
            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.e(TAG, "Failed to add comment");
            }
        });
    }

    public void addPost(int userID, String text){
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("user", userID);
        jsonParams.put("body", text);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        ApiClient.getInstance().getApiService().addPost(body).enqueue(new Callback<UserPost>() {
            @Override
            public void onResponse(Call<UserPost> call, Response<UserPost> response) {
                Log.d(TAG, "Post added");
                fetchPosts();
            }
            @Override
            public void onFailure(Call<UserPost> call, Throwable t) {
                Log.e(TAG, "Failed to add post");
            }
        });
    }

    private void calculateProductiveHours() {
        List<Event> list = todayEvents.getValue();
        float totalDuration = 0.0f;
        for(int i = 0;list != null && i < list.size(); i++){
            long startTime = list.get(i).start_time;
            long endTime = list.get(i).end_time;

            float duration = (float) (endTime - startTime) / 3600.0f;
            totalDuration += duration;
        }
        productiveHours.setValue(totalDuration);
        calculateProdScore();
    }

    private void calculateProdScore(){
        int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        productivityScore.setValue(productiveHours.getValue() / (float)hours);
    }

    private void calculateTypes(){
        List<Event> list = todayEvents.getValue();
        HashMap<String,Long> map = new HashMap<>();

        for(int i = 0; list != null && i < list.size() ; i++)
        {
            long startTime = list.get(i).start_time;
            long endTime = list.get(i).end_time;
            long duration = endTime - startTime;
            String type = list.get(i).type;

            if(map.containsKey(type)) map.put(type, map.get(type) + duration);
            else map.put(type, duration);
        }
        types.setValue(map);
    }


    public MutableLiveData<User> getUserData() {
        return userData;
    }
    public MutableLiveData<List<Event>> getTodayEvents() {return todayEvents;}
    public MutableLiveData<List<Event>> getEvents() {return events; }
    public MutableLiveData<Float> getProductiveHours() {return productiveHours;}
    public MutableLiveData<Float> getProductivityScore() {return productivityScore;}
    public MutableLiveData<HashMap<String,Long>> getTypes(){return types;}
    public MutableLiveData<List<UserPost>> getAllPosts(){return allPosts;}
    public MutableLiveData<UserPost> getPost(){return post;}
    public MutableLiveData<List<Comment>> getPostComments(){return postComments;}
}
