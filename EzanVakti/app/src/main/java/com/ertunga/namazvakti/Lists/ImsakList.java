package com.ertunga.namazvakti.Lists;

public class ImsakList {

    String gun;
    String gun_txt;
    String ay;
    String hicri;
    String imsak;
    String oglen;
    String aksam;
    String sabah;

    public ImsakList(String gun,String gun_txt,String ay,String hicri,String imsak,String sabah,String oglen,String aksam){
        this.gun = gun;
        this.gun_txt = gun_txt;
        this.ay = ay;
        this.hicri = hicri;
        this.imsak = imsak;
        this.sabah = sabah;
        this.oglen = oglen;
        this.aksam = aksam;
    }

    public String getAksam() { return aksam; }

    public void setAksam(String aksam) { this.aksam = aksam; }

    public String getAy() { return ay; }

    public void setAy(String ay) { this.ay = ay; }

    public String getGun() { return gun; }

    public void setGun(String gun) { this.gun = gun; }

    public String getGun_txt() { return gun_txt; }

    public void setGun_txt(String gun_txt) { this.gun_txt = gun_txt; }

    public String getHicri() { return hicri; }

    public void setHicri(String hicri) { this.hicri = hicri; }

    public String getImsak() { return imsak; }

    public void setImsak(String imsak) { this.imsak = imsak; }

    public String getSabah() {
        return sabah;
    }

    public void setSabah(String sabah) {
        this.sabah = sabah;
    }

    public String getOglen() {
        return oglen;
    }

    public void setOglen(String oglen) {
        this.oglen = oglen;
    }
}
