package com.example.warline;

import android.os.Parcelable;
import android.os.Parcel;

public class KeranjangI extends ProductI {
    public KeranjangI(int id, String name, int quantity, double price, byte[] imageResource) {
        super(id, name, quantity, price, imageResource);
    }
    double total;
    public double totalHarga(double harga, int jumlah){
        total = harga*jumlah;
        return total;
    }

}
