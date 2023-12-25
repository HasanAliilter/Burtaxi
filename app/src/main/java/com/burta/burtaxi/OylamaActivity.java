package com.burta.burtaxi;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
                if (ratingBarKisi.getRating() != 0) {
                    oylamaSonucu(calisanlar.getCalisan_id(), ratingBarKisi.getRating());
                    startActivity(new Intent(OylamaActivity.this,MainActivity.class));
                    finish();
                    // Diğer işlemleri ekleyebilirsiniz.
                } else {
                    Toast.makeText(getApplicationContext(), "Lütfen Oyunuzu Giriniz !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void oylamaSonucu(int calisanId, final double yeniOy) {
        // DatabaseReference calisanlarRef = FirebaseDatabase.getInstance().getReference().child("calisanlar");
        DatabaseReference calisanRef = FirebaseDatabase.getInstance().getReference().child("calisanlar").child(String.valueOf(calisanId));

        calisanRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Veri bulunduğunda yapılacak işlemler
                    Calisanlar calisan = dataSnapshot.getValue(Calisanlar.class);
                    if (calisan != null) {
                        double oy = calisan.getCalisan_rating();
                        int toplamOy = calisan.getCalisan_toplamOy();

                        if (toplamOy == 0) {
                            toplamOy = 2;
                        }

                        double sonuc = ((yeniOy + (oy * (toplamOy - 1))) / toplamOy);
                        sonuc = Double.parseDouble(String.format(Locale.US, "%.2f", sonuc));

                        // Yeni değerleri güncelle
                        dataSnapshot.getRef().child("calisan_rating").setValue(sonuc);
                        dataSnapshot.getRef().child("calisan_toplamOy").setValue(toplamOy + 1);
                    }
                } else {
                    // Veri bulunamadığında yapılacak işlemler
                    Log.e("Firebase", "Belirtilen calisan_id'ye sahip veri bulunamadı.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hata durumu
                Log.e("Firebase", "Veritabanı hatası: " + databaseError.getMessage());
            }
        });
    }

}
