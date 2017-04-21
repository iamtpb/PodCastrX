package com.xipherlabs.podcastr.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Episode implements Serializable {

    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("date")
    @Expose
    private Integer date;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("mp3")
    @Expose
    private String mp3;

    public Episode() {
    }

    /**
     *
     * @param author
     * @param duration
     * @param title
     * @param description
     * @param link
     * @param mp3
     * @param date
     * @param thumb
     * @param size
     */
    public Episode(String thumb, String author, String title, String link, Integer date, String description, String duration, String size, String mp3) {
        super();
        this.thumb = thumb;
        this.author = author;
        this.title = title;
        this.link = link;
        this.date = date;
        this.description = description;
        this.duration = duration;
        this.size = size;
        this.mp3 = mp3;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

}