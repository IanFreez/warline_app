package com.example.warline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PurchasedActivity extends AppCompatActivity {
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased);
        DBHelper dbHelper = new DBHelper(this);

        btnBack = findViewById(R.id.backRiwayat);
        ListView purchasedListView = findViewById(R.id.listPurchased);
        SharedPreferences preferences = getSharedPreferences("usernow", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);

        List<Riwayat> purchasedItems = dbHelper.getRiwayat(userId);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PurchasedActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        if (purchasedItems != null) {
            PurchasedAdapter adapter = new PurchasedAdapter(this, purchasedItems);
            purchasedListView.setAdapter(adapter);
        }
    }
}