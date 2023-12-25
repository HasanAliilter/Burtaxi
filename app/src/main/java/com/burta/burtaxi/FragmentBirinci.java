package com.burta.burtaxi;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentBirinci extends Fragment {
    private Toolbar toolbar_fragmentBir;
    private RecyclerView rv_fragmentBir;
    private ArrayList<Calisanlar> calisanlarArrayList;
    private CalisanlarAdapter adapter;

    private DatabaseReference calisanlarRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_birinci, container, false);

        toolbar_fragmentBir = rootView.findViewById(R.id.toolbar_fragmentBir);
        rv_fragmentBir = rootView.findViewById(R.id.rv_fragmentBir);

        toolbar_fragmentBir.setTitle("Şoför Listesi");
        toolbar_fragmentBir.setTitleTextColor(Color.BLACK);

        rv_fragmentBir.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        calisanlarArrayList = new ArrayList<>();
        adapter = new CalisanlarAdapter(rootView.getContext(), calisanlarArrayList);

        calisanlarRef = FirebaseDatabase.getInstance().getReference().child("Calisanlar");

        calisanlarRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                calisanlarArrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Calisanlar calisanlar = snapshot.getValue(Calisanlar.class);
                    calisanlarArrayList.add(calisanlar);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hata durumu
            }
        });

        rv_fragmentBir.setAdapter(adapter);

        return rootView;
    }
    private void veriEkle() {
        // DatabaseReference oluştur
        DatabaseReference calisanlarRef = FirebaseDatabase.getInstance().getReference().child("Calisanlar");

        // Yeni bir anahtar (key) oluştur
        String yeniCalisanKey = calisanlarRef.push().getKey();

        // Yeni bir Calisanlar nesnesi oluştur
        Calisanlar yeniCalisan = new Calisanlar();
        yeniCalisan.setCalisan_id(1);
        yeniCalisan.setCalisan_isim("Ataham");
        yeniCalisan.setCalisan_resim("avatar3.png");
        yeniCalisan.setCalisan_meslek("Şoför");
        yeniCalisan.setCalisan_plaka("34 DEF 123");
        yeniCalisan.setCalisan_rating(3.0);
        yeniCalisan.setCalisan_durak_id(2);
        yeniCalisan.setCalisan_toplamOy(11);
        yeniCalisan.setCalisan_sehir_id(1);

        // DatabaseReference'in altına yeniCalisan'ı ekleyin
        calisanlarRef.child(yeniCalisanKey).setValue(yeniCalisan);

        // İşlem başarıyla tamamlandı
        // Log.d("Firebase", "Yeni çalışan eklendi. Key: " + yeniCalisanKey);
    }

}
