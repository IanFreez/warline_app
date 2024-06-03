package com.example.warline;

import com.example.warline.CartManager;
import android.content.Context;
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

public class CustomListAdapter extends BaseAdapter {
    private Context context;
    DBHelper dbHelper;
    private List<? extends ProductI> productList;

    public CustomListAdapter(Context context, List<? extends ProductI> productList) {
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
        }

        ImageView productImage = convertView.findViewById(R.id.listItemImage);
        TextView productName = convertView.findViewById(R.id.listItemNama);
        TextView productStok = convertView.findViewById(R.id.listItemStok);
        TextView productHarga = convertView.findViewById(R.id.listItemHarga);
        Button addToCartButton = convertView.findViewById(R.id.listItemButton);
        Button reduceItem = convertView.findViewById(R.id.minusButton);

        reduceItem.setVisibility(View.GONE);

        ProductI product = productList.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImageResource(), 0, product.getImageResource().length);

        productImage.setImageBitmap(bitmap);
        productName.setText(product.getName());
        productStok.setText(product.getStok2());
        productHarga.setText(product.getHarga());

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(productList.get(position));
            }
        });

        reduceItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return convertView;
    }

    public void addToCart(ProductI product) {
        List<KeranjangI> selectedProducts = CartManager.getSelectedProducts();
        boolean productExists = false;
        for (KeranjangI cartProduct : selectedProducts) {
            if (cartProduct.getName().equals(product.getName())) {
                productExists = true;
                if (cartProduct.getJumlah() + 1 > product.getStok()){
                    Toast.makeText(context, "Stok produk terbatas", Toast.LENGTH_SHORT).show();
                    return;
                }
                cartProduct.setJumlah(cartProduct.getJumlah() + 1);
                Toast.makeText(context, "Produk ditambahkan", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (!productExists){
            if(1 > product.getStok()){
                Toast.makeText(context, "Stok produk terbatas", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedProducts.add(new KeranjangI(product.getId(), product.getName(),1, product.getPrice(), product.getImageResource()));
            Toast.makeText(context, "Produk ditambahkan", Toast.LENGTH_SHORT).show();
        }
        CartManager.setSelectedProducts(selectedProducts);
    }

    private boolean isAdminIdFound(int adminId) {
        return dbHelper.isAdminIdExists(adminId);
    }
}


