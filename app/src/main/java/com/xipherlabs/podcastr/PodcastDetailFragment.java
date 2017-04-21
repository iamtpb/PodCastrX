package com.xipherlabs.podcastr;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xipherlabs.podcastr.api.ApiClient;
import com.xipherlabs.podcastr.api.ApiInterface;
import com.xipherlabs.podcastr.model.Episode;
import com.xipherlabs.podcastr.model.Favorites;
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
public class PodcastDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Feed> {
    private static final String ARG_PODCAST = "podcast";
    private Podcast podcast;
    public Boolean isFavorite = false;
    RecyclerView mRecyclerView;
    //RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager linearLayoutManager;
    EpisodesAdapter adapter;
    //TextView tv_desc;
    ExpandableTextView etv_desc;
    FloatingActionButton fab;
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
            checkIfFav();
            getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_podcast_detail, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.detail_image);
        //tv_desc = (TextView) view.findViewById(R.id.detail_description);
        etv_desc = (ExpandableTextView) view.findViewById(R.id.detail_exp_desc);
        getActivity().setTitle(podcast.getName());
        String imageUrl = podcast.getThumb();
        Glide.with(getContext())
                .load(podcast.getThumb().replace("170x170","600x600"))
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(img);
        Log.d("DetailFrag",""+podcast.getId());
        final List<Episode> episodex = new ArrayList<>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.detail_episodes);
        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);

        fab = (FloatingActionButton) view.findViewById(R.id.fab_fav);
        if(isFavorite)
            fab.setImageResource(R.drawable.ic_favorite);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference(user.getUid()+"/favs");
                myRef.keepSynced(true);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Podcast> pods = new ArrayList<Podcast>();
                        if(dataSnapshot==null || dataSnapshot.getChildrenCount()==0){
                            pods.add(podcast);
                            myRef.setValue(pods);
                            fab.setImageResource(R.drawable.ic_favorite);
                            isFavorite=true;
                        }else{
                            if(isFavorite){
                                //remove from list
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    Podcast favPodcast = postSnapshot.getValue(Podcast.class);
                                    if(favPodcast.equals(podcast))
                                        continue;
                                    pods.add(favPodcast);
                                }
                                //TODO: See if you can check if successful before making changes
                                myRef.setValue(pods);
                                fab.setImageResource(R.drawable.ic_notfavorite);
                                isFavorite=false;
                            }else{
                                //add as favorite
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    Podcast favPodcast = postSnapshot.getValue(Podcast.class);
                                    pods.add(favPodcast);
                                }
                                pods.add(podcast);
                                myRef.setValue(pods);
                                fab.setImageResource(R.drawable.ic_favorite);
                                isFavorite=true;
                            }
                        }

                        //Log.d("Favorites",""+dataSnapshot.getValue(Favorites.class));
                        //podcasts exist. Check if this one is present or not.

                        //if present remove.

                        //if not present add.

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


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

    //Loader Stuff
    @Override public Loader<Feed> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader with no arguments, so it is simple.
        return new EpisodeLoader(getActivity(),podcast);
    }

    @Override public void onLoadFinished(Loader<Feed> loader, Feed data) {
        // Set the new data in the adapter.
        if(data==null){
            Toast.makeText(getContext(),"Error: Couldn't fetch Data from Server",Toast.LENGTH_SHORT).show();
            return;
        }
        if(data.getDescription()!=null)
            //tv_desc.setText(data.getDescription());
            etv_desc.setText(data.getDescription());
        if(data.getEpisodes()!=null)
            adapter = new EpisodesAdapter(data.getEpisodes());
        mRecyclerView.setAdapter(adapter);
    }

    @Override public void onLoaderReset(Loader<Feed> loader) {
        // Clear the data in the adapter.
        adapter = null;
    }

    public void checkIfFav(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(user.getUid()+"/favs");
        myRef.keepSynced(true);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null) {
                    isFavorite = false;
                    return;
                }
                //Fetch Favorites
                if(dataSnapshot.getChildrenCount()==0){
                    isFavorite = false;
                }else{
                    List<Podcast> pods = new ArrayList<Podcast>();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Podcast favPodcast = postSnapshot.getValue(Podcast.class);
                        if(favPodcast.equals(podcast)) {
                            isFavorite = true;
                            //Toast.makeText(getContext(), "is Favorite!", Toast.LENGTH_SHORT).show();
                            fab.setImageResource(R.drawable.ic_favorite);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: Add Clause
            }
        });
    }
}
