package com.example.warline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private Context context;
    private boolean isDatabaseCreated;
    private static final String DATABASE_NAME = "WarlineDatabase";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_HISTORY = "histori";
    public static final String TABLE_TOPUP = "topup";
    public static final String TABLE_ADMIN = "admin";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IDP = "id_produk";
    public static final String COLUMN_IDU = "id_user";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_STOCK = "stock";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_TYPE = "type";

    public static final String TABLE_USERS = "user";
    public static final String COLUMN_SALDO = "saldo";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASS = "pass";
    public static final String COLUMN_DATE = "tanggal";
    public static final String COLUMN_JUMLAH = "jumlah";
    public static final String COLUMN_STATUS = "status";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.isDatabaseCreated = false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createProductsTable(db);
        createUserTable(db);
        createHistoryTable(db);
        createTopupTable(db);
        createAdminTable(db);

        if (!isDatabaseCreated) {
            addUser(db);
            addProduct(db);
            addAdmin(db);
            isDatabaseCreated = true;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
            onCreate(db);
        }
    }

    private void createProductsTable(SQLiteDatabase db){
        String createTableQuery = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_STOCK + " INTEGER," +
                COLUMN_PRICE + " DOUBLE," +
                COLUMN_IMAGE + " BLOB," +
                COLUMN_TYPE + " TEXT);";
        db.execSQL(createTableQuery);
    }

    private void createUserTable(SQLiteDatabase db){
        String createTableQuery = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_SALDO + " DOUBLE," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_PASS + " TEXT);";
        db.execSQL(createTableQuery);
    }

    private void createAdminTable(SQLiteDatabase db){
        String createTableQuery = "CREATE TABLE " + TABLE_ADMIN + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_SALDO + " DOUBLE," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_PASS + " TEXT);";
        db.execSQL(createTableQuery);
    }

    private void createHistoryTable(SQLiteDatabase db){
        String createTableQuery = "CREATE TABLE " + TABLE_HISTORY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_IDP + " INTEGER," +
                COLUMN_IDU + " INTEGER," +
                COLUMN_NAME + " TEXT," +
                COLUMN_PRICE + " DOUBLE," +
                COLUMN_JUMLAH + " INTEGER," +
                COLUMN_DATE + " TEXT," +
                "FOREIGN KEY (" + COLUMN_IDU + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")," +
                "FOREIGN KEY (" + COLUMN_IDP + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_ID + ")" +
                ");";
        db.execSQL(createTableQuery);
    }

    private void createTopupTable(SQLiteDatabase db){
        String createTableQuery = "CREATE TABLE " + TABLE_TOPUP + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_IDU + " INTEGER," +
                COLUMN_SALDO + " DOUBLE," +
                COLUMN_DATE + " TEXT," +
                COLUMN_STATUS + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_IDU + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")" +
                ");";
        db.execSQL(createTableQuery);
    }

    //retrieve produk dengan kategori
    public List<ProductI> getProducts(String category) {
        List<ProductI> productList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_PRICE, COLUMN_STOCK, COLUMN_IMAGE, COLUMN_TYPE};
        String selection = COLUMN_TYPE + " = ?";
        String[] selectionArgs = {category};

        Cursor cursor = db.query(TABLE_PRODUCTS, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                int stock = cursor.getInt(cursor.getColumnIndex(COLUMN_STOCK));
                byte[] imageResource = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));

                if ("Daging".equals(category)) {
                    productList.add(new ProductI.Daging(id, name, price, stock, imageResource));
                } else if ("Buah".equals(category)) {
                    productList.add(new ProductI.Buah(id, name, price, stock, imageResource));
                } else if ("Sayur".equals(category)) {
                    productList.add(new ProductI.Sayur(id, name, price, stock, imageResource));
                } else if ("Sembako".equals(category)) {
                    productList.add(new ProductI.Sembako(id, name, price, stock, imageResource));
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return productList;
    }

    public ProductI getProductsValue(String columnName, String columnValue){
        ProductI produk = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_PRICE, COLUMN_STOCK, COLUMN_IMAGE, COLUMN_TYPE};

        String selection = columnName + " = ?";
        String[] selectionArgs = {columnValue};

        Cursor cursor = db.query(TABLE_PRODUCTS, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String nama = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                double harga = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                int stok = cursor.getInt(cursor.getColumnIndex(COLUMN_STOCK));
                byte[] imageResource = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
                produk = new ProductI(id, nama, harga, stok, imageResource);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return produk;
    }

    public ProductI getProdukById(int id){
        return getProductsValue(COLUMN_ID, String.valueOf(id));
    }

    public Customer getUserValue(String columnName, String columnValue){
        Customer user = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_SALDO, COLUMN_USERNAME, COLUMN_PASS};

        String selection = columnName + " = ?";
        String[] selectionArgs = {columnValue};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int ids = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String nama = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                double saldo = cursor.getDouble(cursor.getColumnIndex(COLUMN_SALDO));
                String usernames = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
                String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASS));
                user = new Customer(ids, usernames, password, saldo, nama);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return user;
    }

    public Customer getUserByUsername(String username){
        return getUserValue(COLUMN_USERNAME, username);
    }

    public Customer getUserById(int id){
        return getUserValue(COLUMN_ID, String.valueOf(id));
    }

    public Admin getAdminValue(String columnName, String columnValue){
        Admin admin = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_SALDO, COLUMN_USERNAME, COLUMN_PASS};

        String selection = columnName + " = ?";
        String[] selectionArgs = {columnValue};

        Cursor cursor = db.query(TABLE_ADMIN, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int ids = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String nama = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                double saldo = cursor.getDouble(cursor.getColumnIndex(COLUMN_SALDO));
                String usernames = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
                String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASS));
                admin = new Admin(ids, usernames, password, saldo, nama);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return admin;
    }

    public Admin getAdminByUsername(String username){
        return getAdminValue(COLUMN_USERNAME, username);
    }

    public Admin getAdminById(int id){
        return getAdminValue(COLUMN_ID, String.valueOf(id));
    }
    public boolean isAdminIdExists(int adminId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(adminId)};

        Cursor cursor = db.query(TABLE_ADMIN, columns, selection, selectionArgs, null, null, null);

        boolean adminIdExists = cursor != null && cursor.moveToFirst();

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return adminIdExists;
    }

    public Topups getTopupValue(String columnName, String columnValue){
        Topups topup = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_IDU, COLUMN_SALDO, COLUMN_DATE, COLUMN_STATUS};

        String selection = columnName + " = ?";
        String[] selectionArgs = {columnValue};

        Cursor cursor = db.query(TABLE_TOPUP, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                int iduser= cursor.getInt(cursor.getColumnIndex(COLUMN_IDU));
                double saldo = cursor.getDouble(cursor.getColumnIndex(COLUMN_SALDO));
                String tanggal = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                int statusValue = cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS));
                boolean status = (statusValue != 0);
                topup = new Topups(id, iduser, saldo, tanggal, status);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return topup;
    }

    public Topups getTopupByIdU(int idu){
        return getTopupValue(COLUMN_IDU, String.valueOf(idu));
    }
    public Riwayat getRiwayatValue(String columnName, String columnValue){
        Riwayat riwayat = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_IDP, COLUMN_IDU, COLUMN_NAME, COLUMN_PRICE, COLUMN_JUMLAH, COLUMN_DATE};

        String selection = columnName + " = ?";
        String[] selectionArgs = {columnValue};

        Cursor cursor = db.query(TABLE_HISTORY, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                int idp = cursor.getInt(cursor.getColumnIndex(COLUMN_IDP));
                int idu = cursor.getInt(cursor.getColumnIndex(COLUMN_IDU));
                String nama = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                double harga = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                int jumlah = cursor.getInt(cursor.getColumnIndex(COLUMN_JUMLAH));
                String tanggal = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                riwayat = new Riwayat(id, idp, idu, nama, harga, jumlah, tanggal);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return riwayat;
    }

    public List<Riwayat> getRiwayat(int id){
        List<Riwayat> riwayatList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_IDP, COLUMN_IDU, COLUMN_NAME, COLUMN_PRICE, COLUMN_JUMLAH, COLUMN_DATE};

        String selection = COLUMN_IDU + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(TABLE_HISTORY, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int ids = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                int idp = cursor.getInt(cursor.getColumnIndex(COLUMN_IDP));
                int idu = cursor.getInt(cursor.getColumnIndex(COLUMN_IDU));
                String nama = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                double harga = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                int jumlah = cursor.getInt(cursor.getColumnIndex(COLUMN_JUMLAH));
                String tanggal = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));

                riwayatList.add(new Riwayat(ids, idp, idu, nama, harga, jumlah, tanggal));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return riwayatList;
    }
    public Riwayat getRiwayatById(int id){
        return getRiwayatValue(COLUMN_IDU, String.valueOf(id));
    }
    private void addUserToDatabase(SQLiteDatabase db, String nama, double saldo, String username, String pass) {
        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_NAME, nama);
        values.put(DBHelper.COLUMN_SALDO, saldo);
        values.put(DBHelper.COLUMN_USERNAME, username);
        values.put(DBHelper.COLUMN_PASS, pass);

        long newRowId = db.insert(DBHelper.TABLE_USERS, null, values);
    }

    private void addAdminToDatabase(SQLiteDatabase db, String nama, double saldo, String username, String pass) {
        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_NAME, nama);
        values.put(DBHelper.COLUMN_SALDO, saldo);
        values.put(DBHelper.COLUMN_USERNAME, username);
        values.put(DBHelper.COLUMN_PASS, pass);

        long newRowId = db.insert(DBHelper.TABLE_ADMIN, null, values);
    }

    public void addHistoryToDatabase(SQLiteDatabase db, int idp, int idu, String nama, double harga, int jumlah, String tanggal) {
        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_IDP, idp);
        values.put(DBHelper.COLUMN_IDU, idu);
        values.put(DBHelper.COLUMN_NAME, nama);
        values.put(DBHelper.COLUMN_PRICE, harga);
        values.put(DBHelper.COLUMN_JUMLAH, jumlah);
        values.put(DBHelper.COLUMN_DATE, tanggal);

        long newRowId = db.insert(DBHelper.TABLE_HISTORY, null, values);
    }

    private void addUser(SQLiteDatabase db){
        addUserToDatabase(db, "Zozo", 500000, "zozo123", "akuzozo123");
    }

    private void addAdmin(SQLiteDatabase db){
        addAdminToDatabase(db, "Ian", 2000000, "ian1234", "akuian123");
    }

    public void addTopupToDatabase(SQLiteDatabase db, int idu, double saldo, String date, int status) {
        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_IDU, idu);
        values.put(DBHelper.COLUMN_SALDO, saldo);
        values.put(DBHelper.COLUMN_DATE, date);
        values.put(DBHelper.COLUMN_STATUS, status);

        long newRowId = db.insert(DBHelper.TABLE_TOPUP, null, values);
    }

    private void addProductToDatabase(SQLiteDatabase db, String name, int stock, double price, int imageResource, String type) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, name);
        values.put(DBHelper.COLUMN_STOCK, stock);
        values.put(DBHelper.COLUMN_PRICE, price);
        byte[] imageData = convertImageResourceToByteArray(imageResource);

        values.put(DBHelper.COLUMN_IMAGE, imageData);
        values.put(DBHelper.COLUMN_TYPE, type);

        long newRowId = db.insert(DBHelper.TABLE_PRODUCTS, null, values);
    }
    private byte[] convertImageResourceToByteArray(int imageResource) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageResource);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void addProduct(SQLiteDatabase db){
        addProductToDatabase(db, "Daging Ayam (1/4 Kg)", 20, 15000, R.mipmap.daging_ayam_foreground, "Daging");
        addProductToDatabase(db, "Daging Ayam (1/2 Kg)", 20, 25000, R.mipmap.daging_ayam_foreground, "Daging");
        addProductToDatabase(db, "Daging Ayam (1 Kg)", 20, 40000, R.mipmap.daging_ayam_foreground, "Daging");
        addProductToDatabase(db, "Daging Sapi (1/4 Kg)", 10, 30000, R.mipmap.daging_sapi_foreground, "Daging");
        addProductToDatabase(db, "Daging Sapi (1/2 Kg)", 10, 55000, R.mipmap.daging_sapi_foreground, "Daging");
        addProductToDatabase(db, "Daging Sapi (1 Kg)", 10, 100000, R.mipmap.daging_sapi_foreground, "Daging");

        addProductToDatabase(db, "Apel (1 kg)", 5, 40000, R.mipmap.apel_foreground, "Buah");
        addProductToDatabase(db, "Mangga (1 kg)", 5, 40000, R.mipmap.mangga_foreground, "Buah");
        addProductToDatabase(db, "Melon (1 kg)", 5, 30000, R.mipmap.melon_foreground, "Buah");
        addProductToDatabase(db, "Semangka (1 Kg)", 5, 20000, R.mipmap.semangka_foreground, "Buah");
        addProductToDatabase(db, "Naga (1 Kg)", 5, 20000, R.mipmap.naga_foreground, "Buah");
        addProductToDatabase(db, "Jeruk (1 Kg)", 5, 25000, R.mipmap.jeruk_foreground, "Buah");

        addProductToDatabase(db, "Kentang (1 kg)", 15, 12000, R.mipmap.kentang_foreground, "Sayur");
        addProductToDatabase(db, "Wortel (1 kg)", 15, 15000, R.mipmap.wortel_foreground, "Sayur");
        addProductToDatabase(db, "Kacang Panjang (1 kg)", 20, 10000, R.mipmap.kacang_panjang_foreground, "Sayur");
        addProductToDatabase(db, "Kembang Kol (1 Kg)", 20, 9000, R.mipmap.kembang_kol_foreground, "Sayur");
        addProductToDatabase(db, "Sawi Putih (1 Kg)", 20, 10000, R.mipmap.sawi_putih_foreground, "Sayur");
        addProductToDatabase(db, "Kacang Buncis (1 Kg)", 20, 8000, R.mipmap.buncis_foreground, "Sayur");

        addProductToDatabase(db, "Beras Putih (1 Kg)", 35, 14000, R.mipmap.beras_foreground, "Sembako");
        addProductToDatabase(db, "Minyak (1 lt)", 35, 15000, R.mipmap.minyak_foreground, "Sembako");
        addProductToDatabase(db, "Gula (1 Kg)", 35, 12000, R.mipmap.gula_foreground, "Sembako");
        addProductToDatabase(db, "Tepung (1 Kg)", 55, 10000, R.mipmap.tepung_foreground, "Sembako");
        addProductToDatabase(db, "Telur (1 Kg)", 15, 25000, R.mipmap.telur_foreground, "Sembako");
        addProductToDatabase(db, "Gas LPG (3 Kg)", 25, 20000, R.mipmap.gas_foreground, "Sembako");
    }

    public void updateSaldoUser(int id, double total){
        double saldo = getUserById(id).getSaldo()-total;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_SALDO, saldo);
        db.update(TABLE_USERS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateProductStok(int id, int total){
        ProductI produk = getProdukById(id);
        if (produk != null){
            int tempstok = produk.getStok();
            int newStok = tempstok - total;

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_STOCK, newStok);
            db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            db.close();
        }
    }

    public void plusStok(int id, int total){
        ProductI produk = getProdukById(id);
        if (produk != null){
            int tempstok = produk.getStok();
            int newStok = tempstok + total;

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_STOCK, newStok);
            db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            db.close();
        }
    }

    public void minStok(int id, int total){
        ProductI produk = getProdukById(id);
        if (produk != null){
            int tempstok = produk.getStok();
            int newStok = tempstok - total;

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_STOCK, newStok);
            db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            db.close();
        }
    }
    public void updatePass(int id, String pass){
        Customer user = getUserById(id);
        if (user != null){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_PASS, pass);
            db.update(TABLE_USERS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            db.close();
        }
    }

    public void processPendingTopups() {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = {COLUMN_ID, COLUMN_IDU, COLUMN_SALDO, COLUMN_DATE, COLUMN_STATUS};
        String selection = COLUMN_STATUS + " = ?";
        String[] selectionArgs = {"0"};

        Cursor cursor = db.query(TABLE_TOPUP, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                int idu = cursor.getInt(cursor.getColumnIndex(COLUMN_IDU));
                double saldo = cursor.getDouble(cursor.getColumnIndex(COLUMN_SALDO));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));

                updateSaldoUser(idu, saldo);

                String updateQuery = "UPDATE " + TABLE_TOPUP + " SET " + COLUMN_STATUS + " = 1 WHERE " + COLUMN_ID + " = " + id;
                db.execSQL(updateQuery);

            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
    }

    public boolean PendingTopups() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_ID};
        String selection = COLUMN_STATUS + " = ?";
        String[] selectionArgs = {"0"};

        Cursor cursor = db.query(TABLE_TOPUP, columns, selection, selectionArgs, null, null, null);

        boolean hasPendingTopups = cursor != null && cursor.moveToFirst();

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return hasPendingTopups;
    }
}