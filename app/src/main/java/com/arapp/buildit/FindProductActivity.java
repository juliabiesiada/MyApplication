package com.arapp.buildit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wikitude.WikitudeSDK;
import com.wikitude.common.permission.PermissionManager;

import java.util.Arrays;

public class FindProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_product);




        //Search button
        Button searchButton = (Button) findViewById(R.id.bSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        Button scanButton = (Button) findViewById(R.id.bScan);
        scanButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                WikitudeSDK.getPermissionManager()
                        .checkPermissions(FindProductActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                PermissionManager.WIKITUDE_PERMISSION_REQUEST,
                                new PermissionManager.PermissionManagerCallback() {
                                    @Override
                                    public void permissionsGranted(int requestCode) {
                                        Class activity = ImageTrackingActivity.class;
                                        final Intent intent = new Intent(FindProductActivity.this, activity);
                                        startActivity(intent);

                                    }

                                    @Override
                                    public void permissionsDenied(String[] deniedPermissions) {
                                        Toast.makeText(FindProductActivity.this, "BuildIt needs the following permissions to enable an AR experience: " + Arrays.toString(deniedPermissions), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void showPermissionRationale(final int requestCode, final String[] permissions) {
                                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(FindProductActivity.this);
                                        alertBuilder.setCancelable(true);
                                        alertBuilder.setTitle("Wikitude Permissions");
                                        alertBuilder.setMessage("The Wikitude SDK needs the following permissions to enable an AR experience: " + Arrays.toString(permissions));
                                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                WikitudeSDK.getPermissionManager().positiveRationaleResult(requestCode, permissions);
                                            }
                                        });

                                        AlertDialog alert = alertBuilder.create();
                                        alert.show();
                                    }
                                });



    }
}); }}
