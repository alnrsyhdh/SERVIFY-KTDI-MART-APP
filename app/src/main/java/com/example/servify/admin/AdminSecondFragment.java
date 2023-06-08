package com.example.servify.admin;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
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

import com.example.servify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AdminSecondFragment extends Fragment{

    Button addBtn;
    private ImageView productPic;

    private Uri imgPath;
    private static final int PICK_IMAGE_REQUEST =1;
    private StorageReference storageReference;

    private FirebaseAuth mAuth;
    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("servify/");

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
        productPic = dialogView.findViewById(R.id.productPic);
        productPic.setClickable(true);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        productPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("productPic/*");
                getImageInImageView();
                uploadImage();
            }
        });

        return secView;
    }


    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading Images...");
        progressDialog.show();

        FirebaseStorage.getInstance().getReference("productPic/" + UUID.randomUUID().toString()).putFile(imgPath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                String profilePictureUrl = task.getResult().toString();}
                        }
                    });
                    Toast.makeText(getContext(), "Image Uploaded!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Upload Failed. Please try again...", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                reloadFragment();
            }
        });
    }

    private void reloadFragment() {
        FragmentTransaction ft = requireFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            ContentResolver contentResolver = requireActivity().getContentResolver();
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imgPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        productPic.setImageBitmap(bitmap);
    }

    public void showDialog(){

        //inflate the custom layout xml
        View dialogView = getLayoutInflater().inflate(R.layout.admin_addproduct_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder (getActivity());
        builder.setView(dialogView);

                builder.setPositiveButton("Add Product", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //int productId;
                        // Generate a unique identifier for the productId
                        long timestamp = System.currentTimeMillis();
                        int productId = (int) timestamp; // Convert the timestamp to an integer if necessary

                        EditText productName = dialogView.findViewById(R.id.productName);
                        EditText productPrice = dialogView.findViewById(R.id.productPrice);

                        String name = productName.getText().toString();
                        String price = productPrice.getText().toString();

                        Product product = new Product(productId, name, Double.parseDouble(price), "");

                        productsRef.child("products").child(String.valueOf(productId)).setValue(product);

                        // Display a toast message for successful input
                        Toast.makeText(getActivity(), "Input added successfully.", Toast.LENGTH_SHORT).show();

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
}