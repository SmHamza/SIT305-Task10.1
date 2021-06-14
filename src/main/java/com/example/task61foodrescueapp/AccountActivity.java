package com.example.task61foodrescueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

public class AccountActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    int user_ID;
    Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Intent intent = getIntent();
        user_ID = intent.getIntExtra("USER_ID",0);
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
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
                Intent intent = new Intent(AccountActivity.this,HomeActivity.class);
                intent.putExtra("USER_ID", user_ID);
                startActivity(intent);
                return true;

            case R.id.account_id:
                Intent intent1 = new Intent(AccountActivity.this,AccountActivity.class);
                intent1.putExtra("USER_ID", user_ID);
                startActivity(intent1);
                return true;

            case R.id.my_list_id:
                Intent intent2 = new Intent(AccountActivity.this, MyList.class);
                intent2.putExtra("USER_ID", user_ID);
                startActivity(intent2);
                return true;
            case R.id.cart_id:
                Intent intent3 = new Intent(AccountActivity.this, CartActivity.class);
                intent3.putExtra("USER_ID", user_ID);
                startActivity(intent3);
                return true;
            default:
                return false;
        }
    }
}