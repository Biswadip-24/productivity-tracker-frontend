package com.example.productivitytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        View closeButton = findViewById(R.id.close_button);

        closeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                finish();
            }
        });

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
            hoursSpinner[i].setSelection(prefs.getInt(KEY_HOURS[i],0));
        }
    }

    public void saveChanges(View v) {
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