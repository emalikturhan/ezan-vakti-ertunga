package com.ertunga.namazvakti.Lists;

public class SehirList {

    String sehir_adi;
    String sehir_id;

    public SehirList(String sehir_adi, String sehir_id){
        this.sehir_adi = sehir_adi;
        this.sehir_id = sehir_id;
    }

    public String getSehir_adi() { return sehir_adi; }

    public void setSehir_adi(String sehir_adi) { this.sehir_adi = sehir_adi; }

    public String getSehir_id() { return sehir_id; }

    public void setSehir_id(String sehir_id) { this.sehir_id = sehir_id; }
}
