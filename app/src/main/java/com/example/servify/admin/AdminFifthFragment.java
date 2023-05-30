package com.example.servify.admin;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.servify.MainActivity;
import com.example.servify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class AdminFifthFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView profile_pic;
    private TextView admin_email, admin_role;
    private EditText admin_password, admin_role2;
    private Uri imagePath;
    private FirebaseAuth mAuth;
    private Button buttonLogout, buttonUpdate;

    public static AdminFifthFragment newInstance(String param1, String param2) {
        AdminFifthFragment fragment = new AdminFifthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AdminFifthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_fragment_fifth, container, false);

        buttonUpdate = rootView.findViewById(R.id.update_button);
        buttonLogout = rootView.findViewById(R.id.buttonLogout);
        mAuth = FirebaseAuth.getInstance();
        profile_pic = rootView.findViewById(R.id.profile_pic);
        admin_email = rootView.findViewById(R.id.admin_email);
        admin_role = rootView.findViewById(R.id.admin_role);
        admin_role2 = rootView.findViewById(R.id.admin_role2);
        admin_password = rootView.findViewById(R.id.admin_password);

        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("servify/admin").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String password = dataSnapshot.child("password").getValue(String.class);
                    String profilePic = dataSnapshot.child("profilePic").getValue(String.class);
                    String role = dataSnapshot.child("role").getValue(String.class);

                    // Check if the profilePic value is null or empty
                    if (TextUtils.isEmpty(profilePic)) {
                        // Set the default picture in the ImageView
                        profile_pic.setImageResource(R.drawable.profile_pic);
                    } else {
                        // Load the profile picture using Picasso
                        Picasso.get().load(profilePic).into(profile_pic);
                    }

                    admin_email.setText(email);
                    admin_password.setText(password);
                    admin_role.setText(role);
                    admin_role2.setText(role);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = admin_password.getText().toString();
                String newRole = admin_role2.getText().toString();

                if (!TextUtils.isEmpty(newPassword)) {
                    // Check if the new password is longer than 8 characters
                    if (newPassword.length() > 8) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser != null) {
                            currentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference("servify/admin/" + mAuth.getCurrentUser().getUid() + "/password")
                                                .setValue(newPassword)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                                            reloadFragment();
                                                        } else {
                                                            Toast.makeText(getContext(), "Password update failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getContext(), "Password update failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(getContext(), "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    }
                }

                if (!TextUtils.isEmpty(newRole)) {
                    String userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("servify/admin").child(userId);

                    userRef.child("role").setValue(newRole).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Role updated successfully", Toast.LENGTH_SHORT).show();
                                reloadFragment();
                            } else {
                                Toast.makeText(getContext(), "Role update failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent, 1);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imagePath = data.getData();
            getImageInImageView();
            uploadImage();
        }
    }

    private void reloadFragment() {
        FragmentTransaction ft = requireFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading Images...");
        progressDialog.show();

        FirebaseStorage.getInstance().getReference("images/" + UUID.randomUUID().toString()).putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                updateProfilePicture(task.getResult().toString());
                            }
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

    private void updateProfilePicture(String url) {
        FirebaseDatabase.getInstance().getReference("servify/admin/" + mAuth.getCurrentUser().getUid() + "/profilePic").setValue(url);
    }

    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            ContentResolver contentResolver = requireActivity().getContentResolver();
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        profile_pic.setImageBitmap(bitmap);
    }
}
