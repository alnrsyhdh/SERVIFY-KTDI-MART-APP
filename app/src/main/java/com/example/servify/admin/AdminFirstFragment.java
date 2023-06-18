package com.example.servify.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servify.R;
import com.example.servify.UserAction;
import com.example.servify.UserActionAdapter;
import com.example.servify.customer.ProductAdapter;
import com.example.servify.customer.Wish;
import com.example.servify.customer.WishAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminFirstFragment extends Fragment {
    private Spinner statusDropdown;
    private RecyclerView recyclerView, recyclerView2, recyclerView6;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Wish> wishList;

    private List<UserAction> userActionList;
    private UserActionAdapter adapter;
    private DatabaseReference actionRef;

    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("servify/status/shop_status");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View admin_firstfragment = inflater.inflate(R.layout.admin_fragment_first, container, false);
        recyclerView = admin_firstfragment.findViewById(R.id.recyclerView);

        // Set up the layout manager for the RecyclerView
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        // Initialize the productList
        productList = new ArrayList<>();

        // Set up the adapter with the productList
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        // Retrieve data from Firebase and set up the initial RecyclerView (product)
        loadProducts();

        recyclerView2 = admin_firstfragment.findViewById(R.id.recyclerView2);

        // Initialize the wishList
        wishList = new ArrayList<>();

        // Set up the adapter with the wishList
        WishAdapter wishAdapter = new WishAdapter(wishList);
        recyclerView2.setAdapter(wishAdapter);

        // Set up the layout manager for the RecyclerView
        LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(horizontalLayoutManager2);

        // Retrieve data from Firebase and set up the initial RecyclerView (wish)
        loadProducts2();
        //TODAY'S
        recyclerView6 = admin_firstfragment.findViewById(R.id.recyclerView6);

        // Initialize UserAction list
        userActionList = new ArrayList<>();

        // Sets the layout manager - Arranges the items in a vertical list
        recyclerView6.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserActionAdapter(userActionList); // Pass your data list here
        recyclerView6.setAdapter(adapter);

        // Retrieve data from Firebase & Set up the initial RecyclerView
        loadAction();

        return admin_firstfragment;
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
    private void loadProducts2() {
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
                recyclerView2.setAdapter(wishAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if retrieval is canceled
            }
        });
    }

    private void loadProducts() {
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
                recyclerView.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if retrieval is canceled
            }
        });
    }

    public void onViewCreated(View admin_firstfragment, Bundle savedInstanceState) {
        super.onViewCreated(admin_firstfragment, savedInstanceState);
        final View v = admin_firstfragment;
        statusDropdown = v.findViewById(R.id.statusDropdown);

        //display status in spinner from database
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String selectedItem = dataSnapshot.getValue(String.class);
                    // Populate the spinner with the selected item
                    populateSpinner(selectedItem);
                }//no need to create else since if its null it will be auto filled
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case
            }
            //setting adapter for displaying
            private void populateSpinner(String selectedItem) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.status_options, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                statusDropdown.setAdapter(adapter);

                // Set the selected item in the Spinner
                int position = adapter.getPosition(selectedItem);
                statusDropdown.setSelection(position);
            }
        });

        //updating selected item into database
        statusDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String newSelect = parent.getItemAtPosition(pos).toString();
                insertItemIntoFirebase(newSelect);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
            //function to insert data into database
            private void insertItemIntoFirebase(String item) {
                databaseRef.setValue(item)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Item inserted successfully into Firebase
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle the failure case
                            }
                        });
            }
        });
    }
}