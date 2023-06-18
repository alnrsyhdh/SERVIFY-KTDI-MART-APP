package com.example.servify.customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servify.R;
import com.example.servify.UserAction;
import com.example.servify.UserActionAdapter;
import com.example.servify.admin.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FirstFragment extends Fragment {

    private RecyclerView recyclerView3, recyclerView4, recyclerView5;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Wish> wishList;
    private List<UserAction> userActionList;
    private UserActionAdapter adapter;
    private DatabaseReference actionRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View first_view = inflater.inflate(R.layout.fragment_first, container, false);

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("servify/status/shop_status");
        TextView statusTv = first_view.findViewById(R.id.statusTv);

        recyclerView3 = first_view.findViewById(R.id.recyclerView3);

        // Set up the layout manager for the RecyclerView
        LinearLayoutManager horizontalLayoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView3.setLayoutManager(horizontalLayoutManager3);

        // Initialize the productList
        productList = new ArrayList<>();

        // Set up the adapter with the productList
        productAdapter = new ProductAdapter(productList);
        recyclerView3.setAdapter(productAdapter);

        // Retrieve data from Firebase and set up the initial RecyclerView (product)
        loadProducts3();

        recyclerView4 = first_view.findViewById(R.id.recyclerView4);

        // Initialize the wishList
        wishList = new ArrayList<>();

        // Set up the adapter with the wishList
        WishAdapter wishAdapter = new WishAdapter(wishList);
        recyclerView4.setAdapter(wishAdapter);

        // Set up the layout manager for the RecyclerView
        LinearLayoutManager horizontalLayoutManager4 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView4.setLayoutManager(horizontalLayoutManager4);

        // Retrieve data from Firebase and set up the initial RecyclerView (wish)
        loadProducts4();

        //TODAY'S
        recyclerView5 = first_view.findViewById(R.id.recyclerView5);

        // Initialize UserAction list
        userActionList = new ArrayList<>();

        // Sets the layout manager - Arranges the items in a vertical list
        recyclerView5.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserActionAdapter(userActionList); // Pass your data list here
        recyclerView5.setAdapter(adapter);

        // Retrieve data from Firebase & Set up the initial RecyclerView
        loadAction();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String item = dataSnapshot.getValue(String.class);
                    statusTv.setText(item);
                    changeBackgroundColor(item, statusTv);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case
            }

            private void changeBackgroundColor(String item, TextView textView) {
                switch (item) {
                    case "OPEN":
                        textView.setBackgroundResource(R.drawable.bg_open);
                        break;
                    case "CLOSE":
                        textView.setBackgroundResource(R.drawable.bg_close);
                        break;
                    case "ON BREAK":
                        textView.setBackgroundResource(R.drawable.bg_break);
                        break;
                }
            }
        });

        return first_view;
    }

    private void loadAction() {
            actionRef = FirebaseDatabase.getInstance().getReference("servify/userActions");
            actionRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userActionList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String user = snapshot.child("user").getValue(String.class);
                        String action = snapshot.child("action").getValue(String.class);

                        UserAction actionDisplay = new UserAction(user, action);
                        userActionList.add(actionDisplay);
                    }

                    // Reverse the order of the list
                    Collections.reverse(userActionList);

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                }
            });
    }

    private void loadProducts3() {//products
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("servify/products");
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
                recyclerView3.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if retrieval is canceled
            }
        });
    }

    private void loadProducts4() {//wish
        DatabaseReference wishRef = FirebaseDatabase.getInstance().getReference("servify/wishForm");
        wishRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wishList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int wishId = snapshot.child("id").getValue(Integer.class);
                    String wishPic = snapshot.child("imageUrl").getValue(String.class);
                    String wishName = snapshot.child("uName").getValue(String.class);
                    String wishDetails = snapshot.child("pName").getValue(String.class);

                    Wish wish = new Wish(wishId, wishName, wishDetails, wishPic);
                    wishList.add(wish);
                }

                // Initialize the adapter with the updated productList
                WishAdapter wishAdapter = new WishAdapter(wishList);
                recyclerView4.setAdapter(wishAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if retrieval is canceled
            }
        });
    }

}