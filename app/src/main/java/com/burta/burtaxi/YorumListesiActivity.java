package com.burta.burtaxi;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.burta.burtaxi.R; // Eğer bu kütüphane kullanılıyorsa ekleyin
import com.burta.burtaxi.YorumBilgileri;
import com.burta.burtaxi.YorumListesiAdapter;
import com.burta.burtaxi.Yorumlar;

import java.util.ArrayList;
import java.util.List;

public class YorumListesiActivity extends AppCompatActivity {
    private Toolbar toolbar_liste;
    private ImageView imageViewYorum_SoforResim;
    private TextView textViewYorum_SoforIsım;
    private RecyclerView rvYorumList;
    private YorumListesiAdapter adapter;
    private List<YorumBilgileri> yorumBilgileriList;
    private Yorumlar yorumlar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yorum_liste);

        toolbar_liste = findViewById(R.id.toolbar_liste);
        imageViewYorum_SoforResim = findViewById(R.id.imageViewYorum_SoforResim);
        textViewYorum_SoforIsım = findViewById(R.id.textViewYorum_SoforIsım);
        rvYorumList = findViewById(R.id.rv_yorumlist);

        toolbar_liste.setTitle("Şoför Yorumlar");
        toolbar_liste.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar_liste);

        yorumlar = (Yorumlar) getIntent().getSerializableExtra("yorumlar");

        // Yorumlar nesnesinin null olup olmadığını kontrol et
        if (yorumlar != null) {
            // Yorumlar nesnesi null değilse işlemleri devam ettir
            imageViewYorum_SoforResim.setImageResource(getResources().getIdentifier(yorumlar.getYorumlar_resim(), "drawable", getPackageName()));
            textViewYorum_SoforIsım.setText(yorumlar.getYorumlar_yorumYapan());

            // Firebase'den alacağınız verileri bu listeye ekleyin
            yorumBilgileriList = new ArrayList<>();
            yorumBilgileriList = yorumlar.getYorum_bilgileri().getYorumlar(); // Veriyi al

            adapter = new YorumListesiAdapter(this, yorumBilgileriList);
            rvYorumList.setLayoutManager(new LinearLayoutManager(this));
            rvYorumList.setAdapter(adapter);
        } else {
            // Yorumlar nesnesi null ise gerekli işlemleri yapın (örneğin bir hata mesajı gösterme)
            // Örnek: Toast.makeText(this, "Yorumlar nesnesi null", Toast.LENGTH_SHORT).show();
        }
    }
}
