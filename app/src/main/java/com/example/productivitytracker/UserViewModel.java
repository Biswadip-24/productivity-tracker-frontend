package com.example.productivitytracker;

import android.util.ArrayMap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.productivitytracker.api.ApiClient;
import com.example.productivitytracker.api.ApiService;
import com.example.productivitytracker.models.Comment;
import com.example.productivitytracker.models.Event;
import com.example.productivitytracker.models.IdealData;
import com.example.productivitytracker.models.Score;
import com.example.productivitytracker.models.User;
import com.example.productivitytracker.models.UserPost;
import com.google.android.gms.common.api.Api;

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
    private final MutableLiveData<List<String>> recommendations = new MutableLiveData<>();
    private final MutableLiveData<Score> lastWeekScore = new MutableLiveData<>();
    private final MutableLiveData<List<IdealData>> userIdealData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> userExists = new MutableLiveData<>();

    private String[] eventType = {"Study", "Entertainment", "Gym", "Others"};

    public void fetchUserData(int userID)
    {
        ApiClient.getInstance().getApiService().getUserDetails(userID).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userData.setValue(response.body());
                userExists.setValue(true);
                //fetchIdealData(userID);
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void createUser(String email, String firstName, String lastName, String userName){
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("email", email);
        jsonParams.put("firstName", firstName);
        jsonParams.put("lastName", lastName);
        jsonParams.put("userName", userName);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        ApiClient.getInstance().getApiService().addUser(body).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "user created " + response.toString());
                userData.setValue(response.body());
                userExists.setValue(true);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Failed to add user");
            }
        });

    }

    public void fetchUserDataByEmail(String email)
    {
        ApiClient.getInstance().getApiService().getUserDetailsByEmail(email).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.body().isEmpty()) userExists.setValue(false);
                else {
                    userData.setValue(response.body().get(0));
                    userExists.setValue(true);
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void updateUserDetails(int userId, String userName, String firstname, String lastName, String email)
    {
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("userName", userName);
        jsonParams.put("email", email);
        jsonParams.put("firstName", firstname);
        jsonParams.put("lastName", lastName);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        ApiClient.getInstance().getApiService().updateUserDetails(userId, body).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Failed update user data");
            }
        });
    }

    public void fetchLastWeekProductivity(int userId)
    {
        ApiClient.getInstance().getApiService().getLastWeekProductivity(userId).enqueue(new Callback<Score>() {
            @Override
            public void onResponse(Call<Score> call, Response<Score> response) {
                lastWeekScore.setValue(response.body());
            }
            @Override
            public void onFailure(Call<Score> call, Throwable t) {
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
                fetchIdealData(userID);
                calculateProductiveHours(userID);
                calculateTypes();
                //generateRecommendations();
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
        jsonParams.put("postID", postID);
        jsonParams.put("userID", userID);
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

    public void likePost(int userID, int postID){
        ApiClient.getInstance().getApiService().likePost(postID,userID).enqueue(new Callback<UserPost>() {
            @Override
            public void onResponse(Call<UserPost> call, Response<UserPost> response) {
                Log.d(TAG, "Post liked " + response);
            }
            @Override
            public void onFailure(Call<UserPost> call, Throwable t) {
                Log.e(TAG, "Failed to like post");
            }
        });
    }

    public void unLikePost(int userID, int postID){
        ApiClient.getInstance().getApiService().unLikePost(postID,userID).enqueue(new Callback<UserPost>() {
            @Override
            public void onResponse(Call<UserPost> call, Response<UserPost> response) {
                Log.d(TAG, "Post unliked");
            }
            @Override
            public void onFailure(Call<UserPost> call, Throwable t) {
                Log.e(TAG, "Failed to unlike post");
            }
        });
    }

    public void postIdealData(int user, int sleep_time, int work_hours, int screen_time, int workout_hours){
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("user", user);
        jsonParams.put("sleep_time", sleep_time);
        jsonParams.put("work_hours", work_hours);
        jsonParams.put("screen_time", screen_time);
        jsonParams.put("workout_hours", workout_hours);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        ApiClient.getInstance().getApiService().postIdealData(body).enqueue(new Callback<IdealData>() {
            @Override
            public void onResponse(Call<IdealData> call, Response<IdealData> response) {
                Log.d(TAG, "Ideal data added");
            }
            @Override
            public void onFailure(Call<IdealData> call, Throwable t) {
                Log.d(TAG, "Failed to add ideal data");
            }
        });
    }

    public void updateIdealData(int user, int sleep_time, int work_hours, int screen_time, int workout_hours, int id){
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("user", user);
        jsonParams.put("sleep_time", sleep_time);
        jsonParams.put("work_hours", work_hours);
        jsonParams.put("screen_time", screen_time);
        jsonParams.put("workout_hours", workout_hours);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        ApiClient.getInstance().getApiService().updateIdealData(id, body).enqueue(new Callback<IdealData>() {
            @Override
            public void onResponse(Call<IdealData> call, Response<IdealData> response) {
                Log.d(TAG, "Ideal data updated ");
            }
            @Override
            public void onFailure(Call<IdealData> call, Throwable t) {
                Log.d(TAG, "Failed to update ideal data");
            }
        });
    }

    public void fetchIdealData(int userId){
        ApiClient.getInstance().getApiService().getIdealData(userId).enqueue(new Callback<List<IdealData>>() {
            @Override
            public void onResponse(Call<List<IdealData>> call, Response<List<IdealData>> response) {
                userIdealData.setValue(response.body());
                calculateProductiveHours(userId);
                //calculateProdScore();
                generateRecommendations();
            }
            @Override
            public void onFailure(Call<List<IdealData>> call, Throwable t) {
                Log.e(TAG, "Error getting user ideal data");
            }
        });
    }

    public void addPost(int userID, String text){
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("user", userID);
        jsonParams.put("body", text);
        jsonParams.put("timestamp", System.currentTimeMillis()/1000);
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

    private void calculateProductiveHours(int userID) {
        List<Event> list = todayEvents.getValue();
        //fetchIdealData(userID);
        float totalDuration = 0.0f;
        for(int i = 0;list != null && i < list.size(); i++)
        {
            if(list.get(i).type.equalsIgnoreCase(eventType[0]) || list.get(i).type.equalsIgnoreCase(eventType[2])){
                long startTime = list.get(i).start_time;
                long endTime = list.get(i).end_time;

                float duration = (float) (endTime - startTime) / 3600.0f;
                totalDuration += duration;
            }
        }
        productiveHours.setValue(totalDuration);
        calculateProdScore();
    }

    private void calculateProdScore()
    {
        int sleepTime = 0;
        if(userIdealData.getValue() != null && !userIdealData.getValue().isEmpty()){
            sleepTime = userIdealData.getValue().get(userIdealData.getValue().size() - 1).sleep_time;
        }
        int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - sleepTime;
        if(hours < sleepTime) productivityScore.setValue(1.0f);
        else productivityScore.setValue(productiveHours.getValue() / (float)hours);

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

    private void generateRecommendations()
    {
        IdealData data;
        if(userIdealData.getValue() == null || userIdealData.getValue().isEmpty()){
            data = new IdealData();
            data.sleep_time = 0;
            data.id = -1;
            data.screen_time = 0;
            data.workout_hours = 0;
            data.work_hours = 0;
        }
        else data = userIdealData.getValue().get(userIdealData.getValue().size() - 1);

        RecommendationsService service = new RecommendationsService(todayEvents.getValue(), data);
        recommendations.setValue(service.getRecommendations());
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
    public MutableLiveData<List<String>> getRecommendations(){return recommendations;}
    public MutableLiveData<Score> getLastWeekScore() {return lastWeekScore;}
    public MutableLiveData<List<IdealData>> getUserIdealData() {return userIdealData;}
    public MutableLiveData<Boolean> getUserExists(){return userExists;}
}
