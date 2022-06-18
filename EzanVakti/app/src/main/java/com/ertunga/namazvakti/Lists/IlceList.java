package com.ertunga.namazvakti.Lists;

public class IlceList {

    String ilce_adi;
    String ilce_id;

    public IlceList(String ilce_adi, String ilce_id){
        this.ilce_adi = ilce_adi;
        this.ilce_id = ilce_id;
    }

    public String getIlce_adi() { return ilce_adi; }

    public void setIlce_adi(String ilce_adi) { this.ilce_adi = ilce_adi; }

    public String getIlce_id() { return ilce_id; }

    public void setIlce_id(String ilce_id) { this.ilce_id = ilce_id; }
}
