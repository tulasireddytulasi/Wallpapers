package com.example.tulasi.viewpager1;

public class DataModel {
    private String imgurl;
    private String txt;
    public DataModel(String imgurl, String txt){
        this.imgurl = imgurl;
        this.txt = txt;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getTxt() {
        return txt;
    }
}

