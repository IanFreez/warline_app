package com.example.warline;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private ProductManager productManager;
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        ListView listView = findViewById(R.id.listProduct);
        String kategori = getIntent().getStringExtra("kategori");
        List<ProductI> productList = getProductList(kategori);

        CustomListAdapter adapter = new CustomListAdapter(this, productList);
        listView.setAdapter(adapter);
    }

    private List<ProductI> getProductList(String kategori){
        return dbHelper.getProducts(kategori);
    }

}