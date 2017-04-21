package com.xipherlabs.podcastr.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feed implements Serializable {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("feed")
    @Expose
    private String feed;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("release")
    @Expose
    private Integer release;
    @SerializedName("episodes")
    @Expose
    private List<Episode> episodes = null;

    public Feed() {
    }

    public Feed(Integer status, Integer id, String thumb, String name, String site, String description, String author, String category, String feed, Integer total, Integer release, List<Episode> episodes) {
        super();
        this.status = status;
        this.id = id;
        this.thumb = thumb;
        this.name = name;
        this.site = site;
        this.description = description;
        this.author = author;
        this.category = category;
        this.feed = feed;
        this.total = total;
        this.release = release;
        this.episodes = episodes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getRelease() {
        return release;
    }

    public void setRelease(Integer release) {
        this.release = release;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

}