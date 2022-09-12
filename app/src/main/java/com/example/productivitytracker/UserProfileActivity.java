package com.example.productivitytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.service.autofill.UserData;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.productivitytracker.databinding.ActivityMetricsBinding;
import com.example.productivitytracker.databinding.ActivityUserProfileBinding;
import com.example.productivitytracker.models.IdealData;
import com.example.productivitytracker.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {
    private EditText fullName;
    private EditText UserName;
    private EditText Age;
    private EditText Email;
    private static final Spinner[] hoursSpinner = new Spinner[4];
    private static final String KEY = "Name";
    private static final String KEY_MAIL = "Email";
    private static final String KEY_AGE = "Age";
    //TODO: Rename parameters appropriately
    private static final String[] KEY_HOURS = {"Sleep_Hours","Work_Hours","Screen_Hours","Workout_Hours"};
    private SharedPreferences prefs;

    int userID;
    boolean viewer = false;
    ActivityUserProfileBinding binding;
    UserViewModel viewModel;
    boolean newUser = false;
    boolean initialSet = false;
    int idealDataID;
    String firstName, lastName;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        View closeButton = findViewById(R.id.close_button);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getInt("userID");
            viewer = b.getBoolean("viewer", false);
        }

        if(viewer) setViewerMode();

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.fetchUserData(userID);
        viewModel.fetchIdealData(userID);

        setListeners();

        closeButton.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });

        fullName = findViewById(R.id.fullName);
        UserName = findViewById(R.id.userName);
        Email = findViewById(R.id.email_address);
        Age = findViewById(R.id.age);
        //TODO:To Rename
        hoursSpinner[0] = findViewById(R.id.hours_spinner);
        hoursSpinner[1] = findViewById(R.id.hours_spinner2);
        hoursSpinner[2]= findViewById(R.id.hours_spinner3);
        hoursSpinner[3] = findViewById(R.id.hours_spinner4);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        UserName.setText(prefs.getString(KEY,""));
        Email.setText(prefs.getString(KEY_MAIL,""));
        Age.setText(prefs.getString(KEY_AGE,""));

        setSpinners();
    }

    private void setListeners(){
        viewModel.getUserData().observe(this, this::populateUserData);
        viewModel.getUserIdealData().observe(this, this::setUserIdealData);

        binding.logOut.setOnClickListener(v -> signOut());
    }

    private void setViewerMode(){
        binding.userGoals.setVisibility(View.GONE);
        binding.age.setInputType(InputType.TYPE_NULL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.age.setFocusable(View.NOT_FOCUSABLE);
        }
    }

    private void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent i = new Intent(UserProfileActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    private void setUserIdealData(List<IdealData> data){
        if(data!= null && data.isEmpty()) {
            newUser = true;
            for(int i=0;i<hoursSpinner.length;i++) {
                hoursSpinner[0].setSelection(0);
            }
        }
        else if(!initialSet){
            idealDataID = data.get(data.size() - 1).id;
            newUser = false;
            initialSet = true;

            hoursSpinner[0].setSelection(data.get(data.size() - 1).sleep_time - 1);
            hoursSpinner[1].setSelection(data.get(data.size() - 1).work_hours - 1);
            hoursSpinner[2].setSelection(data.get(data.size() - 1).screen_time - 1);
            hoursSpinner[3].setSelection(data.get(data.size() - 1).workout_hours - 1);
        }
    }

    private void populateUserData(User userData){
        firstName = userData.firstName;
        lastName = userData.lastName;

        fullName.setText(userData.firstName + " " + userData.lastName);
        UserName.setText(userData.userName);
        Email.setText(userData.email);
    }

    public void setSpinners(){
        ArrayList<Integer> hours = new ArrayList<>();
        for(int i=1;i<=15;i++){
            hours.add(i);
        }
        ArrayAdapter<Integer> hoursAdapter =new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item,hours
        );

        for(int i=0;i<hoursSpinner.length;i++){
            hoursSpinner[i].setAdapter(hoursAdapter);
            //hoursSpinner[i].setSelection(prefs.getInt(KEY_HOURS[i],0));
        }
    }

    public void updateUserInfo(){
        viewModel.updateUserDetails(userID, binding.userName.getText().toString(), firstName, lastName, binding.emailAddress.getText().toString());
    }

    public void saveChanges(View v)
    {
        int idealData[] = new int[4];
        for(int i = 0;i < 4; i++){
            idealData[i] = hoursSpinner[i].getSelectedItemPosition() + 1;
        }
        if(newUser) viewModel.postIdealData(userID, idealData[0],idealData[1],idealData[2], idealData[3]);
        else viewModel.updateIdealData(userID,idealData[0],idealData[1],idealData[2], idealData[3], idealDataID);

        updateUserInfo();

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY,UserName.getText().toString());
        editor.putString(KEY_MAIL,Email.getText().toString());
        editor.putString(KEY_AGE,Age.getText().toString());
        for(int i=0;i<hoursSpinner.length;i++){
            editor.putInt(KEY_HOURS[i],hoursSpinner[i].getSelectedItemPosition());

        editor.apply();
        Toast.makeText(UserProfileActivity.this, "Changes made successfully.", Toast.LENGTH_SHORT).show();
    }}
}