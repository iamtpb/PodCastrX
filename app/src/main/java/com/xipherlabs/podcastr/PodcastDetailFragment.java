package com.xipherlabs.podcastr;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class PodcastDetailFragment extends Fragment {
    private static final String ARG_PODCAST = "podcast";
    private Podcast podcast;
    public PodcastDetailFragment() {
    }

    public static PodcastDetailFragment newInstance(Podcast podcast) {
        PodcastDetailFragment fragment = new PodcastDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PODCAST, podcast);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            podcast = (Podcast) getArguments().getSerializable(ARG_PODCAST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_podcast_detail, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.detail_image);
        final TextView tv_desc = (TextView) view.findViewById(R.id.detail_description);
        getActivity().setTitle(podcast.getName());
        String imageUrl = podcast.getThumb();
        Glide.with(getContext())
                .load(podcast.getThumb().replace("170x170","600x600"))
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(img);
        Log.d("DetailFrag",""+podcast.getId());
        //
        final List<Episode> episodex = new ArrayList<>();

        //
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Feed> call = apiService.getFeed(podcast.getId());
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                int statusCode = response.code();
                Feed feed = response.body();
                Toast.makeText(getContext(),"Podcast:"+feed.getName()+" \n",Toast.LENGTH_LONG).show();
                String x = feed.getTotal();
                tv_desc.setText(feed.getDescription());
                List<Episode> episodes = feed.getEpisodes();
                Log.d("Feed",""+x);
                for(Episode e : episodes) {
                    episodex.add(e);
                    Log.d("Episode", "" + e.getTitle());
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                //tv.setText(t.getMessage());
                Toast.makeText(getContext(),"Error fetching Feed: "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //
        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager linearLayoutManager;
        mRecyclerView = (RecyclerView) view.findViewById(R.id.detail_episodes);
        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        EpisodesAdapter adapter = new EpisodesAdapter(episodex);
        mRecyclerView.setAdapter(adapter);
        Log.d("Adapter",""+adapter.episodes);


        //
        return view;
    }


    public void getFeed(final Context context){
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Feed> call = apiService.getFeed("");
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                int statusCode = response.code();
                Feed feed = response.body();
                List<Episode> episodes = feed.getEpisodes();
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {

                Toast.makeText(context,"Error fetching Feed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
