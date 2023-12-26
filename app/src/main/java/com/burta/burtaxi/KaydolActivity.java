package com.burta.burtaxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.UUID;

public class KaydolActivity extends AppCompatActivity {
    private TextView textView_KaydolGiris;
    private Toolbar toolbar_kaydol;
    private EditText editTextKaydol_Sifre, editTextKaydol_SifreTekrar, editTextKaydol_Eposta, editTextKaydol_KullaniciAdi;
    private Button buttonKaydol;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    DatabaseReference reference;
    UUID uuid = UUID.randomUUID();
  ;

    private void kayit_ekle(){
       String kullanici_ad=editTextKaydol_KullaniciAdi.getText().toString();
       String  Eposta=editTextKaydol_Eposta.getText().toString();
       String sifre=editTextKaydol_Sifre.getText().toString();
       String sifre_tekrar=editTextKaydol_SifreTekrar.getText().toString();
       String kullanici_id = uuid.toString();

        if (Eposta.length() == 0 || sifre.length() == 0 || sifre_tekrar.length() == 0 || kullanici_id.length() == 0 || kullanici_ad.length() == 0 ) {
            Toast.makeText(this, "Please fill in all the required fields.", Toast.LENGTH_LONG).show();
        } else {
            auth.createUserWithEmailAndPassword(Eposta,sifre)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Kullanıcı başarıyla oluşturuldu, auth UID'sini al
                                String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

                                kaydol user = new kaydol(userId,kullanici_ad,sifre,Eposta);

                                // Real-time database'e kullanıcıyı kaydet
                                reference = database.getReference("Users");
                                reference.child(userId).setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(KaydolActivity.this, "User created successfully.", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(KaydolActivity.this, GirisActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(KaydolActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                // Kullanıcı oluşturma başarısız olursa
                                Toast.makeText(KaydolActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaydol);



        database=FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();



        //idler
        textView_KaydolGiris = findViewById(R.id.textView_KaydolGiris);
        toolbar_kaydol = findViewById(R.id.toolbar_kaydol);
        editTextKaydol_Sifre = findViewById(R.id.editTextKaydol_Sifre);
        editTextKaydol_SifreTekrar = findViewById(R.id.editTextKaydol_SifreTekrar);
        editTextKaydol_Eposta = findViewById(R.id.editTextKaydol_Eposta);
        editTextKaydol_KullaniciAdi = findViewById(R.id.editTextKaydol_KullaniciAdi);
        buttonKaydol = findViewById(R.id.buttonKaydol);


        //toolbar Ayarları
        toolbar_kaydol.setTitle("Kayıt Ol !");
        toolbar_kaydol.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar_kaydol);



        //click eventleri

        textView_KaydolGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KaydolActivity.this, GirisActivity.class));
                finish();
            }
        });

        buttonKaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayit_ekle();
            }
        });
    }
}