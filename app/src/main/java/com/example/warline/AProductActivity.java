package com.example.warline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class AProductActivity extends AppCompatActivity {
    private ProductManager productManager;
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aproduct);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        backBtn = findViewById(R.id.backAProduct);

        ListView listView = findViewById(R.id.listAProduct);
        String kategori = getIntent().getStringExtra("kategori");
        List<ProductI> productList = getProductList(kategori);

        CustomList2Adapter adapter = new CustomList2Adapter(this, productList);
        listView.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AProductActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<ProductI> getProductList(String kategori){
        return dbHelper.getProducts(kategori);
    }

}