package com.example.servify.admin;

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
import com.example.servify.admin.AdminWish;
import com.example.servify.admin.AdminWishAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminThirdFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdminWishAdapter wishAdapter;
    private List<AdminWish> wishList;
    private DatabaseReference wishRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminThirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        View thirdFrag = inflater.inflate(R.layout.admin_fragment_third, container, false);

        recyclerView = thirdFrag.findViewById(R.id.adminrecyclerViewWish);


        // Initialize the wishList
        wishList = new ArrayList<>();

        // Set up the adapter with the wishList
        wishAdapter = new AdminWishAdapter(wishList);
        recyclerView.setAdapter(wishAdapter);

        // Set up the layout manager for the RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        // Set up the Firebase database reference
        wishRef = FirebaseDatabase.getInstance().getReference("servify/wishForm");


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
}