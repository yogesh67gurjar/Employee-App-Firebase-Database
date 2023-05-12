package com.androiddeveloperyogesh.employee_app_firebase_database.EmployeeRelated;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllEmployees extends AppCompatActivity {
    ActivityAllEmployeesBinding binding;
    List<Employee> employeeList;

    EmployeeAdapter employeeAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public static final int STORAGE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllEmployeesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestRuntimePermissionFunc("manageStorage");
        } else {
            requestRuntimePermissionFunc("storage");
        }

        firebaseDatabase = FirebaseDatabase.getInstance();

        employeeList = new ArrayList<>();
        // static function call hua bina function ka object bnae
//        databaseHelper = DatabaseHelper.getInstance(MainActivity.this);


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
                databaseReference = firebaseDatabase.getReference("Employees");
                databaseReference.removeValue();
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
        binding.loading.setVisibility(View.VISIBLE);
        databaseReference = firebaseDatabase.getReference("Employees");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employeeList.clear();
                for (DataSnapshot singleUnit : snapshot.getChildren()) {
                    Employee e = singleUnit.getValue(Employee.class);
                    employeeList.add(e);
                }

                if (employeeList.size() > 0) {
                    binding.loading.setVisibility(View.GONE);
                    employeeAdapter = new EmployeeAdapter(employeeList, AllEmployees.this);
                    binding.recyclerViewEmployees.setAdapter(employeeAdapter);
                    binding.recyclerViewEmployees.setLayoutManager(new LinearLayoutManager(AllEmployees.this));
                    binding.nothingFoundCard.setVisibility(View.GONE);
                    binding.deleteAllBtn.setVisibility(View.VISIBLE);
                    binding.recyclerViewEmployees.setVisibility(View.VISIBLE);
                    binding.searchView.setVisibility(View.VISIBLE);

                } else {
                    binding.loading.setVisibility(View.GONE);
                    binding.deleteAllBtn.setVisibility(View.GONE);
                    binding.nothingFoundCard.setVisibility(View.VISIBLE);
                    binding.recyclerViewEmployees.setVisibility(View.GONE);
                    binding.searchView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.loading.setVisibility(View.GONE);
                Log.w("TAG", "Failed to read value.", error.toException());
                binding.deleteAllBtn.setVisibility(View.GONE);
                binding.nothingFoundCard.setVisibility(View.VISIBLE);
                binding.recyclerViewEmployees.setVisibility(View.GONE);
                binding.searchView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        showEmployees();
    }


    private void requestRuntimePermissionFunc(String str) {

        if (str.equals("storage")) {
            if (ContextCompat.checkSelfPermission(AllEmployees.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "storage permission already granted", Toast.LENGTH_SHORT).show();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(AllEmployees.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AllEmployees.this);
                builder.setMessage("this permission is required for this and this")
                        .setTitle("storage required")
                        .setCancelable(false)
                        .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(AllEmployees.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE);
                            }
                        })
                        .setNegativeButton("reject", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                ActivityCompat.requestPermissions(AllEmployees.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE);
            }
        } else if (str.equals("manageStorage")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // Permission is granted

                    Toast.makeText(this, "manage storage permission already granted", Toast.LENGTH_SHORT).show();
                    Log.d("dgdgsdfgsdfgs", "yes yes yes yes ");
                } else {
                    // Permission is not granted, request it
                    Log.d("dgdgsdfgsdfgs", "no no no no ");

                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        }
    }
}