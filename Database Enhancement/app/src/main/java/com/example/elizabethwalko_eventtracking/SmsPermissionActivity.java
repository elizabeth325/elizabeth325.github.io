package com.example.elizabethwalko_eventtracking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//class to handle SMS permission activity
public class SmsPermissionActivity extends AppCompatActivity {
    private static final int SMS_PERMISSION_CODE = 1;

    //onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_permission);

        //request permission button click listener
        Button requestPermissionButton = findViewById(R.id.request_permission_button);
        requestPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSmsPermission();
                Intent intent = new Intent(SmsPermissionActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    //method to request SMS permission
    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        } else {
            Toast.makeText(this, "SMS Permission already granted", Toast.LENGTH_SHORT).show();
            sendSmsAlert("Inventory is low!");
        }
    }

    //method to handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS Permission granted", Toast.LENGTH_SHORT).show();
                sendSmsAlert("Inventory is low!");
            } else {
                Toast.makeText(this, "SMS Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //method to send SMS alert
    public void sendSmsAlert(String message) {
        String phoneNumber = "1234567890"; // Replace with the actual phone number
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }
}