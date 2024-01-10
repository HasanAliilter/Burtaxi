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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Fragment tempFragment;
    public FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        String kullanıcı_id = user.getUid();

        FirebaseDatabase.getInstance().getReference("Users").child(kullanıcı_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HashMap<String, Object> veri = (HashMap<String, Object>) dataSnapshot.getValue();
                    String kullaniciAdi = (String) veri.get("kaydol_username");
                    View viewYazi = navigationView.getHeaderView(0);
                    TextView newUser = (TextView) viewYazi.findViewById(R.id.textViewMainKullaniciAdi);
                    newUser.setText("Merhaba : " + kullaniciAdi);

                } else {
                    Log.v("TAG", "Veri bulunamadı");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Hata durumunda burada işlemler yapabilirsiniz
            }
        });


        //idler
        drawer =  (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        toolbar =  (Toolbar) findViewById(R.id.toolbar);

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
        if (item.getItemId() == R.id.nav_dorduncu) {
            tempFragment = new FragmentDorduncu();
        }
        if (item.getItemId() == R.id.nav_besinci) {
            tempFragment = new Fragmentbesinci();
        }
        if (item.getItemId() == R.id.nav_logout) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, GirisActivity.class));
            finish();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_tutucu, tempFragment).commit();

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}