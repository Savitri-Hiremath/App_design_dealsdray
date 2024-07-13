package com.example.flutter_design;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OtpVerificationActivity extends AppCompatActivity {

    private EditText editTextOTP;
    private Button buttonVerify, buttonSendAgain;
    private DatabaseHelper databaseHelper;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        editTextOTP = findViewById(R.id.editTextOTP);
        buttonVerify = findViewById(R.id.buttonverify);
        buttonSendAgain = findViewById(R.id.buttonSendAgain);
        databaseHelper = new DatabaseHelper(this);

        phoneNumber = getIntent().getStringExtra("phoneNumber");

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });

        buttonSendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newOtp = generateNewOtp(); // Generate a new OTP
                databaseHelper.addOtp(phoneNumber, newOtp); // Update OTP in the database
                Toast.makeText(OtpVerificationActivity.this, "OTP sent again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyOtp() {
        String enteredOtp = editTextOTP.getText().toString().trim();
        String storedOtp = databaseHelper.getOtp(phoneNumber);

        if (storedOtp != null && storedOtp.equals(enteredOtp)) {
            // OTP matches, navigate to MainActivity
            Intent intent = new Intent(OtpVerificationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // OTP does not match, show error message
            Toast.makeText(this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateNewOtp() {
        int randomOtp = 1000 + (int) (Math.random() * 9000);
        return String.valueOf(randomOtp);
    }
}
