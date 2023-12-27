package com.burta.burtaxi;

import java.io.Serializable;
import java.util.List;

public class YorumBilgileri implements Serializable {
    private String yorumlar_icerik , yorum_kullanici;
    private List<YorumBilgileri> yorumlar;

    public YorumBilgileri() {
    }

    public YorumBilgileri(String yorumlar_icerik, String yorum_kullanici) {
        this.yorumlar_icerik = yorumlar_icerik;
        this.yorum_kullanici = yorum_kullanici;

    }

    public String getYorum_kullanici() {
        return yorum_kullanici;
    }

    public void setYorum_kullanici(String yorum_kullanici) {
        this.yorum_kullanici = yorum_kullanici;
    }

    public String getYorumlar_icerik() {
        return yorumlar_icerik;
    }

    public void setYorumlar_icerik(String yorumlar_icerik) {
        this.yorumlar_icerik = yorumlar_icerik;
    }

    public List<YorumBilgileri> getYorumlar() {
        return yorumlar;
    }

    public void setYorumlar(List<YorumBilgileri> yorumlar) {
        this.yorumlar = yorumlar;
    }


}