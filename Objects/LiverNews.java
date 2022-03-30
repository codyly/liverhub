package com.example.cody.liverhub.Objects;

import java.util.List;

public class LiverNews {
    private String title="news";
    private float id;
    private int pic_id;
    private String detail;
    private String url;
    private String origin;
    private String brief;
    private List<String> imgs;

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public LiverNews(int pic_id){
        this.pic_id = pic_id;
    }
    public String getTitle(){
        return title;
    }

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public int getPic_id() {
        return pic_id;
    }

    public void setPic_id(int pic_id) {
        this.pic_id = pic_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
