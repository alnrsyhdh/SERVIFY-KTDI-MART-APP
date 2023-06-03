package com.example.servify.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servify.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminWishAdapter extends RecyclerView.Adapter<AdminWishAdapter.AdminWishViewHolder> {

    private List<AdminWish> wishList;
    private Context context;

    public AdminWishAdapter(List<AdminWish> wishList) {
        this.wishList = wishList;
    }

    @NonNull
    @Override
    public AdminWishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.admin_card_view_wish, parent, false);
        return new AdminWishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminWishViewHolder holder, int position) {
        AdminWish wish = wishList.get(position);
        holder.wishNameTextView.setText(wish.getWishName());
        holder.wishDetailsTextView.setText(wish.getWishDetails());
        Picasso.get()
                .load(wish.getWishPic())
                .placeholder(R.drawable.default_pic) // Placeholder image resource
                .error(R.drawable.default_pic) // Error image resource
                .into(holder.wishPic);
    }

    @Override
    public int getItemCount() {
        return wishList.size();
    }

    public class AdminWishViewHolder extends RecyclerView.ViewHolder {
        ImageView wishPic;
        TextView wishNameTextView, wishDetailsTextView;

        public AdminWishViewHolder(@NonNull View itemView) {
            super(itemView);
            wishPic = itemView.findViewById(R.id.admin_wish_pic);
            wishNameTextView = itemView.findViewById(R.id.admin_wish_product);
            wishDetailsTextView = itemView.findViewById(R.id.admin_wish_details);
        }
    }
}

