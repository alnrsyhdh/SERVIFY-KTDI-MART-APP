package com.example.servify.customer;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servify.R;
import com.example.servify.admin.Product;
import com.example.servify.customer.ProductAdapter;
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
    private DatabaseReference productsRef;
    private EditText searchEditText;

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View secondView = inflater.inflate(R.layout.fragment_second, container, false);

        recyclerView = secondView.findViewById(R.id.recyclerView);
        searchEditText = secondView.findViewById(R.id.searchEditText);

        // Set up the layout manager for the RecyclerView
        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize the productList
        productList = new ArrayList<>();

        // Set up the adapter with the productList
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        // Retrieve data from Firebase and set up the initial RecyclerView
        loadProducts();

        // Add a TextWatcher to dynamically filter the list as the user input
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchQuery = s.toString().trim();
                filterProducts(searchQuery); // Call the filterProducts method
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return secondView;
    }

    private void loadProducts() {
        productsRef = FirebaseDatabase.getInstance().getReference("servify/products");
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

                // Initialize the adapter with the updated productList
                productAdapter = new ProductAdapter(productList);
                recyclerView.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if retrieval is canceled
            }
        });
    }


    private void filterProducts(String searchQuery) {
        List<Product> filteredList = new ArrayList<>();

        for (Product product : productList) {
            if (product.getProductName().toLowerCase().contains(searchQuery.toLowerCase())) {
                filteredList.add(product);
            }
        }

        if (TextUtils.isEmpty(searchQuery)) {
            // If search query is empty, display all products
            productAdapter.setFilteredList(productList);
        } else {
            productAdapter.setFilteredList(filteredList);
        }
    }

}
