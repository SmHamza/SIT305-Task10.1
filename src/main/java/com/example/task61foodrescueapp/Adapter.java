package com.example.task61foodrescueapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task61foodrescueapp.model.Food;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<com.example.task61foodrescueapp.Adapter.MyViewHolder> {

    Context context;
    Activity activity;
    List<Food> foodList;

    public Adapter(Context context, Activity activity, List<Food> foodList) {
        this.context = context;
        this.activity = activity;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public com.example.task61foodrescueapp.Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.task61foodrescueapp.Adapter.MyViewHolder holder, int position) {
        holder.image.setImageBitmap(foodList.get(position).getImage());
        holder.title.setText(foodList.get(position).getFood_title());
        holder.description.setText(foodList.get(position).getFood_description());
        holder.date.setText(foodList.get(position).getDate_added());
        holder.time.setText(foodList.get(position).getPick_up_times());
        holder.quantity.setText(foodList.get(position).getQuantity());
        holder.location.setText(foodList.get(position).getLocation());

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = foodList.get(position).getFood_title();
                String description = foodList.get(position).getFood_description();
                String quantity = foodList.get(position).getQuantity();
                String time = foodList.get(position).getPick_up_times();
                String location = foodList.get(position).getLocation();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"Food Title: " + title + " \nFood Description: " +
                        description + "\nQuantity: " + quantity + "\nPick up time: " + time +
                        "\nLocation: " + location);
                v.getContext().startActivity(Intent.createChooser(intent,"Share via"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,description,date,time,quantity,location;
        ImageView image;
        RelativeLayout layout;
        ImageButton shareButton;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            shareButton = (ImageButton) itemView.findViewById(R.id.shareButton);
            image = itemView.findViewById(R.id.cart_image);
            title = itemView.findViewById(R.id.cart_title);
            description = itemView.findViewById(R.id.description);
            time = itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.date);
            quantity = itemView.findViewById(R.id.cart_quantity);
            location = itemView.findViewById(R.id.location);
            layout = itemView.findViewById(R.id.food_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), FoodDetails.class);
                    intent.putExtra("ID", foodList.get(getAdapterPosition()).getId());
                    intent.putExtra("USER_ID", foodList.get(getAdapterPosition()).getUser_id());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
