package com.burta.burtaxi;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class OylamaActivity extends AppCompatActivity {
    private DatabaseReference calisanlarRef;

    private RatingBar ratingBarKisi;
    private Button buttonKaydetKisi;
    private ImageView imageViewKisi;
    private TextView textViewIsimKisi, textViewMeslekKisi, textViewOylamaKisi, textViewPlakaKisi;

    private Calisanlar calisanlar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oylama);

        imageViewKisi = findViewById(R.id.imageViewKisi);
        textViewIsimKisi = findViewById(R.id.textViewIsimKisi);
        textViewMeslekKisi = findViewById(R.id.textViewMeslekKisi);
        textViewOylamaKisi = findViewById(R.id.textViewOylamaKisi);
        textViewPlakaKisi = findViewById(R.id.textViewPlakaKisi);
        buttonKaydetKisi = findViewById(R.id.buttonKaydetKisi);
        ratingBarKisi = findViewById(R.id.ratingBarKisi);

        calisanlar = (Calisanlar) getIntent().getSerializableExtra("calisanlar");

        imageViewKisi.setImageResource(getResources().getIdentifier(calisanlar.getCalisan_resim(), "drawable", getPackageName()));
        textViewIsimKisi.setText("Sürücü İsmi : " + calisanlar.getCalisan_isim());
        textViewMeslekKisi.setText("Sürücü Meslek : " + calisanlar.getCalisan_meslek());
        textViewPlakaKisi.setText("Araç Plakası : " + calisanlar.getCalisan_plaka());
        textViewOylamaKisi.setText("Sürücü Ortalaması : " + calisanlar.getCalisan_rating());

        buttonKaydetKisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    OylamaSonucu(calisanlar, ratingBarKisi.getRating());
                    startActivity(new Intent(OylamaActivity.this,MainActivity.class));
                    finish();
            }
        });
    }

    public void OylamaSonucu(Calisanlar calisan, double calisan_rating) {
        int calisan_id = calisan.getCalisan_id();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Calisanlar");

        Query query = databaseReference.orderByChild("calisan_id").equalTo(calisan_id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        Double oy = calisan.getCalisan_rating();
                        Integer toplamOy = calisan.getCalisan_toplamOy();

                        if (oy != null && toplamOy != null) {
                            if (toplamOy == 0) {
                                toplamOy = 2;
                            }

                            final int finalToplamOy = toplamOy;

                            double sonuc = ((calisan_rating + (oy * (finalToplamOy - 1))) / finalToplamOy);
                            sonuc = Double.parseDouble(String.format(Locale.US, "%.2f", sonuc));

                            Log.e("dalga1", "Yeni Rating: " + sonuc);
                            Log.e("dalga1", "Yeni ToplamOy: " + (finalToplamOy + 1));

                            // Güncellenmiş değerleri Firebase Realtime Database'e yaz
                            childSnapshot.child("calisan_rating").getRef().setValue(sonuc);
                            childSnapshot.child("calisan_toplamOy").getRef().setValue(finalToplamOy + 1);
                        } else {
                            // Değerler null ise, hata durumunu ele alabilirsin
                            Log.e("dalga1", "oy veya toplamOy null");
                        }
                    }
                } else {
                    Log.e("dalga1", "DataSnapshot boş");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("dalga1", "Veritabanı Hatası: " + databaseError.getMessage());
            }
        });
    }


}
