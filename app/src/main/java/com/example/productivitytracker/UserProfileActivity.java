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
    private Spinner hoursSpinner;
    private Spinner hoursSpinner2;
    private Spinner hoursSpinner3;
    private Spinner hoursSpinner4;
    private static final String KEY = "Name";
    private static final String KEY_MAIL = "Email";
    private static final String KEY_AGE = "Age";

    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        View closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
            }
        });

        UserName = (EditText) findViewById(R.id.userName);
        Email = (EditText) findViewById(R.id.email_address);
        Age = (EditText) findViewById(R.id.age);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        UserName.setText(prefs.getString(KEY,""));
        Email.setText(prefs.getString(KEY_MAIL,""));
        Age.setText(prefs.getString(KEY_AGE,""));

        hoursSpinner = findViewById(R.id.hours_spinner);
        hoursSpinner2 = findViewById(R.id.hours_spinner2);
        hoursSpinner3= findViewById(R.id.hours_spinner3);
        hoursSpinner4 = findViewById(R.id.hours_spinner4);

        ArrayList<Integer> hours = new ArrayList<>();
        for(int i=1;i<=15;i++){
            hours.add(i);
        }
        ArrayAdapter<Integer> hoursAdapter =new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item,hours
        );
        hoursSpinner.setAdapter(hoursAdapter);
        hoursSpinner2.setAdapter(hoursAdapter);
        hoursSpinner3.setAdapter(hoursAdapter);
        hoursSpinner4.setAdapter(hoursAdapter);

    }

    public void saveChanges(View v) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY,UserName.getText().toString());
        editor.putString(KEY_MAIL,Email.getText().toString());
        editor.putString(KEY_AGE,Age.getText().toString());
        editor.commit();
        Toast.makeText(UserProfileActivity.this, "Changes made successfully.", Toast.LENGTH_SHORT).show();
    }}