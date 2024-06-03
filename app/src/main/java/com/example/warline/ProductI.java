package com.example.warline;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ProductI extends Product{
    private int id;
    private String name;
    private String harga;
    private double price;
    private int stok;
    private byte[] ImageResource;
    private int quantity;

    public ProductI(int id, String name, double price, int stok, byte[] ImageResource) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stok = stok;
        this.ImageResource = ImageResource;
    }

    public ProductI(int id, String name, int quantity, double price, byte[] ImageResource){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.ImageResource = ImageResource;
    }
    double total;
    public double totalHarga(double harga, int jumlah){
        total = harga*jumlah;
        return total;
    }
    @Override
    public int getId(){
        return id;
    }
    @Override
    public String getName(){
        return name;
    }
    @Override
    public String getHarga() {
        NumberFormat f = new DecimalFormat("#,###");
        harga = f.format(price);
        return "Rp. " + harga;
    }
    @Override
    public double getPrice() {
        return price;
    }
    @Override
    public void updateStok(int newStok){
        this.stok = this.stok - newStok;
    }
    @Override
    public int getStok() {
        return stok;
    }
    @Override
    public String getStok2(){
        return Integer.toString(stok);
    }
    @Override
    public byte[] getImageResource() {
        return ImageResource;
    }
    @Override
    public int getJumlah() {
        return quantity;
    }
    @Override
    public void setJumlah(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public void setStok(int stok){
        this.stok = stok;
    }
    @Override
    public String getJumlah2(){
        return Integer.toString(quantity);
    }

    public static class Daging extends ProductI{
        public Daging(int id, String name, double price, int stok, byte[] ImageResource){
            super(id, name, price, stok, ImageResource);
        }
    }

    public static class Sayur extends ProductI{
        public Sayur(int id, String name, double price, int stok, byte[] ImageResource){
            super(id, name, price, stok, ImageResource);
        }
    }

    public static class Buah extends ProductI{
        public Buah(int id, String name, double price, int stok, byte[] ImageResource){
            super(id, name, price, stok, ImageResource);
        }
    }

    public static class Sembako extends ProductI{
        public Sembako(int id, String name, double price, int stok, byte[] ImageResource){
            super(id, name, price, stok, ImageResource);
        }
    }

}
