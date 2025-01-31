package com.example.flutter_design;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextPhone, editTextPassword, editTextReferralCode;
    private ImageButton imageButtonRegister;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        editTextPhone = findViewById(R.id.editTextTextPhone);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        editTextReferralCode = findViewById(R.id.editTextText2);
        imageButtonRegister = findViewById(R.id.imageButton2);


        databaseHelper = new DatabaseHelper(this);


        imageButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {

        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String referralCode = editTextReferralCode.getText().toString().trim();

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("password", password);
        values.put("referralCode", referralCode);
        long newRowId = db.insert("users", null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}
