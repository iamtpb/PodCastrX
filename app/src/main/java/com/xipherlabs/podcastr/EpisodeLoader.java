package com.xipherlabs.podcastr;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.media.MediaDescriptionCompat;
import android.util.Log;
import android.widget.Toast;

import com.xipherlabs.podcastr.api.ApiClient;
import com.xipherlabs.podcastr.api.ApiInterface;
import com.xipherlabs.podcastr.model.Episode;
import com.xipherlabs.podcastr.model.Feed;
import com.xipherlabs.podcastr.model.Podcast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//Reference: https://developer.android.com/reference/android/content/AsyncTaskLoader.html

public class EpisodeLoader extends AsyncTaskLoader<List<Episode>> {
    Podcast podcast;
    private List<Episode> mData;

    public EpisodeLoader(Context context,Podcast podcast) {
        super(context);
        this.podcast = podcast;
    }

    @Override
    public List<Episode> loadInBackground() {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<Feed> call = api.getFeed(podcast.getId());
        Feed feed = null;
        try{
            feed = call.execute().body();
        }catch (Exception e){
            Log.e("SyncRetroLoader",""+e.getMessage());
        }
        if(feed==null){
            return null;
        }
        return feed.getEpisodes();
    }


    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override public void deliverResult(List<Episode> episodes) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (episodes != null) {
                onReleaseResources(episodes);
            }
        }
        List<Episode> oldApps = mData;

        mData = episodes;
        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(episodes);
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldApps != null) {
            onReleaseResources(oldApps);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override protected void onStartLoading() {
        if (mData != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(mData);
        }
        if (takeContentChanged() || mData == null) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override public void onCanceled(List<Episode> episodes) {
        super.onCanceled(episodes);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(episodes);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (mData != null) {
            mData = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(List<Episode> episodes) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }
}
