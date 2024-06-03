package com.example.warline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ResetActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    EditText editUsername, editPass, editCPass;
    Customer customer;
    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        editUsername = findViewById(R.id.editUsername2);
        editPass = findViewById(R.id.editPass2);
        editCPass = findViewById(R.id.editPass3);
        btnReset = findViewById(R.id.buttonReset);

        dbHelper = new DBHelper(this);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editusername = editUsername.getText().toString();
                String editpass = editPass.getText().toString();
                String editCpass = editCPass.getText().toString();

                customer = dbHelper.getUserByUsername(editusername);

                if(customer != null){
                    int idnow = customer.getId();
                    String usernamenow = customer.getUsername();
                    if(editusername.equals(usernamenow)){
                        if(editpass.equals(editCpass)){
                            dbHelper.updatePass(idnow, usernamenow);
                            Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(ResetActivity.this, "Pass dan Confirm tidak sesuai!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(ResetActivity.this, "User tidak tersedia", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ResetActivity.this, "User tidak tersedia", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean cekReset(String username){
        customer = dbHelper.getUserByUsername(username);
        String usernamenow = customer.getUsername();

        if(username.equals(usernamenow)){
            SharedPreferences preferences = getSharedPreferences("usernow", MODE_PRIVATE);
            preferences.edit().putInt("user_id", customer.getId()).apply();
            return true;
        }
        return false;
    }
}