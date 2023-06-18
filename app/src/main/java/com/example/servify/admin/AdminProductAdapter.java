package com.example.servify.admin;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servify.R;
import com.example.servify.UserAction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> {

    private List<Product> productList;
    private UserActionListener userActionListener;
    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("servify/products");
    DatabaseReference userActionsRef = FirebaseDatabase.getInstance().getReference("servify/userActions");
    public AdminProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_card_view_item, parent, false);
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

    public interface UserActionListener {
        void onUserAction();
    }

    public void setUserActionListener(UserActionListener listener) {
        this.userActionListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewProductName;
        private TextView textViewProductPrice;
        private ImageView productPic, updateBtn, deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productPic = itemView.findViewById(R.id.admin_productPic);
            textViewProductName = itemView.findViewById(R.id.admin_productNameTextView);
            textViewProductPrice = itemView.findViewById(R.id.admin_productPriceTextView);
            updateBtn = itemView.findViewById(R.id.updateBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

            // Set an OnClickListener for the updateButton
            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Product product = productList.get(position);

                        // Perform the update operation using the productId
                        showUpdateDialog(product, itemView);
                    }
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Product product = productList.get(position);

                        // Perform the delete operation using the productId
                        deleteProduct(product);
                    }
                }
            });
        }

        private void deleteProduct(Product product) {
            // Retrieve the product ID from the selected product
            int productId = product.getProductId();

            // Create a reference to the specific product in the database
            DatabaseReference productRef = productsRef.child(String.valueOf(productId));

            // Remove the product from the database
            productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(itemView.getContext(), "Product deleted successfully.", Toast.LENGTH_SHORT).show();
                        // Notify the listener
                        if (userActionListener != null) {
                            userActionListener.onUserAction();
                        }

                        insertUserAction("Admin", "deleted a product!");
                    } else {
                        Toast.makeText(itemView.getContext(), "Failed to delete product.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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

    private void showUpdateDialog(Product product, View itemView) {
        // Inflate the custom layout xml
        View updateDialogView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.admin_updateproduct_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        builder.setView(updateDialogView);

        final EditText updateProductName = updateDialogView.findViewById(R.id.update_productName);
        final EditText updateProductPrice = updateDialogView.findViewById(R.id.update_productPrice);

        updateProductName.setText(product.getProductName());
        updateProductPrice.setText(String.valueOf(product.getProductPrice()));

        builder.setPositiveButton("Update Product", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String updatedName = updateProductName.getText().toString();
                        String updatedPrice = updateProductPrice.getText().toString();

                        if (!TextUtils.isEmpty(updatedName) && !TextUtils.isEmpty(updatedPrice)) {
                            // Retrieve the product ID from the selected product
                            int productId = product.getProductId();

                            // Create a reference to the specific product in the database
                            DatabaseReference productRef = productsRef.child(String.valueOf(productId));

                            try {
                                // Convert the updatedPrice to double
                                double parsedPrice = Double.parseDouble(updatedPrice);

                                // Update the product details
                                productRef.child("productName").setValue(updatedName);
                                productRef.child("productPrice").setValue(parsedPrice);

                                // Notify the listener
                                if (userActionListener != null) {
                                    userActionListener.onUserAction();
                                }

                                insertUserAction("Admin", "updated a product!");

                                Toast.makeText(itemView.getContext(), "Product updated successfully.", Toast.LENGTH_SHORT).show();
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(itemView.getContext(), "Please enter valid values.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Method to insert the user action into the database
    private void insertUserAction(String user, String action) {
        long timestamp = System.currentTimeMillis();
        String userActionId = String.valueOf(timestamp);

        UserAction userAction = new UserAction(user, action);
        userActionsRef.child(userActionId).setValue(userAction)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User action inserted successfully
                    } else {
                        // Failed to insert the user action
                    }
                });

    }

}