package com.example.warline;

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

public class CustomKeranjangAdapter extends BaseAdapter {
    private Context context;
    private List<KeranjangI> selectedProducts;


    public CustomKeranjangAdapter(Context context, List<KeranjangI> selectedProducts) {
        this.context = context;
        this.selectedProducts = selectedProducts;
    }

    @Override
    public int getCount() {
        return selectedProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return selectedProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.keranjang_list_item, parent, false);
        }

        ImageView cartImage = convertView.findViewById(R.id.listCartImage);
        TextView cartName = convertView.findViewById(R.id.listCartNama);
        TextView cartJumlah = convertView.findViewById(R.id.listCartJumlah);
        TextView cartHarga = convertView.findViewById(R.id.listCartHarga);
        Button btnMin = convertView.findViewById(R.id.listItemMin);
        Button btnPlus = convertView.findViewById(R.id.listCartPlus);

        KeranjangI product = selectedProducts.get(position);

        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (KeranjangI cartProduct : selectedProducts) {
                    if (cartProduct.getName().equals(product.getName())) {
                        if (cartProduct.getJumlah() >= 1) {
                            cartProduct.setJumlah(cartProduct.getJumlah() - 1);
                            Intent intent = new Intent(context, KeranjangActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            break;
                        }
                        else{
                            Toast.makeText(context, "Jumlah sudah 0", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (KeranjangI cartProduct : selectedProducts) {
                    if (cartProduct.getName().equals(product.getName())) {
                        if (cartProduct.getJumlah() <= 99) {
                            cartProduct.setJumlah(cartProduct.getJumlah() + 1);
                            Intent intent = new Intent(context, KeranjangActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            break;
                        }
                        else{
                            Toast.makeText(context, "Limit", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });

        KeranjangI cart = selectedProducts.get(position);

        Bitmap bitmap = BitmapFactory.decodeByteArray(cart.getImageResource(), 0, cart.getImageResource().length);

        cartImage.setImageBitmap(bitmap);
        cartName.setText(cart.getName());
        cartJumlah.setText(cart.getJumlah2());
        cartHarga.setText(cart.getHarga());

        return convertView;
    }
}
