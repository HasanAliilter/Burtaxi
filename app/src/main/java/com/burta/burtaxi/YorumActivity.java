package com.burta.burtaxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class YorumActivity extends AppCompatActivity {

    private ImageView imageViewYorum;
    private TextView textViewYorumIsim, textViewYorumMeslek, textViewYorumOylama, textViewYorumPlaka;
    private EditText editTextYorumYorum;
    private Button buttonYorum;
    private Calisanlar calisanlar;
    private DatabaseReference yorumlarRef;
    private YorumBilgileri yorumBilgileri;
    private String kullaniciAdi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yorum);

        imageViewYorum = findViewById(R.id.imageViewYorum);
        textViewYorumIsim = findViewById(R.id.textViewYorumIsim);
        textViewYorumMeslek = findViewById(R.id.textViewYorumMeslek);
        textViewYorumOylama = findViewById(R.id.textViewYorumOylama);
        textViewYorumPlaka = findViewById(R.id.textViewYorumPlaka);
        editTextYorumYorum = findViewById(R.id.editTextYorumYorum);
        buttonYorum = findViewById(R.id.buttonYorum);


        calisanlar = (Calisanlar) getIntent().getSerializableExtra("calisanlar");

        imageViewYorum.setImageResource(getResources().getIdentifier(calisanlar.getCalisan_resim(), "drawable", getPackageName()));
        textViewYorumIsim.setText("Sürücü İsmi : " + calisanlar.getCalisan_isim());
        textViewYorumMeslek.setText("Sürücü Meslek : " + calisanlar.getCalisan_meslek());
        textViewYorumPlaka.setText("Araç Plakası : " + calisanlar.getCalisan_plaka());
        textViewYorumOylama.setText("Sürücü Ortalaması : " + calisanlar.getCalisan_rating());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        String kullanıcı_id = user.getUid();

        FirebaseDatabase.getInstance().getReference("Users").child(kullanıcı_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HashMap<String, Object> veri = (HashMap<String, Object>) dataSnapshot.getValue();
                    kullaniciAdi = (String) veri.get("kaydol_username");

                } else {
                    Log.v("TAG", "Veri bulunamadı");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Hata durumunda burada işlemler yapabilirsiniz
            }
        });

        buttonYorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yorumBilgileri = new YorumBilgileri(editTextYorumYorum.getText().toString().trim(),kullaniciAdi);

                if (TextUtils.isEmpty(editTextYorumYorum.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Lütfen Yorumunuzu Giriniz !", Toast.LENGTH_LONG).show();
                } else {
                    YorumYap(
                            calisanlar.getCalisan_resim(),
                            calisanlar.getCalisan_plaka(),
                            calisanlar.getCalisan_id(),
                            yorumBilgileri,
                            calisanlar.getCalisan_isim());
                    Toast.makeText(getApplicationContext(), "Yorumunuz Başarıyla Gönderildi !", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(YorumActivity.this,MainActivity.class));
                    finish();
                }

            }
        });

    }
    public void YorumYap(String calisan_resim, String calisan_plaka, int calisan_id, YorumBilgileri yorum, String calisan_isim) {
        DatabaseReference yorumlarRef = FirebaseDatabase.getInstance().getReference().child("yorumlar");

        // Yeni bir yorum nesnesi oluştur
        Yorumlar yorumObjesi = new Yorumlar();
        yorumObjesi.setYorumlar_calisan_id(calisan_id);
        yorumObjesi.setYorum_bilgileri(yorum);
        yorumObjesi.setYorumlar_yorumYapan(calisan_isim);
        yorumObjesi.setYorumlar_yorumPlaka(calisan_plaka);
        yorumObjesi.setYorumlar_resim(calisan_resim);

        // Belirli bir calisan_id'ye sahip yorumları kontrol et
        Query query = yorumlarRef.orderByChild("yorumlar_calisan_id").equalTo(calisan_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Belirli bir calisan_id'ye sahip yorum var
                    for (DataSnapshot yorumSnapshot : dataSnapshot.getChildren()) {
                        // Yorum nesnesini al
                        Yorumlar existingYorum = yorumSnapshot.getValue(Yorumlar.class);

                        if (existingYorum != null && existingYorum.getYorum_bilgileri() != null) {
                            // Eğer yorum_bilgileri null değilse, yani daha önce yorum yapılmışsa
                            // Yorum_bilgileri nesnesini al
                            YorumBilgileri existingYorumBilgileri = existingYorum.getYorum_bilgileri();

                            // Yeni yorumları ekleyin
                            if (existingYorumBilgileri.getYorumlar() == null) {
                                // Daha önce yorum yoksa, yeni bir liste oluşturun
                                List<YorumBilgileri> yorumlarListesi = new ArrayList<>();
                                yorumlarListesi.add(yorum);
                                existingYorumBilgileri.setYorumlar(yorumlarListesi);
                            } else {
                                // Daha önce yorum varsa, mevcut listeye yeni yorumları ekleyin
                                existingYorumBilgileri.getYorumlar().add(yorum);
                            }

                            // Yorum nesnesinin altındaki yorum_bilgileri'ni güncelle
                            yorumSnapshot.getRef().child("yorum_bilgileri").setValue(existingYorumBilgileri);
                        } else {
                            // Eğer daha önce yorum yapılmamışsa, yeni yorum_bilgileri'ni ekle
                            YorumBilgileri yeniYorumBilgileri = new YorumBilgileri();
                            List<YorumBilgileri> yorumlarListesi = new ArrayList<>();
                            yorumlarListesi.add(yorum);
                            yeniYorumBilgileri.setYorumlar(yorumlarListesi);

                            yorumSnapshot.getRef().child("yorum_bilgileri").setValue(yeniYorumBilgileri);
                        }
                    }
                } else {
                    // Belirli bir calisan_id'ye sahip yorum yok, yeni bir yorum ekleyin
                    Yorumlar yeniYorum = new Yorumlar();
                    YorumBilgileri yeniYorumBilgileri = new YorumBilgileri();
                    List<YorumBilgileri> yorumlarListesi = new ArrayList<>();
                    yorumlarListesi.add(yorum);
                    yeniYorumBilgileri.setYorumlar(yorumlarListesi);
                    yeniYorumBilgileri.setYorumlar(yorumlarListesi);
                    yeniYorum.setYorumlar_calisan_id(calisan_id);
                    yeniYorum.setYorum_bilgileri(yeniYorumBilgileri);
                    yeniYorum.setYorumlar_yorumYapan(calisan_isim);
                    yeniYorum.setYorumlar_yorumPlaka(calisan_plaka);
                    yeniYorum.setYorumlar_resim(calisan_resim);

                    yorumlarRef.push().setValue(yeniYorum);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Hata durumu
            }
        });
    }



}