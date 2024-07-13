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

          editTextPhone = findViewById(R.id.editTextPhone2);
        buttonSendOTP = findViewById(R.id.button4);


        databaseHelper = new DatabaseHelper(this);


        buttonSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP();
            }
        });



    }

    private void sendOTP() {

        String phoneNumber = editTextPhone.getText().toString().trim();


        if (isPhoneNumberExists(phoneNumber)) {
            String otp = generateOTP(4);
            sendOtpToPhoneNumber(phoneNumber, otp);


            Intent intent = new Intent(LoginActivity.this, OtpVerificationActivity.class);
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("otp", otp);
            startActivity(intent);


            Toast.makeText(this, "OTP sent to registered number.", Toast.LENGTH_SHORT).show();
        } else {

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
        Random random = new Random();
        StringBuilder otp = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            otp.append(numbers.charAt(random.nextInt(numbers.length())));
        }

        return otp.toString();
    }



    private void sendOtpToPhoneNumber(String phoneNumber, String otp) {

        String message = "Your OTP for login is: " + otp;
        Toast.makeText(this, "OTP: " + otp, Toast.LENGTH_LONG).show();

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
