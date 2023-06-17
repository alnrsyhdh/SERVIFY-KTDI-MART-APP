package com.example.servify.customer;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servify.R;
import com.example.servify.admin.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;
    private List<Product> filteredList;

    public ProductAdapter(List<Product> productList) {
        this.productList = new ArrayList<>(productList);
        this.filteredList = new ArrayList<>(productList);
    }

    public void setFilteredList(List<Product> filteredList) {
        this.filteredList = new ArrayList<>(filteredList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = filteredList.get(position); // Use filteredList instead of productList
        holder.bindData(product);
    }

    @Override
    public int getItemCount() {
        return filteredList.size(); // Use filteredList instead of productList
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewProductName;
        private TextView textViewProductPrice;
        private ImageView productPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productPic = itemView.findViewById(R.id.addProductPic);
            textViewProductName = itemView.findViewById(R.id.productNameTextView);
            textViewProductPrice = itemView.findViewById(R.id.productPriceTextView);
        }

        public void bindData(Product product) {
            if (TextUtils.isEmpty(product.getProductPic())) {
                // Set the default picture in the ImageView
                productPic.setImageResource(R.drawable.default_pic);
            } else {
                Picasso.get().load(product.getProductPic()).into(productPic);
            }

            // Format the price with two decimal places
            String formattedPrice = String.format("%.2f", product.getProductPrice());

            textViewProductName.setText(product.getProductName());
            textViewProductPrice.setText(formattedPrice);
        }
    }
}

