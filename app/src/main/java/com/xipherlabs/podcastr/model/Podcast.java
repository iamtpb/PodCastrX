package com.xipherlabs.podcastr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by xipher on 12/4/17.
 */

public class Podcast implements Serializable{

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

    @Override
    public boolean equals(Object obj) {
        if(obj==null)
            return false;
        try {
            final Podcast other = (Podcast) obj;

            if (other.getId() == null || this.getId() == null) {
                return false;
            }else if (this.getId().compareTo(other.getId()) == 0) {
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}