package com.example.task61foodrescueapp.data;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.task61foodrescueapp.AddFoodItem;
import com.example.task61foodrescueapp.model.CartItem;
import com.example.task61foodrescueapp.model.Food;
import com.example.task61foodrescueapp.model.User;
import com.example.task61foodrescueapp.util.Util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int NOT_EXIST = -1;
    public ByteArrayOutputStream objectByteArrayOutputStream;
    public byte[] foodImageInBytes;

    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + Util.USERTABLE_NAME + " (" + Util.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Util.USERNAME + " TEXT," + Util.USER_EMAIL+ " TEXT," +  Util.USER_NUMBER + " TEXT," +
                Util.USER_ADDRESSES + " TEXT, " + Util.USER_PASSWORD + " TEXT)";
        String CREATE_FOOD_TABLE = "CREATE TABLE " + Util.FOOD_TABLE_NAME + " (" + Util.FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Util.USER_ID_FOOD + " INTEGER, " + Util.FOOD_IMAGE + " BLOB, " + Util.FOOD_TITLE + " TEXT," +
                Util.FOOD_DESCRIPTION + " TEXT," + Util.FOOD_DATE_ADDED + " TEXT, " + Util.FOOD_PICK_UP + " TEXT, " +
                Util.FOOD_QUANTITY+ " TEXT, " + Util.FOOD_LOCATION  + " TEXT, " + Util.FOOD_PRICE + " INTEGER)";
        String CREATE_CART_TABLE = "CREATE TABLE " + Util.CART_TABLE_NAME + " (" + Util.CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                Util.FOOD_ID + " INTEGER, " +
                Util.USER_ID_FOOD + " INTEGER, " + Util.FOOD_IMAGE + " BLOB, " + Util.FOOD_TITLE + " TEXT," +
                Util.FOOD_DESCRIPTION + " TEXT," + Util.FOOD_DATE_ADDED + " TEXT, " + Util.FOOD_PICK_UP + " TEXT, " +
                Util.FOOD_QUANTITY+ " TEXT, " + Util.FOOD_LOCATION  + " TEXT, " + Util.FOOD_PRICE + " INTEGER)";
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + Util.LOCATION_TABLE_NAME + " (" + Util.LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Util.FOOD_ID + " INTEGER, " +
                Util.LOCATION_NAME + " STRING, " +
                Util.LATITUDE + " DOUBLE, " +
                Util.LONGITUDE + " DOUBLE)";
        db.execSQL(CREATE_FOOD_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_LOCATION_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + Util.USERTABLE_NAME;
        db.execSQL(DROP_USER_TABLE, new String[]{Util.USERTABLE_NAME});
        String DROP_FOOD_TABLE = "DROP TABLE IF EXISTS " + Util.FOOD_TABLE_NAME;
        db.execSQL(DROP_FOOD_TABLE, new String[]{Util.FOOD_TABLE_NAME});
        String DROP_CART_TABLE = "DROP TABLE IF EXISTS " + Util.CART_TABLE_NAME;
        db.execSQL(DROP_CART_TABLE, new String[]{Util.CART_TABLE_NAME});
        String DROP_LOCATION_TABLE = "DROP TABLE IF EXISTS " + Util.LOCATION_TABLE_NAME;
        db.execSQL(DROP_LOCATION_TABLE, new String[]{Util.LOCATION_TABLE_NAME});
        onCreate(db);
    }
    public long insertUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.USERNAME, user.getUsername());
        contentValues.put(Util.USER_EMAIL, user.getEmail());
        contentValues.put(Util.USER_ADDRESSES, user.getAddress());
        contentValues.put(Util.USER_NUMBER, user.getNumber());
        contentValues.put(Util.USER_PASSWORD, user.getPassword());
        long newRowID = db.insert(Util.USERTABLE_NAME, null, contentValues);
        db.close();
        return newRowID;
    }
    public long insertLocation(int food_id, String locationName, double latitude, double longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.FOOD_ID, food_id);
        contentValues.put(Util.LOCATION_NAME, locationName);
        contentValues.put(Util.LATITUDE, latitude);
        contentValues.put(Util.LONGITUDE, longitude);
        long newRowID = db.insert(Util.LOCATION_TABLE_NAME, null, contentValues);
        db.close();
        return newRowID;
    }
    public List<Double> fetchLocation(int food_id) {
        List<Double> LatLong = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Util.LOCATION_TABLE_NAME,new String[] { Util.LATITUDE,Util.LONGITUDE,}, Util.FOOD_ID + "=?", new String[] {String.valueOf(food_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            LatLong.set(0, cursor.getDouble(cursor.getColumnIndex(Util.LATITUDE)));
            LatLong.set(1, cursor.getDouble(cursor.getColumnIndex(Util.LONGITUDE)));
        }
        if (cursor != null) {
            cursor.close();
        }
        return LatLong;
    }
    public boolean fetchUser(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.USERTABLE_NAME, new String[]{Util.USER_ID}, Util.USERNAME +"=? and " + Util.USER_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        int numberOfRows = cursor.getCount();
        db.close();
        if(numberOfRows > 0)
        {
            return true;
        }
        else{
            return false;
        }
    }
    public int fetchUserID(String str) {
        int res;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Util.USERTABLE_NAME, new String[] { Util.USER_ID,
                }, Util.USERNAME + "=?",
                new String[] { str }, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            res = cursor.getInt(cursor.getColumnIndex(Util.USER_ID));
        }
        else {
            res = NOT_EXIST;
        }
        if (cursor != null) {
            cursor.close();
        }
        return res;
    }
    public long insertFoodItem(Food food){
        SQLiteDatabase db = this.getWritableDatabase();
        Bitmap imageToStoreBitmap = food.getImage();
        objectByteArrayOutputStream = new ByteArrayOutputStream();
        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100,objectByteArrayOutputStream);

        foodImageInBytes = objectByteArrayOutputStream.toByteArray();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.USER_ID_FOOD, food.getUser_id());
        contentValues.put(Util.FOOD_TITLE, food.getFood_title());
        contentValues.put(Util.FOOD_DESCRIPTION, food.getFood_description());
        contentValues.put(Util.FOOD_DATE_ADDED, food.getDate_added());
        contentValues.put(Util.FOOD_PICK_UP, food.getPick_up_times());
        contentValues.put(Util.FOOD_QUANTITY, food.getQuantity());
        contentValues.put(Util.FOOD_LOCATION, food.getLocation());
        contentValues.put(Util.FOOD_IMAGE, foodImageInBytes);
        contentValues.put(Util.FOOD_PRICE, food.getPrice());

        long newRowID = db.insert(Util.FOOD_TABLE_NAME, null, contentValues);
        db.close();
        return newRowID;
    }
    public Food getFood(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.FOOD_TABLE_NAME, new String[]{Util.FOOD_ID, Util.USER_ID_FOOD, Util.FOOD_IMAGE,
                        Util.FOOD_TITLE, Util.FOOD_DESCRIPTION,Util.FOOD_DATE_ADDED, Util.FOOD_PICK_UP,
                        Util.FOOD_QUANTITY, Util.FOOD_LOCATION, Util.FOOD_PRICE}, Util.FOOD_ID+"=?",
                new String[]{String.valueOf(id)}, null,null, null);
        if (cursor.moveToFirst())
        {
            cursor.moveToFirst();
        }
        byte[] imageToStoreBitmap1 = cursor.getBlob(2);
        Bitmap imageToStoreBitmap = BitmapFactory.decodeByteArray(imageToStoreBitmap1, 0 ,imageToStoreBitmap1.length);
        objectByteArrayOutputStream = new ByteArrayOutputStream();
        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100,objectByteArrayOutputStream);
        Food food = new Food(cursor.getInt(0),cursor.getInt(1),imageToStoreBitmap,cursor.getString(3)
                ,cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8)
                ,cursor.getInt(9));
        return food;
    }
    public List<Food> getAllFoodItems()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Food> foodList = new ArrayList<>();
        String query = "SELECT * FROM " + Util.FOOD_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst())
        {
            do{
                byte[] imageBytes = cursor.getBlob(2);
                Bitmap objectBitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
                Food food = new Food();
                food.setId(cursor.getInt(0));
                food.setUser_id(cursor.getInt(1));
                food.setImage(objectBitmap);
                food.setFood_title(cursor.getString(3));
                food.setFood_description(cursor.getString(4));
                food.setDate_added(cursor.getString(5));
                food.setPick_up_times(cursor.getString(6));
                food.setQuantity(cursor.getString(7));
                food.setLocation(cursor.getString(8));
                food.setPrice(cursor.getInt(9));

                foodList.add(food);
            }while(cursor.moveToNext());
            cursor.moveToFirst();
        }
        return  foodList;
    }
    public long insertCartItem(Food food){
        SQLiteDatabase db = this.getWritableDatabase();
        Bitmap imageToStoreBitmap = food.getImage();
        objectByteArrayOutputStream = new ByteArrayOutputStream();
        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100,objectByteArrayOutputStream);

        foodImageInBytes = objectByteArrayOutputStream.toByteArray();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.FOOD_ID, food.getId());
        contentValues.put(Util.USER_ID_FOOD, food.getUser_id());
        contentValues.put(Util.FOOD_TITLE, food.getFood_title());
        contentValues.put(Util.FOOD_DESCRIPTION, food.getFood_description());
        contentValues.put(Util.FOOD_DATE_ADDED, food.getDate_added());
        contentValues.put(Util.FOOD_PICK_UP, food.getPick_up_times());
        contentValues.put(Util.FOOD_QUANTITY, food.getQuantity());
        contentValues.put(Util.FOOD_LOCATION, food.getLocation());
        contentValues.put(Util.FOOD_IMAGE, foodImageInBytes);
        contentValues.put(Util.FOOD_PRICE, food.getPrice());

        long newRowID = db.insert(Util.CART_TABLE_NAME, null, contentValues);
        db.close();
        return newRowID;
    }
    public List<CartItem> getAllCartItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<CartItem> cartItemList = new ArrayList<>();
        String query = "SELECT * FROM " + Util.CART_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                byte[] imageBytes = cursor.getBlob(3);
                Bitmap objectBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                CartItem food = new CartItem();
                food.setId(cursor.getInt(0));
                food.setFood_id(cursor.getInt(1));
                food.setUser_id(cursor.getInt(2));
                food.setImage(objectBitmap);
                food.setFood_title(cursor.getString(4));
                food.setFood_description(cursor.getString(5));
                food.setDate_added(cursor.getString(6));
                food.setPick_up_times(cursor.getString(7));
                food.setQuantity(cursor.getString(8));
                food.setLocation(cursor.getString(9));
                food.setPrice(cursor.getInt(10));

                cartItemList.add(food);
            } while (cursor.moveToNext());
            cursor.moveToFirst();
        }
        return cartItemList;
    }
}

