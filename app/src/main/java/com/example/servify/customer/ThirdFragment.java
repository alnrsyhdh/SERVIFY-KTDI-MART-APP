package com.example.servify.customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servify.R;
import com.example.servify.customer.Wish;
import com.example.servify.customer.WishAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ThirdFragment extends Fragment {
    private Button button_wishForm;

    private RecyclerView recyclerView;
    private WishAdapter wishAdapter;
    private List<Wish> wishList;
    private DatabaseReference wishRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View thirdFrag = inflater.inflate(R.layout.fragment_third, container, false);

        recyclerView = thirdFrag.findViewById(R.id.recyclerViewWish);
        button_wishForm = thirdFrag.findViewById(R.id.button_wishForm);

        // Initialize the wishList
        wishList = new ArrayList<>();

        // Set up the adapter with the wishList
        wishAdapter = new WishAdapter(wishList);
        recyclerView.setAdapter(wishAdapter);

        // Set up the layout manager for the RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        // Set up the Firebase database reference
        wishRef = FirebaseDatabase.getInstance().getReference("servify/wishForm");

        button_wishForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToFormFragment();
            }
        });

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
                wishAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if retrieval is canceled
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
