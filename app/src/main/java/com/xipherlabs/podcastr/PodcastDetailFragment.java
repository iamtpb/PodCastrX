package com.xipherlabs.podcastr;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xipherlabs.podcastr.model.Podcast;

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
        //Tost.makeText(getContext(),"Podcast:"+podcast.getName(),Toast.LENGTH_LONG).show();
        //Toast.makeText(getContext(),"Did it!",Toast.LENGTH_LONG).show();

        ImageView img = (ImageView) view.findViewById(R.id.detail_image);
        ImageView img1 = (ImageView) view.findViewById(R.id.detail_image1);
        ImageView img2 = (ImageView) view.findViewById(R.id.detail_image2);
        ImageView img3 = (ImageView) view.findViewById(R.id.detail_image3);
        ImageView img4 = (ImageView) view.findViewById(R.id.detail_image4);

        String imageUrl = podcast.getThumb();
        Glide.with(getContext())
                .load(podcast.getThumb().replace("170x170","600x600"))
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);
        Glide.with(getContext())
                .load(podcast.getThumb().replace("170x170","600x600"))
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img1);
        Glide.with(getContext())
                .load(podcast.getThumb().replace("170x170","600x600"))
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img2);
        Glide.with(getContext())
                .load(podcast.getThumb())
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img3);
        Glide.with(getContext())
                .load(podcast.getThumb().replace("170x170","200X200"))
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img4);
        return view;
    }
}
