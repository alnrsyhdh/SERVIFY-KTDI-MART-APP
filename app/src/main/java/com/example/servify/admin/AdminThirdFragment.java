package com.example.servify.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servify.R;
import com.example.servify.admin.AdminWish;
import com.example.servify.admin.AdminWishAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminThirdFragment extends Fragment implements AdminWishAdapter.OnDeleteClickListener {

    private RecyclerView recyclerView;
    private AdminWishAdapter wishAdapter;
    private List<AdminWish> wishList;
    private DatabaseReference wishRef;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AdminThirdFragment() {
        // Required empty public constructor
    }

    public static AdminThirdFragment newInstance(String param1, String param2) {
        AdminThirdFragment fragment = new AdminThirdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thirdFrag = inflater.inflate(R.layout.admin_fragment_third, container, false);

        recyclerView = thirdFrag.findViewById(R.id.adminrecyclerViewWish);

        wishList = new ArrayList<>();
        wishAdapter = new AdminWishAdapter(wishList);
        recyclerView.setAdapter(wishAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        wishRef = FirebaseDatabase.getInstance().getReference("servify/wishForm");

        wishAdapter.setOnDeleteClickListener(this);

        wishRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wishList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int wishId = snapshot.child("id").getValue(Integer.class);
                    String wishPic = snapshot.child("imageUrl").getValue(String.class);
                    String wishName = snapshot.child("pName").getValue(String.class);
                    String wishDetails = snapshot.child("wish").getValue(String.class);

                    AdminWish wish = new AdminWish(wishId, wishName, wishDetails, wishPic);
                    wishList.add(wish);
                }
                wishAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if retrieval is canceled
            }
        });

        return thirdFrag;
    }

    @Override
    public void onDeleteClick(int position) {
        AdminWish wish = wishList.get(position);
        int wishId = wish.getWishId();
        wishList.remove(position);
        wishAdapter.notifyItemRemoved(position);
        removeWishFromFirebase(wishId);
    }

    private void removeWishFromFirebase(int wishId) {
        DatabaseReference wishToDeleteRef = wishRef.child(String.valueOf(wishId));
        wishToDeleteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Wish deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to delete wish", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
