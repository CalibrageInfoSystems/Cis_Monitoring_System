package com.cis.views;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventsModel {

    @SerializedName("EventsTable")
    @Expose
    private List<EventsTable> eventsTable = null;
    @SerializedName("ScrollBarTextTable")
    @Expose
    private List<ScrollBarTextTable> scrollBarTextTable = null;
    @SerializedName("BackGround")
    @Expose
    private List<BackGround> backGround = null;

    public List<EventsTable> getEventsTable() {
        return eventsTable;
    }

    public void setEventsTable(List<EventsTable> eventsTable) {
        this.eventsTable = eventsTable;
    }

    public List<ScrollBarTextTable> getScrollBarTextTable() {
        return scrollBarTextTable;
    }

    public void setScrollBarTextTable(List<ScrollBarTextTable> scrollBarTextTable) {
        this.scrollBarTextTable = scrollBarTextTable;
    }

    public List<BackGround> getBackGround() {
        return backGround;
    }

    public void setBackGround(List<BackGround> backGround) {
        this.backGround = backGround;
    }
    public class EventsTable {

        @SerializedName("EventTitle")
        @Expose
        private String eventTitle;
        @SerializedName("EventDate")
        @Expose
        private String eventDate;
        @SerializedName("ImageUrl")
        @Expose
        private String imageUrl;
        @SerializedName("Description")
        @Expose
        private String description;

        public String getEventTitle() {
            return eventTitle;
        }

        public void setEventTitle(String eventTitle) {
            this.eventTitle = eventTitle;
        }

        public String getEventDate() {
            return eventDate;
        }

        public void setEventDate(String eventDate) {
            this.eventDate = eventDate;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }
    public class ScrollBarTextTable {

        @SerializedName("DailyText")
        @Expose
        private String dailyText;

        public String getDailyText() {
            return dailyText;
        }

        public void setDailyText(String dailyText) {
            this.dailyText = dailyText;
        }

    }
    public class BackGround {

        @SerializedName("BackGroundImage")
        @Expose
        private String backGroundImage;

        public String getBackGroundImage() {
            return backGroundImage;
        }

        public void setBackGroundImage(String backGroundImage) {
            this.backGroundImage = backGroundImage;
        }

    }
}
