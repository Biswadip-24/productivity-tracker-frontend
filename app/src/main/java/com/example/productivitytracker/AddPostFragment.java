package com.example.productivitytracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.example.productivitytracker.databinding.FragmentAddEventBinding;
import com.example.productivitytracker.databinding.FragmentAddPostBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddPostFragment extends BottomSheetDialogFragment {

    FragmentAddPostBinding binding;
    Button postButton;
    UserViewModel viewModel;

    int userID;

    public static AddPostFragment newInstance(int userID) {
        AddPostFragment f = new AddPostFragment();
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
        binding = FragmentAddPostBinding.inflate(inflater, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        Bundle args = getArguments();
        userID = args.getInt("userID", 1);

        postButton = binding.postButton;
        setListeners();

        return binding.getRoot();
    }

    private void setListeners(){
        postButton.setOnClickListener(v -> addPost());
    }

    private void addPost(){
        viewModel.addPost(userID, binding.postDescription.getText().toString());
        dismiss();
    }
}