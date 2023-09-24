package com.whatsend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    DBConfig config;

    BottomNavigationView bottomNavigationView;

    SendFragment sendFragment = new SendFragment();
    HistoryFragment historyFragment = new HistoryFragment();
    AboutFragment aboutFragment = new AboutFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        config = new DBConfig(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.send);

        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, sendFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getTitle().toString()) {
                case "Send":
                    replaceFragment(sendFragment);
                    break;
                case "History":
                    replaceFragment(historyFragment);
                    break;
                case "About":
                    replaceFragment(aboutFragment);
                    break;
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.commit();
    }

}