package com.example.warline;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import com.example.warline.LoginActivity;

public class AdminActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    Button btnDaging, btnSayur, btnBuah, btnSembako, btnKeranjang, btnIsiSaldo, btnLogout;
    TextView saldoText, userText;
    private static final int REQUEST_CODE_PURCHASED_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dbHelper = new DBHelper(this);

        btnDaging = findViewById(R.id.dagingbtnA);
        btnSayur = findViewById(R.id.sayuranbtnA);
        btnBuah = findViewById(R.id.buahbtnA);
        btnSembako = findViewById(R.id.sembakobtnA);
        btnKeranjang = findViewById(R.id.keranjangBtn);
        btnIsiSaldo = findViewById(R.id.buttonIsiSaldoA);
        btnLogout = findViewById(R.id.logoutAdmin);

        saldoText = findViewById(R.id.saldo);
        userText = findViewById(R.id.userView);

        SharedPreferences preferences = getSharedPreferences("usernow", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);

        if (userId != -1) {
            // Fetch user data from the database using the ID
            Admin usernow = dbHelper.getAdminById(userId);

            if (usernow != null) {
                saldoText.setText(usernow.getSaldotext());
                userText.setText(usernow.getNama());
            }
        }

        btnDaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, AProductActivity.class);
                intent.putExtra("kategori", "Daging");
                startActivity(intent);
            }
        });

        btnSayur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, AProductActivity.class);
                intent.putExtra("kategori", "Sayur");
                startActivity(intent);
            }
        });

        btnBuah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, AProductActivity.class);
                intent.putExtra("kategori", "Buah");
                startActivity(intent);
            }
        });

        btnSembako.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, AProductActivity.class);
                intent.putExtra("kategori", "Sembako");
                startActivity(intent);
            }
        });

        btnKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, KeranjangActivity.class);
                startActivity(intent);
            }
        });

        btnIsiSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dbHelper.PendingTopups()) {
                    dbHelper.processPendingTopups();
                    Toast.makeText(AdminActivity.this, "Top Up Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminActivity.this, "No Pending Top Up", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PURCHASED_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                List<PurchasedItem> purchasedItems = data.getParcelableArrayListExtra("purchasedItems");
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

}