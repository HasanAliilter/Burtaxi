package com.burta.burtaxi;

public class Konum {
    private String tarih;
    private String ilk_konum;
    private String Sonraki_konum;

    private  String sofor_ad;
    private String seyahat_id ;

    public Konum(){}
    public Konum(String tarih, String ilkKonum, String sonrakiKonum, String sofor_ad, String seyahatId) {
        this.tarih = tarih;
        this.ilk_konum = ilkKonum;
        this.Sonraki_konum = sonrakiKonum;
        this.sofor_ad = sofor_ad;
        this.seyahat_id = seyahatId;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getIlk_konum() {
        return ilk_konum;
    }

    public void setIlk_konum(String ilk_konum) {
        this.ilk_konum = ilk_konum;
    }

    public String getSonraki_konum() {
        return Sonraki_konum;
    }

    public void setSonraki_konum(String sonraki_konum) {
        Sonraki_konum = sonraki_konum;
    }

    public String getSofor_ad() {
        return sofor_ad;
    }

    public void setSofor_ad(String sofor_ad) {
        this.sofor_ad = sofor_ad;
    }

    public String getSeyahat_id() {
        return seyahat_id;
    }

    public void setSeyahat_id(String seyahat_id) {
        this.seyahat_id = seyahat_id;
    }
}
