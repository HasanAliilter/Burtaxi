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

public class Fragmentbesinci extends Fragment {
    private Toolbar toolbar_fragmentBes;
    private RecyclerView rv_fragmentBes;
    private ArrayList<Calisanlar> calisanlarArrayList;
    private CalisanlarAdapterCagir adapter;

    private DatabaseReference calisanlarRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_besinci, container, false);

        toolbar_fragmentBes = rootView.findViewById(R.id.toolbar_fragmentBes);
        rv_fragmentBes = rootView.findViewById(R.id.rv_fragmentBes);

        toolbar_fragmentBes.setTitle("Burtaxi Şoförleri");
        toolbar_fragmentBes.setTitleTextColor(Color.BLACK);

        rv_fragmentBes.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        calisanlarArrayList = new ArrayList<>();
        adapter = new CalisanlarAdapterCagir(rootView.getContext(), calisanlarArrayList);

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

        rv_fragmentBes.setAdapter(adapter);

        return rootView;
    }
}
