package com.example.task61foodrescueapp;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task61foodrescueapp.model.CartItem;

import java.util.List;

public class Adapter_cart extends RecyclerView.Adapter<Adapter_cart.MyViewHolder> {

    Context context;
    Activity activity;
    List<CartItem> foodList;


    public Adapter_cart(Context context, Activity activity, List<CartItem> foodList) {
        this.context = context;
        this.activity = activity;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public Adapter_cart.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout_cart,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_cart.MyViewHolder holder, int position) {

        String price = String.valueOf(foodList.get(position).getPrice());
        holder.image.setImageBitmap(foodList.get(position).getImage());
        holder.title.setText(foodList.get(position).getFood_title());
        holder.quantity.setText(foodList.get(position).getQuantity());
        holder.price.setText("$" +price);

//        holder.shareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,quantity, price;
        ImageView image;
        ConstraintLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cart_image);
            title = itemView.findViewById(R.id.cart_title);
            price = itemView.findViewById(R.id.cart_price);
            quantity = itemView.findViewById(R.id.cart_quantity);
            layout = itemView.findViewById(R.id.cart_layout);
        }
    }
}
