package com.burta.burtaxi;

public class kaydol {

    private String  kaydol_id,kaydol_username , kaydol_password , kaydol_eposta;

    public kaydol() {
    }

    public kaydol(String kaydol_id, String kaydol_username, String kaydol_password, String kaydol_eposta) {
        this.kaydol_id = kaydol_id;
        this.kaydol_username = kaydol_username;
        this.kaydol_password = kaydol_password;
        this.kaydol_eposta = kaydol_eposta;
    }

    public String getKaydol_id() {
        return kaydol_id;
    }

    public void setKaydol_id(String kaydol_id) {
        this.kaydol_id = kaydol_id;
    }

    public String getKaydol_username() {
        return kaydol_username;
    }

    public void setKaydol_username(String kaydol_username) {
        this.kaydol_username = kaydol_username;
    }

    public String getKaydol_password() {
        return kaydol_password;
    }

    public void setKaydol_password(String kaydol_password) {
        this.kaydol_password = kaydol_password;
    }

    public String getKaydol_eposta() {
        return kaydol_eposta;
    }

    public void setKaydol_eposta(String kaydol_eposta) {
        this.kaydol_eposta = kaydol_eposta;
    }
}