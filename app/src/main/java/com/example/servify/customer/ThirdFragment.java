package com.example.servify.customer;

import static android.app.Activity.RESULT_OK;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.servify.FormData;
import com.example.servify.MainActivity;
import com.example.servify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ThirdFragment extends Fragment {
    Button button_wishForm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View thirdFrag = inflater.inflate(R.layout.fragment_third, container, false);

        button_wishForm = thirdFrag.findViewById(R.id.button_wishForm);
        button_wishForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToFormFragment();
            }
        });

        return thirdFrag;
    }

    private void sendUserToFormFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ThirdFragmentForm thirdFragmentForm = new ThirdFragmentForm();
        fragmentTransaction.replace(R.id.flFragment, thirdFragmentForm);
        fragmentTransaction.addToBackStack(null); // Optional: Add the transaction to the back stack, so the user can navigate back

        fragmentTransaction.commit();
    }
}
