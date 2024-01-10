package com.burta.burtaxi;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap mMap;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SearchView mapSearchView;
    private TextView current_location;
    private TextView next_location;
    private String currLocation;
    private String nextLocation;
    private FirebaseDatabase database;
    private Calisanlar calisanlar;
    private Button Ekle;
    UUID uuid = UUID.randomUUID();
    DatabaseReference reference;

    FirebaseAuth auth ;
    FirebaseUser user ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        calisanlar = (Calisanlar) getIntent().getSerializableExtra("calisanlar");


        database = FirebaseDatabase.getInstance();


        auth = FirebaseAuth.getInstance();
         user = auth.getCurrentUser();


        current_location = findViewById(R.id.current_location);
        next_location = findViewById(R.id.next_location);


        Ekle = findViewById(R.id.Ekle);
        Ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocationsToEditTexts();
            }
        });

        mapSearchView=findViewById(R.id.mapSearch);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location=mapSearchView.getQuery().toString();
                List<Address> addressList=null;
                if (location!=null){
                    Geocoder geocoder =new Geocoder(MapsActivity.this);
                    try {
                        addressList=geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address=addressList.get(0);
                    LatLng latLng =new LatLng(address.getLatitude(),address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });



    }
    public void setLocationsToEditTexts() {

        if (currentLocation != null) {
            Geocoder geocoder = new Geocoder(this);
            try {
                List<Address> addressList = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                if (addressList != null && addressList.size() > 0) {
                    String il = addressList.get(0).getAdminArea(); // İl bilgisini alır
                    String ilce = addressList.get(0).getSubAdminArea(); // İlçe bilgisini alır
                    String konum=il+" "+ilce;
                    currLocation=konum;
                    current_location.setText(il + ", " + ilce); // İl ve ilçeyi TextView'e yazar

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            current_location.setText("Current Location: Not Available");
        }


        String searchedLocation = mapSearchView.getQuery().toString().trim();
            if (!searchedLocation.isEmpty()) {
                nextLocation=searchedLocation;
                next_location.setText(searchedLocation);

            } else {
                next_location.setText("Next Location: Not Found");
            }

        if (current_location != null && next_location!=null ) {
                    konum_ekle();
            Toast.makeText(MapsActivity.this, "Burtaxi Yola Çıktı!", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(MapsActivity.this, "Lütfen Gideceğiniz Konumu Giriniz!", Toast.LENGTH_SHORT).show();
        }


    }


    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                currentLocation = location;

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapsActivity.this);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission is denied, please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void konum_ekle() {


        String seyahat_id = user.getUid();

        String seyahatler_id=uuid.toString();

        String tarih = getDateTime();
        String sofor_isim = calisanlar.getCalisan_isim();

        Konum konum = new Konum(tarih, currLocation, nextLocation, sofor_isim, seyahat_id);

        Log.v("TAg",sofor_isim+" "+currLocation);
        if (database != null) {
            reference = database.getReference("Seyahatlerim");
            reference.child(seyahat_id).child(seyahatler_id).setValue(konum)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(MapsActivity.this, burtaxi_splash_screen.class);
                            startActivity(intent);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MapsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            // Eğer database null ise, burada bir işlem yapılabilir.
            Toast.makeText(MapsActivity.this, "Database instance is null", Toast.LENGTH_LONG).show();
        }
    }
    private String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}
