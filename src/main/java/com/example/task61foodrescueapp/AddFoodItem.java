package com.example.task61foodrescueapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.task61foodrescueapp.data.DatabaseHelper;
import com.example.task61foodrescueapp.model.Food;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddFoodItem extends AppCompatActivity implements LocationListener {
    EditText food_title, food_description,time_added,food_quantity,location, price;
    CalendarView calendarView;
    ImageView image;
    Button saveButton;
    String date;
    int PICK_IMAGE_REQUEST = 100;
    LocationManager locationManager;
    List<Address> addresses;
    Uri imageFilePath;
    Bitmap imageToStore;
    DatabaseHelper db;
    double latitude;
    double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);

        image = findViewById(R.id.insert_image);
        food_title = findViewById(R.id.imageTitle);
        food_description = findViewById(R.id.imageDescription);
        calendarView = findViewById(R.id.calendarView);
        time_added = findViewById(R.id.timeInputText);
        food_quantity = findViewById(R.id.quantityInputText);
        location = findViewById(R.id.locationInputText);
        saveButton = findViewById(R.id.saveButton);
        price = findViewById(R.id.priceInputText);
        db = new DatabaseHelper(AddFoodItem.this);
        Intent intent = getIntent();
        int user_Id = intent.getIntExtra("USER_ID",0);

        location.setFocusable(false);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        Places.initialize(getApplicationContext(), "AIzaSyDDcThuULd9uphTJ7jMHjua45oJLIxU0Hg");

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(AddFoodItem.this);
                startActivityForResult(intent, 101);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        date = dayOfMonth + "/" + (month+1) + "/" + year;
                    }
                });

                if (TextUtils.isEmpty(food_title.getText().toString()))
                {
                    Toast.makeText(AddFoodItem.this, "Title Field is Empty", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(food_description.getText().toString()))
                {
                    Toast.makeText(AddFoodItem.this, "Description Field is Empty", Toast.LENGTH_SHORT).show();
                }
                else if (date == null)
                {
                    Toast.makeText(AddFoodItem.this, "Date Field is Empty", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(time_added.getText().toString()))
                {
                    Toast.makeText(AddFoodItem.this, "Time Field is Empty", Toast.LENGTH_SHORT).show();
                }

                else if ( TextUtils.isEmpty(food_quantity.getText().toString()))
                {
                    Toast.makeText(AddFoodItem.this, "Quantity Field is Empty", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(location.getText().toString()))
                {
                    Toast.makeText(AddFoodItem.this, "Location Field is Empty", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(price.getText().toString()))
                {
                    Toast.makeText(AddFoodItem.this, "Location Field is Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    db.insertFoodItem(new Food(user_Id, imageToStore,food_title.getText().toString(),food_description.getText().toString(),date,time_added.getText().toString(),food_quantity.getText().toString(),location.getText().toString(),Integer.parseInt(price.getText().toString())));
                    Intent intent = new Intent(AddFoodItem.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("USER_ID", user_Id);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    public void chooseImage(View objectView)
    {
        try
        {
            confirmDialog();
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {
            case 100:
                try {
                    super.onActivityResult(requestCode, resultCode, data);
                    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
                        ;
                    {
                        imageFilePath = data.getData();
                        imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);

                        image.setImageBitmap(imageToStore);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case 101:
                if (requestCode == 101 && resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    location.setText(place.getAddress());
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;

                    List<Food> foodList = db.getAllFoodItems();
                    int food_id = foodList.size() + 1;

                    long result = db.insertLocation(food_id, location.getText().toString(), latitude, longitude);
                    if(result != -1){
                        Toast.makeText(this, "Food_id is: " + food_id, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, "food ID: " + food_id + " Latitude: " + latitude + " Longitude: " + longitude , Toast.LENGTH_SHORT).show();
                    }
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    void confirmDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Allow the app to access photos,media and files on your device?");
        builder.setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent objectIntent = new Intent();
                objectIntent.setType("image/*");
                objectIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(objectIntent,PICK_IMAGE_REQUEST);
            }
        });
        builder.setNegativeButton("DENY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location1) {
//        Toast.makeText(this, "" + location.getLatitude()+","+ location.getLongitude(), Toast.LENGTH_SHORT).show();
        latitude =  location1.getLatitude();
        longitude =  location1.getLongitude();
        try
        {
            Geocoder geocoder = new Geocoder(AddFoodItem.this, Locale.getDefault());
            addresses = geocoder.getFromLocation(location1.getLatitude(),location1.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            location.setText(address);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}