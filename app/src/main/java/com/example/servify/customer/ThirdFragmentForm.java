package com.example.servify.customer;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.servify.FormData;
import com.example.servify.R;
import com.example.servify.UserAction;
import com.example.servify.UserActionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ThirdFragmentForm extends Fragment implements UserActionListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imageView;

    private EditText editTextName;
    private EditText editTextProduct;
    private EditText editTextWish;

    private DatabaseReference wishRef = FirebaseDatabase.getInstance().getReference().child("servify").child("wishForm");
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images");
    DatabaseReference userActionsRef = FirebaseDatabase.getInstance().getReference("servify/userActions");

    public void saveFormData(View view) {
        long timestamp = System.currentTimeMillis();
        int id = (int) timestamp; // Convert the timestamp to an integer if necessary

        String uname = editTextName.getText().toString().trim();
        String pname = editTextProduct.getText().toString().trim();
        String wish = editTextWish.getText().toString().trim();

        // Notify the listener
        UserActionListener listener = (UserActionListener) getActivity();
        if (listener != null) {
            listener.onUserAction();
        }

        insertUserAction("User", "added new wishlist");

        if (imageUri != null) {
            StorageReference imageRef = storageRef.child(String.valueOf(id));
            UploadTask uploadTask = imageRef.putFile(imageUri);

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

                    FormData formData = new FormData(id, uname, pname, wish, imageUrl);
                    wishRef.child(String.valueOf(id)).setValue(formData)
                            .addOnCompleteListener(formTask -> {
                                if (formTask.isSuccessful()) {
                                    editTextName.setText("");
                                    editTextProduct.setText("");
                                    editTextWish.setText("");
                                    imageView.setImageResource(R.drawable.basket);

                                    Toast.makeText(getActivity(), "Your wish is successfully sent!", Toast.LENGTH_SHORT).show();

                                    //sendUserToNextFragment(); //go back to previous page

//                                    imageRef.delete().addOnCompleteListener(deleteTask -> {
//                                        if (deleteTask.isSuccessful()) {
//                                            // Image successfully deleted
//                                        } else {
//                                            // Failed to delete the image
//                                        }
//                                    });


                                    // Data saved successfully
                                } else {
                                    // Data saving failed
                                }
                            });
                } else {
                    // Image upload failed
                }
            });
        } else {
            FormData formData = new FormData(id, uname, pname, wish, "");
            wishRef.child(String.valueOf(id)).setValue(formData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            editTextName.setText("");
                            editTextProduct.setText("");
                            editTextWish.setText("");
                            imageView.setImageResource(R.drawable.basket);
                            Toast.makeText(getActivity(), "Your wish is successfully sent!", Toast.LENGTH_SHORT).show();

                            // Data saved successfully
                        } else {
                            // Data saving failed
                        }
                    });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third_form, container, false);

        editTextName = view.findViewById(R.id.wn);
        editTextProduct = view.findViewById(R.id.itn);
        editTextWish = view.findViewById(R.id.wd);
        imageView = view.findViewById(R.id.addcart);

        Button buttonSave = view.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(v -> saveFormData(v));

        Button buttonUpload = view.findViewById(R.id.addcartbtn);
        buttonUpload.setOnClickListener(v -> openImagePicker());

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendUserToNextFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ThirdFragment thirdFragment = new ThirdFragment();
        fragmentTransaction.replace(R.id.flFragment, thirdFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Toast.makeText(getActivity(), "Wishlist Added!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserAction() {

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