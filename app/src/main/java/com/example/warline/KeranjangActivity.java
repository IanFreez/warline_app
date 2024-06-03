package com.example.warline;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warline.KeranjangI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KeranjangActivity extends AppCompatActivity {
    DBHelper dbHelper;
    List<KeranjangI> selectedProducts = CartManager.getSelectedProducts();
    TextView totalText;
    Button btnCheckout;
    SQLiteDatabase db;
    Double saldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        ListView cartView = findViewById(R.id.listKeranjang);
        List<KeranjangI> selectedProducts = CartManager.getSelectedProducts();
        dbHelper = new DBHelper(this);

        totalText = findViewById(R.id.totalText);
        btnCheckout = findViewById(R.id.checkoutBtn);

        SharedPreferences preferences = getSharedPreferences("usernow", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);
        if (userId != -1) {
            // Fetch user data from the database using the ID
            Customer usernow = dbHelper.getUserById(userId);

            if (usernow != null) {
                saldo = usernow.getSaldo();
            }
        }

        CustomKeranjangAdapter adapter = new CustomKeranjangAdapter(this, selectedProducts);

        cartView.setAdapter(adapter);

        totalHarga();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (KeranjangI cartProduct : selectedProducts) {
                    int temp_id = cartProduct.getId();
                    int temp_stok = cartProduct.getJumlah();

                    ProductI produk = dbHelper.getProdukById(temp_id);
                    dbHelper.updateProductStok(temp_id, temp_stok);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        String rName = cartProduct.getName();
                        double rHarga = cartProduct.getPrice();
                        int rJumlah = cartProduct.getJumlah();
                        String rTanggal = LocalDateTime.now().toString();
                        db = dbHelper.getWritableDatabase();
                        dbHelper.addHistoryToDatabase(db, temp_id, userId, rName, rHarga, rJumlah, rTanggal);
                        db.close();
                    }
                }

                if (saldo >= total){
                    dbHelper.updateSaldoUser(userId, total);
                }
                checkout(view);

                Intent intent = new Intent(KeranjangActivity.this, PurchasedActivity.class);
                startActivity(intent);
            }
        });
    }
    public void checkout (View view){
        if (selectedProducts.isEmpty()){
            Toast.makeText(KeranjangActivity.this, "Daftar pesanan kosong!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(KeranjangActivity.this, "Pesanan berhasil dibuat", Toast.LENGTH_SHORT).show();
            selectedProducts.clear();
        }
    }
    double total;

    private double calctotal() {
        for (KeranjangI cartProduct : selectedProducts) {
            Double totalT = cartProduct.totalHarga(cartProduct.getPrice(), cartProduct.getJumlah());
            total += totalT;
        }
        return total;
    }
    public void totalHarga(){
        if (selectedProducts.isEmpty()){
            totalText.setText("0");
        }

        total = calctotal();

        NumberFormat f = new DecimalFormat("#,###");
        String textTotal = f.format(total);
        totalText.setText(textTotal);
    }
}