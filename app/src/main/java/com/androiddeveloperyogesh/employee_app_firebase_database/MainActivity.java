package com.androiddeveloperyogesh.employee_app_firebase_database;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.widget.Toast;

import com.androiddeveloperyogesh.employee_app_firebase_database.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public static final int NOTIFICATION = 357;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestRuntimePermissionFunc("notification");
        } else {
            Toast.makeText(MainActivity.this, Build.VERSION.SDK_INT + " requires no notification permission", Toast.LENGTH_SHORT).show();
        }

//        FirebaseMessaging.getInstance().subscribeToTopic("news");
//        String x=FirebaseMessaging.getInstance().getToken().getResult();
//        Log.d("gdgsdfgsdfsdfgsdfg", x);

    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestRuntimePermissionFunc(String str) {

        if (str.equals("notification")) {

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "notification permission already granted", Toast.LENGTH_SHORT).show();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("this permission is required for this and this")
                        .setTitle("notification required")
                        .setCancelable(false)
                        .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION);
                            }
                        })
                        .setNegativeButton("reject", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "accepted", Toast.LENGTH_SHORT).show();
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("this feature is unavailable , now open settings ")
                        .setTitle("notification to chaiye")
                        .setCancelable(false)
                        .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("reject", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestRuntimePermissionFunc("notification");
                }
            }
        }
    }
}