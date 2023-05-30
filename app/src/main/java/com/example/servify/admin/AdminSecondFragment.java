package com.example.servify.admin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.servify.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminSecondFragment extends Fragment{

    Button addBtn;

    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("servify/products");

    public AdminSecondFragment() {
        // Required empty public constructor
    }


    public static AdminSecondFragment newInstance(String param1, String param2) {
        AdminSecondFragment fragment = new AdminSecondFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase
        FirebaseApp.initializeApp(getActivity());
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View secView = inflater.inflate(R.layout.admin_fragment_second, container, false);

        //create function
        addBtn = secView.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog();
            }
        });
        return secView;
    }
    public void showDialog(){

        //inflate the custom layout xml
        View dialogView = getLayoutInflater().inflate(R.layout.admin_addproduct_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder (getActivity());
        builder.setView(dialogView)
                .setTitle("Add Product Details");

                builder.setPositiveButton("Add Product", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //int productId;
                        // Generate a unique identifier for the productId
                        long timestamp = System.currentTimeMillis();
                        int productId = (int) timestamp; // Convert the timestamp to an integer if necessary

                        EditText productName = dialogView.findViewById(R.id.productName);
                        EditText productPrice = dialogView.findViewById(R.id.productPrice);

                        //Button addBtn = dialogView.findViewById(R.id.addBtn);

                        //Product product = new Product(productName, productPrice);
                        //productsRef.child(String.valueOf(productId)).setValue(product);
                        //productsRef.child("products").child(productId).setValue(new Product(productId, productName, productPrice));

                        String name = productName.getText().toString();
                        String price = productPrice.getText().toString();

                        Product product = new Product(productId, name, Double.parseDouble(price));
                        productsRef.child("products").child(String.valueOf(productId)).setValue(product);


                        // Display a toast message for successful input
                        Toast.makeText(getActivity(), "Input added successfully.", Toast.LENGTH_SHORT).show();

                    }

                })
                .setNegativeButton("Change Image", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}