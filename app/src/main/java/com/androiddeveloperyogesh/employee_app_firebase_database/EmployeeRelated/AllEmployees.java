package com.androiddeveloperyogesh.employee_app_firebase_database.EmployeeRelated;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.androiddeveloperyogesh.employee_app_firebase_database.Adapters.EmployeeAdapter;
import com.androiddeveloperyogesh.employee_app_firebase_database.Models.Employee;
import com.androiddeveloperyogesh.employee_app_firebase_database.R;
import com.androiddeveloperyogesh.employee_app_firebase_database.databinding.ActivityAllEmployeesBinding;

import java.util.ArrayList;
import java.util.List;

public class AllEmployees extends AppCompatActivity {
    ActivityAllEmployeesBinding binding;
    List<Employee> employeeList;

    EmployeeAdapter employeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllEmployeesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        employeeList = new ArrayList<>();
        // static function call hua bina function ka object bnae
//        databaseHelper = DatabaseHelper.getInstance(MainActivity.this);
        showEmployees();

        binding.addFirstEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AddEmployee addEmp = new AddEmployee(databaseHelper, MainActivity.this);
//                addEmp.show(getSupportFragmentManager(), addEmp.getTag());
            }
        });

        binding.addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.addFirstEmployee.performClick();
            }
        });

        binding.deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                databaseHelper.employeeDao().deleteAllEmployees();
                showEmployees();
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

    }


    private void filter(String text) {
        ArrayList<Employee> filteredlist = new ArrayList<>();

        for (Employee item : employeeList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            employeeAdapter.filterList(filteredlist);
        }
    }

    public void showEmployees() {
//        if (databaseHelper.employeeDao().getAllEmployees().size() > 0) {
//            employeeList = databaseHelper.employeeDao().getAllEmployees();
//            employeeAdapter = new EmployeeAdapter(employeeList, MainActivity.this, databaseHelper);
//            binding.recyclerViewEmployees.setAdapter(employeeAdapter);
//            binding.recyclerViewEmployees.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//            binding.nothingFoundCard.setVisibility(View.GONE);
//            binding.deleteAllBtn.setVisibility(View.VISIBLE);
//            binding.recyclerViewEmployees.setVisibility(View.VISIBLE);
//            binding.searchView.setVisibility(View.VISIBLE);
//
//        } else {
//            binding.deleteAllBtn.setVisibility(View.GONE);
//            binding.nothingFoundCard.setVisibility(View.VISIBLE);
//            binding.recyclerViewEmployees.setVisibility(View.GONE);
//            binding.searchView.setVisibility(View.GONE);
//        }
    }
}