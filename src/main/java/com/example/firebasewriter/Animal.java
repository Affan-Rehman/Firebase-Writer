package com.example.firebasewriter;

import androidx.annotation.Keep;

@Keep
public class Animal {
    String purl="";
    String soundUrl="";
    String name;                    //Simply the Animal's name
    String yt = "";

    Animal(){

    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYt() {
        return yt;
    }

    public void setYt(String yt) {
        this.yt = yt;
    }

    Animal(String yt, int count, boolean selected){
        this.yt = yt;
    }
}