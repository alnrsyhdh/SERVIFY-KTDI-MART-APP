package com.example.servify.admin;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servify.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminSecondFragment extends Fragment{

    Button addBtn, uploadPic;
    private ImageView addProductPic;
    private List<Product> productList;
    private RecyclerView recyclerView;
    private AdminProductAdapter productAdapter;
    private Uri imgPath;
    private static final int PICK_IMAGE_REQUEST =1;
    private StorageReference storageReference;

    private FirebaseAuth mAuth;
    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("servify/");
    DatabaseReference displayRef = FirebaseDatabase.getInstance().getReference("servify/products");
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("productPic");

    public AdminSecondFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase
        FirebaseApp.initializeApp(getActivity());
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View secView = inflater.inflate(R.layout.admin_fragment_second, container, false);
        View dialogView = getLayoutInflater().inflate(R.layout.admin_addproduct_dialog, null);

        //create function
        addBtn = secView.findViewById(R.id.addBtn);

        //productPic.setClickable(true);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });



        recyclerView = secView.findViewById(R.id.admin_recyclerView);
        // Set up the layout manager for the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // Initialize the productList
        productList = new ArrayList<>();
        // Set up the adapter with the productList
        productAdapter = new AdminProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);
        // Retrieve data from Firebase
        displayRef.addValueEventListener(new ValueEventListener() {
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

        return secView;
    }

    private void reloadFragment() {
        FragmentTransaction ft = requireFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    //upload picture in a dialog
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgPath = data.getData();
            getImageInImageView();
        }
    }
    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            ContentResolver contentResolver = requireActivity().getContentResolver();
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imgPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addProductPic.setImageBitmap(bitmap);
    }

    public void showDialog(){

        //inflate the custom layout xml
        View dialogView = getLayoutInflater().inflate(R.layout.admin_addproduct_dialog, null);
        addProductPic = dialogView.findViewById(R.id.addProductPic);
        uploadPic = dialogView.findViewById(R.id.uploadPic);

        AlertDialog.Builder builder = new AlertDialog.Builder (getActivity());
        builder.setView(dialogView);

                builder.setPositiveButton("Add Product", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Generate a unique identifier for the productId
                        long timestamp = System.currentTimeMillis();
                        int productId = (int) timestamp; // Convert the timestamp to an integer if necessary

                        EditText productName = dialogView.findViewById(R.id.productName);
                        EditText productPrice = dialogView.findViewById(R.id.productPrice);

                        String name = productName.getText().toString();
                        String price = productPrice.getText().toString();



                        if (imgPath != null) {
                            StorageReference imageRef = storageRef.child(String.valueOf(productId));
                            UploadTask uploadTask = imageRef.putFile(imgPath);

                            Picasso.get()
                                    .load(String.valueOf(imageRef))
                                    .into(addProductPic);

                            uploadTask.continueWithTask(task -> {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                // Continue with the task to get the download URL
                                return imageRef.getDownloadUrl();
                            }).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String imageUrl = downloadUri.toString();

                                    Product product = new Product(productId, name, Double.parseDouble(price), imageUrl);
                                    productsRef.child("products").child(String.valueOf(productId)).setValue(product)
                                            .addOnCompleteListener(formTask -> {
                                                if (formTask.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Your product is successfully added!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });

                        }else {
                            Product product = new Product(productId, name, Double.parseDouble(price), "");
                            productsRef.child("products").child(String.valueOf(productId)).setValue(product)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Your product is successfully added!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

    }

}