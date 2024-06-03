package com.example.warline;

import com.example.warline.CartManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CustomList2Adapter extends BaseAdapter {
    private Context context;
    DBHelper dbHelper;
    private List<? extends ProductI> productList;

    public CustomList2Adapter(Context context, List<? extends ProductI> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false);
            notifyDataSetChanged();
        }

        ImageView productImage = convertView.findViewById(R.id.listItemImage);
        TextView productName = convertView.findViewById(R.id.listItemNama);
        TextView productStok = convertView.findViewById(R.id.listItemStok);
        TextView productHarga = convertView.findViewById(R.id.listItemHarga);
        Button addToCartButton = convertView.findViewById(R.id.listItemButton);
        Button reduceItem = convertView.findViewById(R.id.minusButton);

        dbHelper = new DBHelper(this.context);
        reduceItem.setVisibility(View.VISIBLE);

        ProductI product = productList.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImageResource(), 0, product.getImageResource().length);

        productImage.setImageBitmap(bitmap);
        productName.setText(product.getName());
        productStok.setText(product.getStok2());
        productHarga.setText(product.getHarga());

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp_id = product.getId();
                dbHelper.plusStok(temp_id, 1);
            }
        });

        reduceItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp_id = product.getId();
                dbHelper.minStok(temp_id, 1);
            }
        });

        return convertView;
    }
}


