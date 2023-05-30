package com.example.servify.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.servify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminFirstFragment extends Fragment {
    private Spinner statusDropdown;

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
        return admin_firstfragment;
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