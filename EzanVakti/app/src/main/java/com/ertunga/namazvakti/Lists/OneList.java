package com.ertunga.namazvakti.Lists;

public class OneList {

    String baslik;
    String data;
    int icon;

    public OneList(String baslik, String data,int icon){
        this.baslik = baslik;
        this.data = data;
        this.icon= icon;
    }

    public String getBaslik() { return baslik; }

    public void setBaslik(String baslik) { this.baslik = baslik; }

    public String getData() { return data; }

    public void setData(String data) { this.data = data; }

    public int getIcon() { return icon; }

    public void setIcon(int icon) { this.icon = icon; }
}
