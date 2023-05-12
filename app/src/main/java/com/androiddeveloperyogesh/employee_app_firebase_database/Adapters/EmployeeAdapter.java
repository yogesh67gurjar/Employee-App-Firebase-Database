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

import com.androiddeveloperyogesh.employee_app_firebase_database.EmployeeRelated.AddEmployeeBottomSheet;
import com.androiddeveloperyogesh.employee_app_firebase_database.EmployeeRelated.AllEmployees;
import com.androiddeveloperyogesh.employee_app_firebase_database.Models.Employee;
import com.androiddeveloperyogesh.employee_app_firebase_database.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private List<Employee> employeeList;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public EmployeeAdapter(List<Employee> employeeList, Context context) {
        this.employeeList = employeeList;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
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

        holder.eId.setText("employee Id- " + singleUnit.getEmployeeId());


        // for UPDATE
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                bundle.putString("EmployeeId", singleUnit.getEmployeeId());

                AddEmployeeBottomSheet addEmp = new AddEmployeeBottomSheet(context);
                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                addEmp.setArguments(bundle);
                addEmp.show(fm, addEmp.getTag());
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                databaseReference = firebaseDatabase.getReference("Employees");

                AlertDialog.Builder b = new AlertDialog.Builder(context)
                        .setTitle("Do u really want to remove this employee ???")
                        .setPositiveButton("yes proceed",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        databaseReference.child(singleUnit.getEmployeeId()).removeValue();
                                        Toast.makeText(context, " removed successfully", Toast.LENGTH_SHORT).show();
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
