package com.example.servify.customer;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.servify.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View first_view = inflater.inflate(R.layout.fragment_first, container, false);

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("servify/status/shop_status");
        TextView statusTv = first_view.findViewById(R.id.statusTv);
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
}