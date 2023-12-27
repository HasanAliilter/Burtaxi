package com.burta.burtaxi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SeyahatlerimAdapter extends RecyclerView.Adapter<SeyahatlerimAdapter.CardViewTutucu> {
    private Context mContext;
    private ArrayList<Konum> KonumArrayList;

    Konum konum;

    public SeyahatlerimAdapter(Context mContext, ArrayList<Konum> KonumArrayList) {
        this.mContext = mContext;
        this.KonumArrayList = KonumArrayList;
    }

    @NonNull
    @Override
    public CardViewTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tasarim_seyahlerim, parent, false);
        return new CardViewTutucu(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CardViewTutucu holder, int position) {
        if (KonumArrayList != null) {
            konum = KonumArrayList.get(position);
            if (konum != null) {
                holder.textViewTarih.setText("Tarih : " + konum.getTarih());
                holder.textViewUlasim.setText("İlk Konum : " + konum.getIlk_konum());
                holder.textViewKonumDurak.setText("Son Konum : " + konum.getSonraki_konum());
                holder.textViewSofor.setText("Soför Ad : " + konum.getSofor_ad());
            }
        }
    }


    @Override
    public int getItemCount() {
        if (KonumArrayList != null) {
            return KonumArrayList.size();
        }
        return 0;
    }


    public class CardViewTutucu extends RecyclerView.ViewHolder {

        protected TextView textViewTarih, textViewUlasim, textViewKonumDurak, textViewSofor;

        protected CardView cardViewDort;


        public CardViewTutucu(@NonNull View itemView) {
            super(itemView);
            textViewTarih = itemView.findViewById(R.id.textViewTarih);
            textViewUlasim = itemView.findViewById(R.id.textViewUlasim);
            textViewKonumDurak = itemView.findViewById(R.id.textViewKonumDurak);
            textViewSofor = itemView.findViewById(R.id.textViewSoför);
            cardViewDort = itemView.findViewById(R.id.cardViewSeyahat);


        }
    }
}
