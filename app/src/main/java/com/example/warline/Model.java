package com.example.warline;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.NumberFormat;

interface ModelProduct{
    int getId();
    String getName();
    String getHarga();
    double getPrice();
    int getStok();
    byte[] getImageResource();

}
abstract class Product implements ModelProduct{
    private int id;
    private String name;
    private String harga;
    private double price;
    private int stok;
    private byte[] ImageResource;
    private int quantity;

    public String getName() {
        return name;
    }

    public String getHarga() {
        NumberFormat f = new DecimalFormat("#,###");
        harga = f.format(price);
        return "Rp. " + harga;
    }
    public int getId(){
        return id;
    }
    public double getPrice() {
        return price;
    }
    public void updateStok(int newStok){
        this.stok = this.stok - newStok;
    }
    public int getStok() {
        return stok;
    }
    public String getStok2(){
        return Integer.toString(stok);
    }
    public byte[] getImageResource() {
        return ImageResource;
    }
    public int getJumlah() {
        return quantity;
    }
    public void setJumlah(int jumlah) {
        this.quantity = jumlah;
    }

    public abstract void setStok(int stok);

    public String getJumlah2(){
        return Integer.toString(quantity);
    }
}

interface ModelUser {
    int getId();
    String getUsername();
    String getPass();
    double getSaldo();
    String getNama();
    String getSaldotext();
}
abstract class User implements ModelUser{
    private int id;
    private String username;
    private String pass;
    private double saldo;
    private String nama;
    private String saldotext;

    public User(int id, String username, String pass, double saldo, String nama){
        this.id = id;
        this.username = username;
        this.pass = pass;
        this.saldo = saldo;
        this.nama = nama;
    }

    @Override
    public int getId(){
        return id;
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public String getPass() {
        return pass;
    }

    @Override
    public String getNama() {
        return nama;
    }

    @Override
    public double getSaldo() {
        return saldo;
    }

    @Override
    public String getSaldotext() {
        NumberFormat f = new DecimalFormat("#,###");
        saldotext = f.format(saldo);
        return "Rp. " + saldotext;
    }
}

interface ModelHistori {
    int getId();
    int getUserId();
    int getProdukId();
    String getProdukName();
    double getHarga();
    int getJumlah();
    String gettanggal();
}

abstract class Histori implements ModelHistori{
    private int id;
    private int userId;
    private int produkId;
    private String produkName;
    private double harga;
    private int jumlah;
    private String tanggal;

    public Histori(int id, int userId, int produkId, String produkName, double harga, int jumlah, String tanggal){
        this.id = id;
        this.userId = userId;
        this.produkId = produkId;
        this.produkName = produkName;
        this.harga = harga;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
    }

    @Override
    public int getId(){
        return id;
    }
    @Override
    public int getUserId(){
        return userId;
    };
    @Override
    public int getProdukId(){
        return produkId;
    };
    @Override
    public String getProdukName(){
        return produkName;
    };
    @Override
    public double getHarga(){
        return harga;
    };
    @Override
    public int getJumlah(){
        return jumlah;
    };
    @Override
    public String gettanggal(){
        return tanggal;
    };
}

interface ModelTopup {
    int getId();
    int getIdUser();
    double getSaldo();
    String getTanggal();
    boolean getStatus();
    int getStatusValue();
}

abstract class Topup implements ModelTopup{
    private int id;
    private int userId;
    private double saldo;
    private String tanggal;
    private boolean status;

    public Topup(int id, int userId, double saldo, String tanggal, boolean status){
        this.id = id;
        this.userId = userId;
        this.saldo = saldo;
        this.tanggal = tanggal;
        this.status = status;
    }

    @Override
    public int getId(){
        return id;
    }
    @Override
    public int getIdUser(){
        return userId;
    }
    @Override
    public double getSaldo(){
        return saldo;
    }
    @Override
    public String getTanggal(){
        return tanggal;
    }
    @Override
    public boolean getStatus() {
        return status;
    }
    @Override
    public int getStatusValue(){
        if (getStatus()){
            return 1;
        }else{
            return 0;
        }
    }
}