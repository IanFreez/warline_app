package com.example.warline;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PurchasedItem implements Parcelable {

    private String productName;
    private double price;
    private int quantity;
    private LocalDateTime purchaseTime;

    public PurchasedItem(String productName, double price, int quantity, LocalDateTime purchaseTime) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.purchaseTime = purchaseTime;
    }

    protected PurchasedItem(Parcel in) {
        productName = in.readString();
        price = in.readDouble();
        quantity = in.readInt();

        long epochSecond = in.readLong();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            purchaseTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneId.systemDefault());
        }
    }

    public static final Creator<PurchasedItem> CREATOR = new Creator<PurchasedItem>() {
        @Override
        public PurchasedItem createFromParcel(Parcel in) {
            return new PurchasedItem(in);
        }

        @Override
        public PurchasedItem[] newArray(int size) {
            return new PurchasedItem[size];
        }
    };

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return purchaseTime.format(formatter);
        }
        return null;
    }

    public String getProductDetails() {
        String details = "Product: " + productName +
                "\nPrice: Rp. " + price +
                "\nQuantity: " + quantity;

        return details;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeDouble(price);
        dest.writeInt(quantity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dest.writeLong(purchaseTime.toEpochSecond(ZoneId.systemDefault().getRules().getOffset(purchaseTime)));
        }
    }
}
