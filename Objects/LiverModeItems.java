package com.example.cody.liverhub.Objects;

import java.util.List;

public class LiverModeItems {
    private int id;
    private int name;
    private String url;
    private String tumor_url;

    public String getTumor_url() {
        return tumor_url;
    }

    public void setTumor_url(String tumor_url) {
        this.tumor_url = tumor_url;
    }

    public LiverModeItems(int id, String url){
        this.id = id;
        if(url.contains("*")){
            String[] url_s = url.split("\\*");
            this.url = url_s[0];
            this.tumor_url = url_s[1];
        }
        else{
            this.url = url;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
