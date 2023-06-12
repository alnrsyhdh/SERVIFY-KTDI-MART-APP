package com.example.servify.customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.servify.R;
import com.example.servify.admin.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    ImageView product_pic;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("servify/products");

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View secondView = inflater.inflate(R.layout.fragment_second, container, false);

        recyclerView = secondView.findViewById(R.id.recyclerView);

        // Set up the layout manager for the RecyclerView
        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize the productList
        productList = new ArrayList<>();

        // Set up the adapter with the productList
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        // Retrieve data from Firebase
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int productId = snapshot.child("productId").getValue(Integer.class);
                    String productName = snapshot.child("productName").getValue(String.class);
                    String productPic = snapshot.child("productPic").getValue(String.class);
                    double productPrice = snapshot.child("productPrice").getValue(Double.class);

                    Product productDisplay = new Product(productId, productName, productPrice, productPic);
                    productList.add(productDisplay);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if retrieval is canceled
            }
        });

        return secondView;
    }
}