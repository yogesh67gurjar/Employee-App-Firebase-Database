package com.androiddeveloperyogesh.employee_app_firebase_database.EmployeeRelated;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.androiddeveloperyogesh.employee_app_firebase_database.EmployeeRelated.AllEmployees;
import com.androiddeveloperyogesh.employee_app_firebase_database.Models.Employee;
import com.androiddeveloperyogesh.employee_app_firebase_database.R;
import com.androiddeveloperyogesh.employee_app_firebase_database.databinding.FragmentAddEmployeeBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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


public class AddEmployeeBottomSheet extends BottomSheetDialogFragment {
    public static final int IMAGE_CODE = 987;

    //  ye sheet 2 cases me khulegi
    //  1 jb apn employee ko add krenge , us case me apne paas us employee ki employeeId nhi hogi
    //  2 jb apn employee ko update krenge , us case me apn employeeId ki help se use update krenge


    Context context;
    FragmentAddEmployeeBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    // shuru me jb apn employee ko add krenge tb employeeId rhengi apne paas phir jb update krenge to constantEmployeeId use krenge qki apn ko purani employeeId ko change nhi krna he to apne paas jo database se employeeId aaegi usi ko reuse krenge apn
    String employeeId;
    String imagePath;
    boolean imageBoolean = false;

    public AddEmployeeBottomSheet(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddEmployeeBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_add_employee, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();

        if (getArguments() != null) {
            // is case me update hoga


            // image definitely aarhi hogi isiliye isko true hi rkh do shuru me hi
            imageBoolean = true;

            // employeeId edittext k text me ab koi bhi modification nhi kr skta he user
            binding.employeeId.setEnabled(false);

            // bundle se id le li
            employeeId = getArguments().getString("EmployeeId");

            //  database me apn ne ye set kr diya k hum is child k liye kaam krenge jo is employeeId se jaana jaata he
            databaseReference = firebaseDatabase.getReference("Employees").child(employeeId);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    binding.saveBtn.setText("save changes");

                    // database se data le liya apn ne us employee ka jiski employeeId thi apne paas
                    // ye isliye liya qki apn ko edittext me bydefault dikhaane hi he na he sb cheeze change krne k liye
                    Employee singleEmployee = dataSnapshot.getValue(Employee.class);

                    employeeId = singleEmployee.getEmployeeId();

                    binding.nameEt.setText(singleEmployee.getName());
                    binding.fatherNameEt.setText(singleEmployee.getFatherName());
                    binding.dobEt.setText(singleEmployee.getDob());
                    binding.phoneEt.setText(singleEmployee.getPhone());
                    binding.emailEt.setText(singleEmployee.getEmail());
                    binding.addressEt.setText(singleEmployee.getAddress());
                    binding.employeeId.setText(singleEmployee.getEmployeeId());
                    binding.designitionEt.setText(singleEmployee.getDesignation());
                    binding.experienceEt.setText(singleEmployee.getExperience());
                    binding.salaryEt.setText(String.valueOf(singleEmployee.getSalary()));
                    imagePath = singleEmployee.getImagePath();

                    //  imagepath jo mil rha he usse imageView me bitmap set kr denge
                    getBitmapFromImagePath(imagePath);

                    if (singleEmployee.getGender().equalsIgnoreCase("male")) {
                        binding.rbMale.setChecked(true);
                    } else {
                        binding.rbFemale.setChecked(true);
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
                //  gallery se image pick krne k liye

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

                if (binding.employeeId.getText().toString().trim().isEmpty()) {
                    binding.employeeId.setError("enter employee Id");
                    binding.employeeId.requestFocus();
                } else if (binding.nameEt.getText().toString().trim().isEmpty()) {
                    binding.nameEt.setError("enter name");
                    binding.nameEt.requestFocus();
                } else if (binding.fatherNameEt.getText().toString().trim().isEmpty()) {
                    binding.fatherNameEt.setError("enter father name");
                    binding.fatherNameEt.requestFocus();
                } else if (binding.dobEt.getText().toString().trim().isEmpty()) {
                    binding.dobEt.setError("enter Dob");
                    binding.dobEt.requestFocus();
                } else if (binding.phoneEt.getText().toString().trim().isEmpty()) {
                    binding.phoneEt.setError("enter phone no.");
                    binding.phoneEt.requestFocus();
                } else if (binding.emailEt.getText().toString().trim().isEmpty()) {
                    binding.emailEt.setError("enter email");
                    binding.emailEt.requestFocus();
                } else if (binding.addressEt.getText().toString().trim().isEmpty()) {
                    binding.addressEt.setError("enter address");
                    binding.addressEt.requestFocus();
                } else if (binding.designitionEt.getText().toString().trim().isEmpty()) {
                    binding.designitionEt.setError("enter designation");
                    binding.designitionEt.requestFocus();
                } else if (binding.experienceEt.getText().toString().trim().isEmpty()) {
                    binding.experienceEt.setError("enter experience");
                    binding.experienceEt.requestFocus();
                } else if (binding.salaryEt.getText().toString().trim().isEmpty()) {
                    binding.salaryEt.setError("enter salary");
                    binding.salaryEt.requestFocus();
                } else if (selectedIdGender == -1) {
                    Toast.makeText(context, "please select gender", Toast.LENGTH_SHORT).show();
                } else if (selectedIdMaritalStatus == -1) {
                    Toast.makeText(context, "please select marital status", Toast.LENGTH_SHORT).show();
                } else if (!imageBoolean) {
                    Toast.makeText(context, "please upload image", Toast.LENGTH_SHORT).show();
                } else {

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
                    marriage = maritalStatus.getText().toString().equals("married");

                    if (getArguments() == null) {
                        employeeId = binding.employeeId.getText().toString();
                    }
                    addNewEmployeeFunc(employeeId, name, fatherName, dob, gender, phone, email, address, designation, experience, marriage, Float.parseFloat(salary), imagePath);
                }


            }


        });

        return binding.getRoot();
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

    public void addNewEmployeeFunc(String employeeId, String name, String fatherName, String dob, String gender, String phone, String email, String address, String designation, String experience, boolean maritalStatus, float salary, String image) {
        if (getArguments() == null) {
            // adding
            databaseReference = firebaseDatabase.getReference("Employees");

            // add krna he to hashmap ki help se add krenge
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

            //  update krna he to bhi hashmap ki help se krenge
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