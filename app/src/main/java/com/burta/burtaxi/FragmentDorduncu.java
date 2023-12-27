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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentDorduncu extends Fragment {
    private Toolbar toolbar_fragmentDort;
    private RecyclerView rv_fragmentDort;

    private ArrayList<Konum> KonumArrayList;
    private SeyahatlerimAdapter adapter;

    private DatabaseReference SehayatRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dorduncu, container, false);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        String kullanıcı_id = user.getUid();


        toolbar_fragmentDort = rootView.findViewById(R.id.toolbar_seyahatler);
        rv_fragmentDort = rootView.findViewById(R.id.rv_seyahatler);

        toolbar_fragmentDort.setTitle("Seyahat Listesi");
        toolbar_fragmentDort.setTitleTextColor(Color.BLACK);

        rv_fragmentDort.setHasFixedSize(true);
        rv_fragmentDort.setLayoutManager(new LinearLayoutManager(getContext()));

        KonumArrayList = new ArrayList<>();
        adapter = new SeyahatlerimAdapter(rootView.getContext(), KonumArrayList);

        SehayatRef = FirebaseDatabase.getInstance().getReference().child("Seyahatlerim").child(kullanıcı_id);

        SehayatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                KonumArrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Konum konum = snapshot.getValue(Konum.class);
                    KonumArrayList.add(konum);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hata durumu
            }
        });

        rv_fragmentDort.setAdapter(adapter);

        return rootView;

    }
}
