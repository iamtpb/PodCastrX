package com.xipherlabs.podcastr;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        if(podcast!=null){
            Toast.makeText(getContext(),"Podcast "+podcast.getName(),Toast.LENGTH_LONG).show();
        }
        Toast.makeText(getContext(),"Did it!",Toast.LENGTH_LONG).show();
        return view;
    }
}
