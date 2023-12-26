package com.burta.burtaxi;

public class Konum {
    public String Ilk_konum;
    public String Sonraki_konum;

    public Konum() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Konum(String Ilk_konum, String Sonraki_konum) {
        this.Ilk_konum = Ilk_konum;
        this.Sonraki_konum = Sonraki_konum;
    }
}
