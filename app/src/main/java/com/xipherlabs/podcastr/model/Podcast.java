package com.xipherlabs.podcastr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by xipher on 12/4/17.
 */

public class Podcast {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("author")
    @Expose
    private String author;
    public Podcast(){

    }
    public Podcast(String id,String thumb,String name,String author){
        this.id=id;
        this.thumb=thumb;
        this.name=name;
        this.author=author;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}