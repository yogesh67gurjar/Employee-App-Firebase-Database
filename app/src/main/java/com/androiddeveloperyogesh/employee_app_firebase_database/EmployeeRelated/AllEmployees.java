package com.androiddeveloperyogesh.employee_app_firebase_database.EmployeeRelated;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.androiddeveloperyogesh.employee_app_firebase_database.Adapters.EmployeeAdapter;
import com.androiddeveloperyogesh.employee_app_firebase_database.BottomSheetDialogFragment.AddEmployee;
import com.androiddeveloperyogesh.employee_app_firebase_database.MainActivity;
import com.androiddeveloperyogesh.employee_app_firebase_database.Models.Employee;
import com.androiddeveloperyogesh.employee_app_firebase_database.R;
import com.androiddeveloperyogesh.employee_app_firebase_database.databinding.ActivityAllEmployeesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllEmployees extends AppCompatActivity {
    ActivityAllEmployeesBinding binding;
    List<Employee> employeeList;

    EmployeeAdapter employeeAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllEmployeesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseDatabase = FirebaseDatabase.getInstance();

        employeeList = new ArrayList<>();
        // static function call hua bina function ka object bnae
//        databaseHelper = DatabaseHelper.getInstance(MainActivity.this);
        showEmployees();

        binding.addFirstEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEmployee addEmp = new AddEmployee(AllEmployees.this);
                addEmp.show(getSupportFragmentManager(), addEmp.getTag());
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

        databaseReference = firebaseDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employeeList.clear();
                for (DataSnapshot singleUnit : snapshot.getChildren()) {
                    Employee e = singleUnit.getValue(Employee.class);
                    employeeList.add(e);
                }

                if (employeeList.size() > 0) {
                    employeeAdapter = new EmployeeAdapter(employeeList, AllEmployees.this);
                    binding.recyclerViewEmployees.setAdapter(employeeAdapter);
                    binding.recyclerViewEmployees.setLayoutManager(new LinearLayoutManager(AllEmployees.this));
                    binding.nothingFoundCard.setVisibility(View.GONE);
                    binding.deleteAllBtn.setVisibility(View.VISIBLE);
                    binding.recyclerViewEmployees.setVisibility(View.VISIBLE);
                    binding.searchView.setVisibility(View.VISIBLE);

                } else {
                    binding.deleteAllBtn.setVisibility(View.GONE);
                    binding.nothingFoundCard.setVisibility(View.VISIBLE);
                    binding.recyclerViewEmployees.setVisibility(View.GONE);
                    binding.searchView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
                binding.deleteAllBtn.setVisibility(View.GONE);
                binding.nothingFoundCard.setVisibility(View.VISIBLE);
                binding.recyclerViewEmployees.setVisibility(View.GONE);
                binding.searchView.setVisibility(View.GONE);
            }
        });
        if (employeeList.size() > 0) {
            employeeAdapter = new EmployeeAdapter(employeeList, AllEmployees.this);
            binding.recyclerViewEmployees.setAdapter(employeeAdapter);
            binding.recyclerViewEmployees.setLayoutManager(new LinearLayoutManager(AllEmployees.this));
            binding.nothingFoundCard.setVisibility(View.GONE);
            binding.deleteAllBtn.setVisibility(View.VISIBLE);
            binding.recyclerViewEmployees.setVisibility(View.VISIBLE);
            binding.searchView.setVisibility(View.VISIBLE);

        } else {
            binding.deleteAllBtn.setVisibility(View.GONE);
            binding.nothingFoundCard.setVisibility(View.VISIBLE);
            binding.recyclerViewEmployees.setVisibility(View.GONE);
            binding.searchView.setVisibility(View.GONE);
        }
    }
}