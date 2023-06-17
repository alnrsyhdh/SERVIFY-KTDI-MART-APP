package com.example.servify;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.example.servify.admin.AdminFifthFragment;
import com.example.servify.admin.AdminFirstFragment;
import com.example.servify.admin.AdminFourthFragment;
import com.example.servify.admin.AdminSecondFragment;
import com.example.servify.admin.AdminThirdFragment;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener , UserActionListener {

    BottomNavigationView adminBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_main);

        adminBottomNavigationView
                = findViewById(R.id.adminBottomNavigationView);

        adminBottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        adminBottomNavigationView.setSelectedItemId(R.id.admin_home);

    }
    com.example.servify.admin.AdminFirstFragment AdminFirstFragment = new AdminFirstFragment();
    com.example.servify.admin.AdminSecondFragment AdminSecondFragment = new AdminSecondFragment();
    com.example.servify.admin.AdminThirdFragment AdminThirdFragment = new AdminThirdFragment();
    com.example.servify.admin.AdminFourthFragment AdminFourthFragment = new AdminFourthFragment();
    com.example.servify.admin.AdminFifthFragment AdminFifthFragment = new AdminFifthFragment();

    //Bottom navigation linking
    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {

        int itemID = item.getItemId();
        if (itemID == R.id.admin_home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.adminflFragment, AdminFirstFragment)
                    .commit();
            return true;
        } else if (itemID == R.id.admin_product) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.adminflFragment, AdminSecondFragment)
                    .commit();
            return true;
        } else if (itemID == R.id.admin_wishlist) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.adminflFragment, AdminThirdFragment)
                    .commit();
            return true;
        } else if (itemID == R.id.admin_activity) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.adminflFragment, AdminFourthFragment)
                    .commit();
            return true;
        } else if (itemID == R.id.admin_account) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.adminflFragment, AdminFifthFragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onUserAction() {

    }
}
