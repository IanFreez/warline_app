package com.example.warline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDateTime;

public class TopupActivity extends AppCompatActivity {
    SQLiteDatabase db;
    DBHelper dbHelper;
    Button btnRequest;
    EditText editTopup;
    Topups topups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        btnRequest = findViewById(R.id.buttonTopup);
        editTopup = findViewById(R.id.editTopup);

        SharedPreferences preferences = getSharedPreferences("usernow", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);
        dbHelper = new DBHelper(this);

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topups = dbHelper.getTopupByIdU(userId);
                if (topups != null) {
                    if(topups.getStatus()){
                        Toast.makeText(TopupActivity.this, "Tunggu request sebelumnya selesai diproses", Toast.LENGTH_SHORT).show();
                    }else{
                        double saldo = Double.parseDouble(editTopup.getText().toString());
                        String tanggal = "";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            tanggal = LocalDateTime.now().toString();
                        }
                        db = dbHelper.getWritableDatabase();
                        dbHelper.addTopupToDatabase(db, userId, saldo, tanggal, 1);
                        db.close();
                    }
                }else{
                    double saldo = Double.parseDouble(editTopup.getText().toString());
                    String tanggal = "";
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        tanggal = LocalDateTime.now().toString();
                    }
                    db = dbHelper.getWritableDatabase();
                    dbHelper.addTopupToDatabase(db, userId, saldo, tanggal, 1);
                    db.close();
                }
            }
        });
    }
}