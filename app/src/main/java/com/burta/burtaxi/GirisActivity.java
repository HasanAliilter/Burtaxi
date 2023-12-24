package com.burta.burtaxi;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class GirisActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private Button buttonGiris;
    private EditText editText_KullaniciAdi, editText_KullaniciSifre;
    private TextView textView_Kaydol;


    public void girisYap() {
        String Eposta = editText_KullaniciAdi.getText().toString();
        String sifre = editText_KullaniciSifre.getText().toString();

        if (Eposta.matches("") || sifre.matches("")) {
            Toast.makeText(this, "Email and password are required!", Toast.LENGTH_LONG).show();
        } else {
            auth.signInWithEmailAndPassword(Eposta,sifre).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent = new Intent(GirisActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GirisActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        auth=FirebaseAuth.getInstance();


        buttonGiris = findViewById(R.id.buttonGiris);
        editText_KullaniciAdi = findViewById(R.id.editText_KullaniciAdi);
        editText_KullaniciSifre = findViewById(R.id.editText_KullaniciSifre);
        textView_Kaydol = findViewById(R.id.textView_Kaydol);



        FirebaseUser user = auth.getCurrentUser();

           if (user != null) {
            Intent intent = new Intent(GirisActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //click eventleri
        textView_Kaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GirisActivity.this, KaydolActivity.class));
            }
        });
        buttonGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                girisYap();
            }
        });
    }



}
