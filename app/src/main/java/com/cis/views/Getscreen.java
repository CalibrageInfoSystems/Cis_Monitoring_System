package com.cis.views;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Getscreen {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("SystemName")
    @Expose
    private String systemName;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("thumbImage")
    @Expose
    private List<ThumbImage> thumbImage = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<ThumbImage> getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(List<ThumbImage> thumbImage) {
        this.thumbImage = thumbImage;
    }

    public class ThumbImage {

        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("thumbImage")
        @Expose
        private String thumbImage;
        @SerializedName("alt")
        @Expose
        private Object alt;
        @SerializedName("title")
        @Expose
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getThumbImage() {
            return thumbImage;
        }

        public void setThumbImage(String thumbImage) {
            this.thumbImage = thumbImage;
        }

        public Object getAlt() {
            return alt;
        }

        public void setAlt(Object alt) {
            this.alt = alt;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }

}
