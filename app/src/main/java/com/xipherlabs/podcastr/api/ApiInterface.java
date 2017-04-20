package com.xipherlabs.podcastr.api;

import com.xipherlabs.podcastr.model.PopularPodcasts;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("/api/highlights/us/50")
    Call<PopularPodcasts> getPopularPodcasts();

/*
    @GET("")
    Call<Response> getResponseApi(@Query("api_key") String apiKey);
 */
}