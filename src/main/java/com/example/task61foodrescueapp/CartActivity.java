package com.example.task61foodrescueapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task61foodrescueapp.Config.Config;
import com.example.task61foodrescueapp.data.DatabaseHelper;
import com.example.task61foodrescueapp.model.CartItem;
import com.example.task61foodrescueapp.model.Food;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView priceTextView;
    Button payButton;
    Adapter_cart adapter1;
    List<CartItem> foodList;
    DatabaseHelper db;
    String amount;
    int user_ID;
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);
    @Override
    protected  void onDestroy(){
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.cartRecyclerView);
        priceTextView = findViewById(R.id.totalPriceTextView);
        payButton = findViewById(R.id.payCartButton);
        foodList = new ArrayList<>();
        db = new DatabaseHelper(this);
        getAllCartItems();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter1 = new Adapter_cart(this, CartActivity.this,foodList);
        recyclerView.setAdapter(adapter1);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        Intent intent1 = getIntent();
        user_ID = intent1.getIntExtra("USER_ID",0);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }
    void getAllCartItems(){
        foodList = db.getAllCartItems();
        Toast.makeText(this, "Size of foodlist: " + foodList.size(), Toast.LENGTH_SHORT).show();
    }
    void processPayment(){
        amount = priceTextView.getText().toString().substring(8, priceTextView.getText().toString().length());
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD", "For food rescue", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class)
                        .putExtra("PaymenyDetails", paymentDetails)
                        .putExtra("PaymentAmount", amount));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}