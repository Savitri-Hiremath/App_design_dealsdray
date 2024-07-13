package com.example.flutter_design;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
public class LoginActivity extends AppCompatActivity {
    private EditText editTextPhone;
    private Button buttonSendOTP;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        editTextPhone = findViewById(R.id.editTextPhone2);
        buttonSendOTP = findViewById(R.id.button4);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Handle send OTP button click
        buttonSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP();
            }
        });

        // Handle register button click

    }

    private void sendOTP() {
        // Get phone number entered by the user
        String phoneNumber = editTextPhone.getText().toString().trim();

        // Check if the phone number exists in the database
        if (isPhoneNumberExists(phoneNumber)) {
            // Generate and send OTP (simulated here, you'll need to implement actual OTP generation and sending)
            String otp = generateOTP(4);
            sendOtpToPhoneNumber(phoneNumber, otp);

            // Example: Navigate to OTP verification activity
            Intent intent = new Intent(LoginActivity.this, OtpVerificationActivity.class);
            intent.putExtra("phoneNumber", phoneNumber); // Pass phone number to OTP verification activity
            intent.putExtra("otp", otp); // Pass OTP to OTP verification activity
            startActivity(intent);

            // Display toast after starting the activity
            Toast.makeText(this, "OTP sent to registered number.", Toast.LENGTH_SHORT).show();
        } else {
            // Phone number not registered, navigate to register page
            Toast.makeText(this, "Phone number not registered. Redirecting to register page.", Toast.LENGTH_SHORT).show();
            navigateToRegister();
        }
    }


    private boolean isPhoneNumberExists(String phoneNumber) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"phone"}, "phone = ?", new String[]{phoneNumber}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private String generateOTP(int length) {
        String numbers = "0123456789";
        Random random = new Random(); // Ensure you import java.util.Random
        StringBuilder otp = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            otp.append(numbers.charAt(random.nextInt(numbers.length())));
        }

        return otp.toString();
    }



    private void sendOtpToPhoneNumber(String phoneNumber, String otp) {
        // Simulated OTP sending (replace with actual SMS sending using SMSManager)
        String message = "Your OTP for login is: " + otp;
        Toast.makeText(this, "OTP: " + otp, Toast.LENGTH_LONG).show();
        // SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, null, null);
    }

    private void navigateToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}
