package com.xipherlabs.podcastr.model;

import java.util.List;

/**
 * Created by xipher on 21/4/17.
 */

public class Favorites {
    List<Podcast> favorites;

    public Favorites(){}
    public Favorites(List<Podcast> favorites){
        this.favorites = favorites;
    }
    public List<Podcast> getFavorites(){
        return favorites;
    }
    public void setFavorites(List<Podcast> favorites){
        this.favorites = favorites;
    }
}
