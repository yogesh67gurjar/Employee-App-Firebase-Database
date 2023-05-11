package com.androiddeveloperyogesh.employee_app_firebase_database.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddeveloperyogesh.employee_app_firebase_database.BottomSheetDialogFragment.AddEmployee;
import com.androiddeveloperyogesh.employee_app_firebase_database.EmployeeRelated.AllEmployees;
import com.androiddeveloperyogesh.employee_app_firebase_database.MainActivity;
import com.androiddeveloperyogesh.employee_app_firebase_database.Models.Employee;
import com.androiddeveloperyogesh.employee_app_firebase_database.R;


import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private List<Employee> employeeList;
    Context context;


    public EmployeeAdapter(List<Employee> employeeList, Context context) {
        this.employeeList = employeeList;
        this.context = context;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmployeeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_employee, parent, false));
    }

    // method for filtering our recyclerview items.
    public void filterList(List<Employee> filterlist) {
        employeeList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee singleUnit = employeeList.get(position);

        holder.name.setText("name- " + singleUnit.getName());

        holder.phone.setText("phone- " + singleUnit.getPhone());

        holder.eId.setText("employee Id- " + String.valueOf(singleUnit.getEmployeeId()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // for update
                Bundle bundle = new Bundle();
                bundle.putString("EmployeeId", singleUnit.getEmployeeId());

                bundle.putString("Name", singleUnit.getName());
                bundle.putString("FatherName", singleUnit.getFatherName());
                bundle.putString("Dob", singleUnit.getDob());
                bundle.putString("Gender", singleUnit.getGender());
                bundle.putString("Phone", singleUnit.getPhone());
                bundle.putString("Email", singleUnit.getEmail());
                bundle.putString("Address", singleUnit.getAddress());
                bundle.putString("Designation", singleUnit.getDesignation());
                bundle.putString("Experience", singleUnit.getExperience());
                bundle.putBoolean("MaritalStatus", singleUnit.isMaritalStatus());
                bundle.putFloat("Salary", singleUnit.getSalary());
                bundle.putString("imagePath", singleUnit.getImagePath());
                bundle.putBoolean("update", true);

                AddEmployee addEmp = new AddEmployee(context);
                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                addEmp.setArguments(bundle);
                addEmp.show(fm, addEmp.getTag());

            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                AlertDialog.Builder b = new AlertDialog.Builder(context)
                        .setTitle("Do u really want to remove this employee ???")
                        .setPositiveButton("yes proceed",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                        Employee e = databaseHelper.employeeDao().getEmployeeById(singleUnit.getId());
//                                        databaseHelper.employeeDao().deleteEmployee(new Employee(singleUnit.getId(), singleUnit.getName(), singleUnit.getFatherName(), singleUnit.getDob(), singleUnit.getGender(), singleUnit.getPhone(), singleUnit.getEmail(), singleUnit.getAddress(), singleUnit.getEmployeeId(), singleUnit.getDesignation(), singleUnit.getExperience(), singleUnit.isMaritalStatus(), singleUnit.getSalary(), singleUnit.getImagePath()));
//                                        Toast.makeText(context, e.getName() + " removed successfully", Toast.LENGTH_SHORT).show();
                                        ((AllEmployees) context).showEmployees();
                                    }
                                }
                        )
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        );
                b.show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, eId;
        CardView cardView;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardEmployee);
            name = itemView.findViewById(R.id.nameEt);
            phone = itemView.findViewById(R.id.phoneEt);
            eId = itemView.findViewById(R.id.eIdEt);
        }
    }
}
