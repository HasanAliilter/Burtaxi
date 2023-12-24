package com.burta.burtaxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Fragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //idler
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        tempFragment = new FragmentBirinci();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_tutucu, tempFragment).commit();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, 0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.inflateHeaderView(R.layout.menu_baslik);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_birinci) {
            tempFragment = new FragmentBirinci();
        }
        if (item.getItemId() == R.id.nav_ikinci) {
            tempFragment = new FragmentIkinci();
        }
        if (item.getItemId() == R.id.nav_ucuncu) {
            tempFragment = new FragmentUcuncu();
        }
        if (item.getItemId() == R.id.nav_logout) {
            startActivity(new Intent(MainActivity.this, GirisActivity.class));
            finish();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_tutucu, tempFragment).commit();

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}