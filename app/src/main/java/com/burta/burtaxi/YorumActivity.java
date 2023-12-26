package com.burta.burtaxi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class YorumActivity extends AppCompatActivity {

    private ImageView imageViewYorum;
    private TextView textViewYorumIsim, textViewYorumMeslek, textViewYorumOylama, textViewYorumPlaka;
    private EditText editTextYorumYorum;
    private Button buttonYorum;
    private Calisanlar calisanlar;
    private DatabaseReference yorumlarRef;

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

        buttonYorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(editTextYorumYorum.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Lütfen Yorumunuzu Giriniz !", Toast.LENGTH_LONG).show();
                } else {
                    YorumYap(
                            calisanlar.getCalisan_resim(),
                            calisanlar.getCalisan_plaka(),
                            calisanlar.getCalisan_id(),
                            editTextYorumYorum.getText().toString().trim(),
                            calisanlar.getCalisan_isim());
                    Toast.makeText(getApplicationContext(), "Yorumunuz Başarıyla Gönderildi !", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(YorumActivity.this,MainActivity.class));
                    finish();
                }

            }
        });

    }
    public void YorumYap(String calisan_resim, String calisan_plaka, int calisan_id, String yorum, String calisan_isim) {
        DatabaseReference yorumlarRef = FirebaseDatabase.getInstance().getReference().child("yorumlar");

        // Yeni bir yorum nesnesi oluştur
        Yorumlar yorumObjesi = new Yorumlar();
        yorumObjesi.setYorumlar_calisan_id(calisan_id);
        yorumObjesi.setYorumlar_icerik(yorum);
        yorumObjesi.setYorumlar_yorumYapan(calisan_isim);
        yorumObjesi.setYorumlar_yorumPlaka(calisan_plaka);
        yorumObjesi.setYorumlar_resim(calisan_resim);


        // Yeni yorumu Firebase veritabanına ekleyin
        yorumlarRef.push().setValue(yorumObjesi);
    }

}