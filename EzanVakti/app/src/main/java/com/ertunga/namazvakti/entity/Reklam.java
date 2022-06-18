package com.ertunga.namazvakti.entity;

public class Reklam {

    private String image_url;
    private String site_url;

    public Reklam() {
    }

    public Reklam(String image_url,String site_url) {
        this.image_url = image_url;
        this.site_url = site_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getSite_url() {
        return site_url;
    }

    public void setSite_url(String site_url) {
        this.site_url = site_url;
    }
}

