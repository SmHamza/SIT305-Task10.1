package com.example.task61foodrescueapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.task61foodrescueapp.data.DatabaseHelper;
import com.example.task61foodrescueapp.model.Food;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    RecyclerView recyclerView;
    com.example.task61foodrescueapp.Adapter adapter;
    List<Food> foodList;
    DatabaseHelper db;
    ImageButton addButton;
    int user_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.myFoodItemsRecyclerView);
        foodList = new ArrayList<>();

        db = new DatabaseHelper(this);
        fetchAllFoodItemsFromDatabase();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, HomeActivity.this, foodList);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        user_ID = intent.getIntExtra("USER_ID",0);

        addButton = findViewById(R.id.myAddFoodButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddFoodItem.class);
                intent.putExtra("USER_ID", user_ID);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            recreate();
        }
    }
    public void showMenu(View view)
    {
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.side_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.home_id:
                Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                intent.putExtra("USER_ID", user_ID);
                startActivity(intent);
                return true;

            case R.id.account_id:
                Intent intent1 = new Intent(HomeActivity.this,AccountActivity.class);
                intent1.putExtra("USER_ID", user_ID);
                startActivity(intent1);
                return true;

            case R.id.my_list_id:
                Intent intent2 = new Intent(HomeActivity.this, MyList.class);
                intent2.putExtra("USER_ID", user_ID);
                startActivity(intent2);
                return true;
            case R.id.cart_id:
                Intent intent3 = new Intent(HomeActivity.this, CartActivity.class);
                intent3.putExtra("USER_ID", user_ID);
                startActivity(intent3);
                return true;
            default:
                return false;
        }
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
    void fetchAllFoodItemsFromDatabase()
    {
        foodList = db.getAllFoodItems();
    }
}