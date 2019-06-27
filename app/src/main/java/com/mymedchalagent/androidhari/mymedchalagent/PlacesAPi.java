package com.mymedchalagent.androidhari.mymedchalagent;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class PlacesAPi extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_api);

        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        }
        else
        {

            // Code for Below 23 API Oriented Device
            // Do next code
        }
    }




    PlacesClient placesClient = Places.createClient(this);



    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(PlacesAPi.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(PlacesAPi.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(PlacesAPi.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();

            ActivityCompat.requestPermissions(PlacesAPi.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(PlacesAPi.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {

                    Log.e("value", "Permission Denied, You cannot use local drive .");

                    //  Toast.makeText(this, "Give Permission to upload Picture", Toast.LENGTH_SHORT).show();


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage("Please Give Access Permission for the App to Work Properly ");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    requestPermission();
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                break;
        }
    }
}
