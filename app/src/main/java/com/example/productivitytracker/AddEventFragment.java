package com.example.productivitytracker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.productivitytracker.databinding.FragmentAddEventBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddEventFragment extends BottomSheetDialogFragment {

    FragmentAddEventBinding binding;
    Button saveButton;
    TextInputLayout startTime;
    TextInputLayout endTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddEventBinding.inflate(inflater, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        saveButton = binding.saveEvent;
        startTime = binding.startTime;
        endTime = binding.endTime;

        setListeners();
        return binding.getRoot();
    }

    private void setListeners(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });

        startTime.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(binding.startTime.getEditText());
            }
        });

        endTime.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(binding.endTime.getEditText());
            }
        });
    }

    private void saveEvent(){
        //add event to database
        dismiss();
    }

    void showTimePickerDialog(EditText editText){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                int hour = calendar.get(Calendar.HOUR);
                if(hour == 0) hour = 12;

                String min = (minute >= 10 ? "" : "0" )+ minute;

                String time = hour + " : " + min + " " + (hourOfDay >= 12 ? "PM" : "AM");
                editText.setText(time);
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }
}