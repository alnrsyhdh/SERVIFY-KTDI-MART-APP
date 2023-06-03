package com.example.servify;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servify.admin.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bindData(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewProductId;
        private TextView textViewProductName;
        private TextView textViewProductPrice;
        private ImageView productPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productPic = itemView.findViewById(R.id.productPic);
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

            textViewProductName.setText(product.getProductName());
            textViewProductPrice.setText(String.valueOf(product.getProductPrice()));
        }
    }
}

