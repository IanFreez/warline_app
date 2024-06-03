package com.example.warline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    TextView forgotPw;
    EditText editTextEmail, editTextPass;
    Button btnLogin;
    Customer customer;
    Admin admin;
    private List<KeranjangI> cartProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);

        forgotPw = findViewById(R.id.forgotpw);
        editTextEmail = findViewById(R.id.editEmailLogin);
        editTextPass = findViewById(R.id.editPassLogin);
        btnLogin = findViewById(R.id.buttonLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editemail = editTextEmail.getText().toString();
                String editpass = editTextPass.getText().toString();
                if (cekAdmin(editemail, editpass)){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);

                    intent.putExtra("cartList", new ArrayList<>(cartProducts));
                    startActivity(intent);
                }else if (cekLogin(editemail, editpass)) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    intent.putExtra("cartList", new ArrayList<>(cartProducts));
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onTextClick(View view){
        Intent intent = new Intent(LoginActivity.this, ResetActivity.class);
        startActivity(intent);
    }

    private boolean cekAdmin(String username, String pass){
        admin = dbHelper.getAdminByUsername(username);
        if (admin != null) {
            String usernamenow = admin.getUsername();
            String passnow = admin.getPass();
            if (username.equals(usernamenow) && pass.equals(passnow)) {
                SharedPreferences preferences = getSharedPreferences("usernow", MODE_PRIVATE);
                preferences.edit().putInt("user_id", admin.getId()).apply();
                // Start admin activity
                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        }else{
            return false;
        }
    }
    private boolean cekLogin(String username, String pass){
        customer = dbHelper.getUserByUsername(username);
        if (customer != null) {
            String usernamenow = customer.getUsername();
            String passnow = customer.getPass();
            if (username.equals(usernamenow) && pass.equals(passnow)) {
                SharedPreferences preferences = getSharedPreferences("usernow", MODE_PRIVATE);
                preferences.edit().putInt("user_id", customer.getId()).apply();
                // Start user activity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("cartList", new ArrayList<>(cartProducts));
                startActivity(intent);
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
}