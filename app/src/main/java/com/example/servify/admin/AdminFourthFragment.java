package com.example.servify.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servify.R;
import com.example.servify.UserAction;
import com.example.servify.UserActionAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminFourthFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserActionAdapter adapter;
    private List<UserAction> userActionList;

    private Button deleteActivity;
    private DatabaseReference actionRef;

    public AdminFourthFragment() {
        // Required empty public constructor
    }

    public static AdminFourthFragment newInstance() {
        return new AdminFourthFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_fragment_fourth, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        deleteActivity = view.findViewById(R.id.deleteActivity);

        // Initialize UserAction list
        userActionList = new ArrayList<>();

        // Sets the layout manager - Arranges the items in a vertical list
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserActionAdapter(userActionList); // Pass your data list here
        recyclerView.setAdapter(adapter);

        // Retrieve data from Firebase & Set up the initial RecyclerView
        loadAction();

        deleteActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllActivity();
            }
        });

        return view;
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

    private void deleteAllActivity() {
        DatabaseReference actionRef = FirebaseDatabase.getInstance().getReference("servify/userActions");
        actionRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    // Deletion successful
                    userActionList.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "All activity deleted successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle deletion error
                }
            }
        });
    }
}
