package com.example.productivitytracker;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.lifecycle.ViewModelProvider;

import com.example.productivitytracker.databinding.FragmentAddEventBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddEventFragment extends BottomSheetDialogFragment {

    FragmentAddEventBinding binding;
    Button saveButton;
    TextInputLayout startTime;
    TextInputLayout endTime;
    long startT, endT;

    String[] eventType = {"Study", "Entertainment", "Gym", "Others"};
    private int userID = -1;
    UserViewModel viewModel;

    public static AddEventFragment newInstance(int userID) {
        AddEventFragment f = new AddEventFragment();
        Bundle args = new Bundle();
        args.putInt("userID", userID);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddEventBinding.inflate(inflater, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        Bundle args = getArguments();
        userID = args.getInt("userID", 1);

        saveButton = binding.saveEvent;
        startTime = binding.startTime;
        endTime = binding.endTime;

        setListeners();
        return binding.getRoot();
    }

    private void setListeners(){
        saveButton.setOnClickListener(v -> saveEvent());
        startTime.setEndIconOnClickListener(v -> showTimePickerDialog(binding.startTime.getEditText(),1));
        endTime.setEndIconOnClickListener(v -> showTimePickerDialog(binding.endTime.getEditText(),2));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.spinner_dropdown_item,
                eventType);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.eventSpinner.setAdapter(adapter);
    }

    private void saveEvent(){
        //add event to database

        String title = binding.eventName.getText().toString();
        String description = binding.eventDescription.getText().toString();
        List<Integer> users = new ArrayList<>();
        users.add(userID);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(Calendar.getInstance().getTime());

        String type = binding.eventSpinner.getSelectedItem().toString();

        Map<String, Object> map = new ArrayMap<>();
        map.put("title",title);
        map.put("description", description);
        map.put("date", formattedDate);
        map.put("start_time",startT);
        map.put("end_time",endT);
        map.put("users", users);
        map.put("type",type);

        viewModel.addEvent(map, userID);

        dismiss();
    }

    void showTimePickerDialog(EditText editText, int type){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute1) ->
        {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute1);

            long timeStamp = calendar.getTimeInMillis() / 1000;
            if(type == 1) startT = timeStamp;
            else endT = timeStamp;

            int hour1 = calendar.get(Calendar.HOUR);
            if(hour1 == 0) hour1 = 12;

            String min = (minute1 >= 10 ? "" : "0" )+ minute1;

            String time = hour1 + " : " + min + " " + (hourOfDay >= 12 ? "PM" : "AM");
            editText.setText(time);
        }, hour, minute, false);

        timePickerDialog.show();
    }
}