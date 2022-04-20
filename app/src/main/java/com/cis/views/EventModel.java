package com.cis.views;

public class EventModel {
    private String title;
    private String desc;
    private String imageURl;

    public EventModel(String title, String desc, String imageURl) {
        this.title = title;
        this.desc = desc;
        this.imageURl = imageURl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(String imageURl) {
        this.imageURl = imageURl;
    }
}
