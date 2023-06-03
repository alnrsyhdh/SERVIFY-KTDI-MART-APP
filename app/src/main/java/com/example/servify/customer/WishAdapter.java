package com.example.servify.customer;

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

public class WishAdapter extends RecyclerView.Adapter<WishAdapter.WishViewHolder> {

    private List<Wish> wishList;
    private Context context;

    public WishAdapter(List<Wish> wishList) {
        this.wishList = wishList;
    }

    @NonNull
    @Override
    public WishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.card_view_wish, parent, false);
        return new WishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishViewHolder holder, int position) {
        Wish wish = wishList.get(position);
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

    public class WishViewHolder extends RecyclerView.ViewHolder {
        ImageView wishPic;
        TextView wishNameTextView, wishDetailsTextView;

        public WishViewHolder(@NonNull View itemView) {
            super(itemView);
            wishPic = itemView.findViewById(R.id.wish_pic);
            wishNameTextView = itemView.findViewById(R.id.wish_name);
            wishDetailsTextView = itemView.findViewById(R.id.wish_details);
        }
    }
}
