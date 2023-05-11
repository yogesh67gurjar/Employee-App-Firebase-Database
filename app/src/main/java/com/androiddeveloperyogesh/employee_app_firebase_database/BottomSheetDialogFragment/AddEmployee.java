package com.androiddeveloperyogesh.employee_app_firebase_database.BottomSheetDialogFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiddeveloperyogesh.employee_app_firebase_database.R;
import com.androiddeveloperyogesh.employee_app_firebase_database.databinding.FragmentAddEmployeeBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class AddEmployee extends BottomSheetDialogFragment {
    Context context;
    FragmentAddEmployeeBinding binding;

    public AddEmployee(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddEmployeeBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_add_employee, container, false);



        return binding.getRoot();
    }
}