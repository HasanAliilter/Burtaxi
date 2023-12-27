package com.burta.burtaxi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.burta.burtaxi.R;
import com.burta.burtaxi.YorumBilgileri;
import com.burta.burtaxi.Yorumlar;

import java.util.ArrayList;
import java.util.List;

public class YorumListesiAdapter extends RecyclerView.Adapter<YorumListesiAdapter.CardViewTutucuYorumlar> {
    private Context mContext;
    private List<YorumBilgileri> yorumlarList;

    public YorumListesiAdapter(Context mContext, List<YorumBilgileri> yorumlarList) {
        this.mContext = mContext;
        this.yorumlarList = yorumlarList;
    }

    @NonNull
    @Override
    public YorumListesiAdapter.CardViewTutucuYorumlar onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_yorum_listesi, parent, false);
        return new YorumListesiAdapter.CardViewTutucuYorumlar(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YorumListesiAdapter.CardViewTutucuYorumlar holder, int position) {
        // Veri setindeki pozisyonu al
        YorumBilgileri yorumBilgileri = yorumlarList.get(position);

        // Null kontrolü yap
        if (yorumBilgileri != null) {
            // İlgili verileri holder bileşenlerine at
            holder.textView_YorumListesiIsim.setText("Yorum Sahibi : " + yorumBilgileri.getYorum_kullanici());
            holder.textView_YorumListesiIcerik.setText(yorumBilgileri.getYorumlar_icerik());
        } else {
            // Eğer yorumBilgileri null ise, gerekli işlemleri yapabilirsiniz
            // Örneğin, bir hata mesajı gösterme veya boş bir durumu ele alma
        }
    }


    @Override
    public int getItemCount() {
        return yorumlarList.size();
    }

    public class CardViewTutucuYorumlar extends RecyclerView.ViewHolder {
        protected CardView cardViewYorumlar;
        protected TextView textView_YorumListesiIsim, textView_YorumListesiIcerik;

        public CardViewTutucuYorumlar(@NonNull View itemView) {
            super(itemView);
            cardViewYorumlar = itemView.findViewById(R.id.cardViewYorumListesi);
            textView_YorumListesiIsim = itemView.findViewById(R.id.textView_YorumListesiIsim);
            textView_YorumListesiIcerik = itemView.findViewById(R.id.textView_YorumListesiIcerik);
        }
    }
}
