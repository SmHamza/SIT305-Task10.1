package com.example.task61foodrescueapp.model;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class Food {
    private int id;
    private int user_id;
    private Bitmap image;
    private String food_title;
    private String food_description;
    private String date_added;
    private String pick_up_times;
    private String quantity;
    private String location;
    private int price;
    public ByteArrayOutputStream objectByteArrayOutputStream;
    public Food(int user_id, Bitmap image, String food_title, String food_description, String date_added, String pick_up_times, String quantity, String location, int price) {
        this.user_id = user_id;
        this.image = image;
        this.food_title = food_title;
        this.food_description = food_description;
        this.date_added = date_added;
        this.pick_up_times = pick_up_times;
        this.quantity = quantity;
        this.location = location;
        this.price = price;
    }
    public Food(int food_id, int user_id, Bitmap image, String food_title, String food_description, String date_added, String pick_up_times, String quantity, String location, int price) {
        this.id = food_id;
        this.user_id = user_id;
        this.image = image;
        this.food_title = food_title;
        this.food_description = food_description;
        this.date_added = date_added;
        this.pick_up_times = pick_up_times;
        this.quantity = quantity;
        this.location = location;
        this.price = price;
    }

    public Food() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getFood_title() {
        return food_title;
    }

    public void setFood_title(String food_title) {
        this.food_title = food_title;
    }

    public String getFood_description() {
        return food_description;
    }

    public void setFood_description(String food_description) {
        this.food_description = food_description;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getPick_up_times() {
        return pick_up_times;
    }

    public void setPick_up_times(String pick_up_times) {
        this.pick_up_times = pick_up_times;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }
}
