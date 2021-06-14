package com.example.task61foodrescueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task61foodrescueapp.data.DatabaseHelper;
import com.example.task61foodrescueapp.model.Food;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodDetails extends AppCompatActivity implements OnMapReadyCallback {
    ImageView image;
    TextView title, description, date, pickUptime,quantity, location;
    MapView mapView;
    Button addToCartButton, payPalBtn;
    boolean checkPermission;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    int id, user_ID;
    String locationName;
    List<Address> addresses;
    double latitude;
    double longitude;
    DatabaseHelper db;
    Food food;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        title = findViewById(R.id.food_dTitle);
        description = findViewById(R.id.food_dDescription);
        date = findViewById(R.id.food_dDate);
        pickUptime = findViewById(R.id.food_dTime);
        quantity = findViewById(R.id.food_dQuantity);
        location = findViewById(R.id.food_dLocation);
        mapView = findViewById(R.id.mapView);
        addToCartButton = findViewById(R.id.add_to_cartButton);
        payPalBtn = findViewById(R.id.payButton);
        image = findViewById(R.id.fooddItemViewImage);
        Intent intent = getIntent();
        user_ID = intent.getIntExtra("USER_ID",0);
        id = intent.getIntExtra("ID", 0);
        db = new DatabaseHelper(this);
        food = db.getFood(id);

        image.setImageBitmap(food.getImage());
        title.setText(food.getFood_title());
        description.setText(food.getFood_description());
        date.setText(food.getDate_added());
        pickUptime.setText(food.getPick_up_times());
        quantity.setText(food.getQuantity());
        location.setText(food.getLocation());

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long result = db.insertCartItem(food);
                if (result == -1)
                {
                    Toast.makeText(FoodDetails.this, "Error while adding item", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent1 = new Intent(FoodDetails.this, HomeActivity.class);
                    intent.putExtra("USER_ID", user_ID);
                    startActivity(intent1);
                }
            }
        });
        Bundle mapViewBundle = null;
        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

//    private  boolean checkGooglePlayServices(){
//        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
//        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
//        if(result== ConnectionResult.SUCCESS){
//            return true;
//        }
//        else if (googleApiAvailability.isUserResolvableError(result)){
//            Dialog dialog= googleApiAvailability.getErrorDialog(this, result, 201, new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    Toast.makeText(FoodDetails.this, "User cancelled dialog", Toast.LENGTH_SHORT).show();
//                }
//            });
//            dialog.show();
//        }
//        return false;
//    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Geocoder geocoder = new Geocoder(FoodDetails.this, Locale.getDefault());

//        addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        try {
            addresses = geocoder.getFromLocationName(location.getText().toString(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        latitude = addresses.get(0).getLatitude();
        longitude = addresses.get(0).getLongitude();
        LatLng place = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(place).title(locationName));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 12));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if(mapViewBundle == null)
        {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}