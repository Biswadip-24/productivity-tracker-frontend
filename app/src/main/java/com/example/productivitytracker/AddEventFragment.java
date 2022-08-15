package com.example.productivitytracker;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

    String[] eventType = {"Study", "Entertainment", "Gym", "Others"};

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
        saveButton.setOnClickListener(v -> saveEvent());
        startTime.setEndIconOnClickListener(v -> showTimePickerDialog(binding.startTime.getEditText()));
        endTime.setEndIconOnClickListener(v -> showTimePickerDialog(binding.endTime.getEditText()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.spinner_dropdown_item,
                eventType);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.eventSpinner.setAdapter(adapter);
    }

    private void saveEvent(){
        //add event to database
        dismiss();
    }

    void showTimePickerDialog(EditText editText){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute1) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            int hour1 = calendar.get(Calendar.HOUR);
            if(hour1 == 0) hour1 = 12;

            String min = (minute1 >= 10 ? "" : "0" )+ minute1;

            String time = hour1 + " : " + min + " " + (hourOfDay >= 12 ? "PM" : "AM");
            editText.setText(time);
        }, hour, minute, false);

        timePickerDialog.show();
    }
}