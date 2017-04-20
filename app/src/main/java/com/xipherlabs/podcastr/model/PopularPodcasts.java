package com.xipherlabs.podcastr.model;

/**
 * Created by xipher on 12/4/17.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopularPodcasts {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("podcasts")
    @Expose
    private List<Podcast> podcasts = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Podcast> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(List<Podcast> podcasts) {
        this.podcasts = podcasts;
    }
}