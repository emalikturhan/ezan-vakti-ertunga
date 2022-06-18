package com.ertunga.namazvakti.Lists;

public class ZikirList {

    int id;
    String baslik;
    String okunus;
    String meali;
    String arapca;
    int adet;
    int icon;

    public ZikirList(int id, String baslik, String okunus, String meali, String arapca, int adet, int icon){
        this.id = id;
        this.baslik = baslik;
        this.okunus = okunus;
        this.meali = meali;
        this.arapca = arapca;
        this.adet = adet;
        this.icon= icon;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getBaslik() { return baslik; }

    public void setBaslik(String baslik) { this.baslik = baslik; }

    public String getOkunus() { return okunus; }

    public void setOkunus(String okunus) { this.okunus = okunus; }

    public String getMeali() { return meali; }

    public void setMeali(String meali) { this.meali = meali; }

    public String getArapca() { return arapca; }

    public void setArapca(String arapca) { this.arapca = arapca; }

    public int getAdet() { return adet; }

    public void setAdet(int adet) { this.adet = adet; }

    public int getIcon() { return icon; }

    public void setIcon(int icon) { this.icon = icon; }
}
