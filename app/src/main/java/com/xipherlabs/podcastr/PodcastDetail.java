package com.xipherlabs.podcastr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xipherlabs.podcastr.model.Podcast;

public class PodcastDetail extends AppCompatActivity {
    public static final String ARG_PODCAST = "podcast";
    public static final String FRAGMENT = "podcast_detail_fragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Added to Favorites", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Podcast podcast = (Podcast) getIntent().getExtras().getSerializable(ARG_PODCAST);
        setTitle(podcast.getName());
        PodcastDetailFragment podcastDetailsFragment = PodcastDetailFragment.newInstance(podcast);
        getSupportFragmentManager().beginTransaction().replace(R.id.podcast_details,podcastDetailsFragment, FRAGMENT).commit();
    }

}
