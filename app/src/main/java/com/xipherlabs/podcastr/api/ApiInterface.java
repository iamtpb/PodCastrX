package com.xipherlabs.podcastr.api;

import com.bumptech.glide.request.Request;
import com.xipherlabs.podcastr.model.Feed;
import com.xipherlabs.podcastr.model.PopularPodcasts;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("/api/highlights/us/50")
    Call<PopularPodcasts> getPopularPodcasts();


    @GET("/api/podcast/us/{feed}/")
    Call<Feed> getFeed(@Path("feed") String feed);
}