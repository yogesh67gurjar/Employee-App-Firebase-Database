package com.androiddeveloperyogesh.employee_app_firebase_database.BottomSheetDialogFragment;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.androiddeveloperyogesh.employee_app_firebase_database.Adapters.EmployeeAdapter;
import com.androiddeveloperyogesh.employee_app_firebase_database.EmployeeRelated.AllEmployees;
import com.androiddeveloperyogesh.employee_app_firebase_database.Models.Employee;
import com.androiddeveloperyogesh.employee_app_firebase_database.R;
import com.androiddeveloperyogesh.employee_app_firebase_database.databinding.FragmentAddEmployeeBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.StartupTime;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class AddEmployee extends BottomSheetDialogFragment {
    Context context;
    FragmentAddEmployeeBinding binding;
    File imageFile;
    public static final int IMAGE_CODE = 987;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String constantEmployeeId, employeeId;
    String imagePath;

    public AddEmployee(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddEmployeeBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_add_employee, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();

        if (getArguments() != null) {

            binding.employeeId.setEnabled(false);

            // update hoga
            String employeeId = getArguments().getString("EmployeeId");
            databaseReference = firebaseDatabase.getReference("Employees").child(employeeId);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    binding.saveBtn.setText("save changes");

                    Employee singleEmployee = dataSnapshot.getValue(Employee.class);
                    binding.nameEt.setText(singleEmployee.getName());
                    binding.fatherNameEt.setText(singleEmployee.getFatherName());
                    binding.dobEt.setText(singleEmployee.getDob());
                    binding.phoneEt.setText(singleEmployee.getPhone());
                    binding.emailEt.setText(singleEmployee.getEmail());
                    binding.addressEt.setText(singleEmployee.getAddress());
                    binding.employeeId.setText(singleEmployee.getEmployeeId());
                    constantEmployeeId = singleEmployee.getEmployeeId();
                    binding.designitionEt.setText(singleEmployee.getDesignation());
                    binding.experienceEt.setText(singleEmployee.getExperience());

                    binding.salaryEt.setText(String.valueOf(singleEmployee.getSalary()));
                    imagePath = singleEmployee.getImagePath();
                    getBitmapFromImagePath(imagePath);

                    if (singleEmployee.getGender() != null) {
                        if (singleEmployee.getGender().equalsIgnoreCase("male")) {
                            binding.rbMale.setChecked(true);
                        } else {
                            binding.rbFemale.setChecked(true);
                        }
                    }

                    if (singleEmployee.isMaritalStatus()) {
                        binding.rbMarried.setChecked(true);
                    } else {
                        binding.rbUnmarried.setChecked(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgIntent = new Intent(Intent.ACTION_PICK);
                imgIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imgIntent, IMAGE_CODE);
            }
        });
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedIdGender = binding.rgGender.getCheckedRadioButtonId();
                RadioButton genderRb = (RadioButton) view.findViewById(selectedIdGender);

                int selectedIdMaritalStatus = binding.rgMaritalStatus.getCheckedRadioButtonId();
                RadioButton maritalStatus = (RadioButton) view.findViewById(selectedIdMaritalStatus);
                boolean marriage = false;

                String name, fatherName, dob, phone, email, address, designation, experience, salary, gender;
                if (getArguments() != null) {
                    // update hoga
//                    employeeId = binding.employeeId.getText().toString();
                    name = binding.nameEt.getText().toString();
                    fatherName = binding.fatherNameEt.getText().toString();
                    dob = binding.dobEt.getText().toString();
                    phone = binding.phoneEt.getText().toString();
                    email = binding.emailEt.getText().toString();
                    address = binding.addressEt.getText().toString();
                    designation = binding.designitionEt.getText().toString();
                    experience = binding.experienceEt.getText().toString();
                    salary = binding.salaryEt.getText().toString();
                    gender = genderRb.getText().toString();
                    if (maritalStatus.getText().toString().equals("married")) {
                        marriage = true;
                    }
                    addNewEmployeeFunc(constantEmployeeId, name, fatherName, dob, gender, phone, email, address, designation, experience, marriage, Float.parseFloat(salary), imagePath);

                } else {
                    employeeId = binding.employeeId.getText().toString();
                    name = binding.nameEt.getText().toString();
                    fatherName = binding.fatherNameEt.getText().toString();
                    dob = binding.dobEt.getText().toString();
                    phone = binding.phoneEt.getText().toString();
                    email = binding.emailEt.getText().toString();
                    address = binding.addressEt.getText().toString();
                    designation = binding.designitionEt.getText().toString();
                    experience = binding.experienceEt.getText().toString();
                    salary = binding.salaryEt.getText().toString();
                    if (selectedIdGender == -1) {
                        gender = null;
                    } else {
                        gender = genderRb.getText().toString();
                    }

                    if (selectedIdMaritalStatus != -1) {
                        if (maritalStatus.getText().toString().equals("married")) {
                            marriage = true;
                        }
                    }

                    if (!binding.salaryEt.getText().toString().isEmpty()) {
                        addNewEmployeeFunc(employeeId, name, fatherName, dob, gender, phone, email, address, designation, experience, marriage, Float.parseFloat(salary), imagePath);
                    } else {
                        addNewEmployeeFunc(employeeId, name, fatherName, dob, gender, phone, email, address, designation, experience, marriage, 0, imagePath);
                    }

                }

            }
        });

        return binding.getRoot();
    }

    private void getBitmapFromImagePath(String imagePath) {
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            try {
                InputStream inputStream = new FileInputStream(imageFile);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                binding.imageView.setImageBitmap(bitmap);
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    private void getUriFromImagePath(String imageP) {
//        File imageFile = new File(imageP);
//        if (imageFile.exists()) {
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//                // Android Q and above
//                ContentResolver resolver = context.getContentResolver();
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.getName());
//                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
//
//                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//                if (imageUri != null) {
//                    String imagePath = imageUri.toString(); // Convert the imageUri to imagePath
//                    Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//                    binding.imageView.setImageBitmap(bitmap);
//                }  // Failed to insert into MediaStore
//                // Handle the error accordingly
//
//            } else {
//                // Below Android Q
//                Uri imageUri = Uri.fromFile(imageFile);
//                String imagePath = imageUri.getPath();
//                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//                binding.imageView.setImageBitmap(bitmap);
//            }
//        } else {
//            // Image file does not exist
//            // Handle the error accordingly
//        }
//    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CODE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    binding.imageView.setImageURI(selectedImageUri);
                    imagePath = getRealPathFromURI(data.getData());
                }
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void addNewEmployeeFunc(String employeeId, String name, String fatherName, String dob, String gender, String phone, String email, String address, String designation, String experience, boolean maritalStatus, float salary, String image) {

        if (getArguments() == null) {
            // adding
            databaseReference = firebaseDatabase.getReference("Employees");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("employeeId", employeeId);
                    map.put("name", name);
                    map.put("fatherName", fatherName);
                    map.put("dob", dob);
                    map.put("gender", gender);
                    map.put("phone", phone);
                    map.put("email", email);
                    map.put("address", address);
                    map.put("designation", designation);
                    map.put("experience", experience);
                    map.put("maritalStatus", maritalStatus);
                    map.put("salary", salary);
                    map.put("imagePath", image);
                    databaseReference.child(employeeId).setValue(map);
                    Toast.makeText(context, "employee added successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "failed to add employee", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            databaseReference = firebaseDatabase.getReference("Employees");
            firebaseDatabase.getReference("Employees").child(employeeId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("employeeId", employeeId);
                    map.put("name", name);
                    map.put("fatherName", fatherName);
                    map.put("dob", dob);
                    map.put("gender", gender);
                    map.put("phone", phone);
                    map.put("email", email);
                    map.put("address", address);
                    map.put("designation", designation);
                    map.put("experience", experience);
                    map.put("maritalStatus", maritalStatus);
                    map.put("salary", salary);
                    map.put("imagePath", image);

                    databaseReference.child(employeeId).updateChildren(map);
                    Toast.makeText(context, "employee updated successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        this.dismiss();

        ((AllEmployees) context).showEmployees();
    }
}