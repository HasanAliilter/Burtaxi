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

public class FragmentIkinci extends Fragment {
    private Toolbar toolbar_yorumlar;
    private RecyclerView rv_yorumlar;
    private DatabaseReference yorumlarRef;
    private ArrayList<Yorumlar> yorumlarArrayList;
    private YorumlarAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ikinci, container, false);

        toolbar_yorumlar = rootView.findViewById(R.id.toolbar_yorumlar);
        rv_yorumlar = rootView.findViewById(R.id.rv_yorumlar);

        toolbar_yorumlar.setTitle("Yorumlar Sayfası");
        toolbar_yorumlar.setTitleTextColor(Color.BLACK);

        rv_yorumlar.setHasFixedSize(true);
        rv_yorumlar.setLayoutManager(new LinearLayoutManager(getContext()));

        yorumlarArrayList = new ArrayList<>();
        adapter = new YorumlarAdapter(rootView.getContext(), yorumlarArrayList);

        yorumlarRef = FirebaseDatabase.getInstance().getReference().child("yorumlar");

        yorumlarRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                yorumlarArrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Yorumlar yorumlar = snapshot.getValue(Yorumlar.class);
                    yorumlarArrayList.add(yorumlar);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hata durumu
            }
        });

        rv_yorumlar.setAdapter(adapter);

        return rootView;

    }

}
