package com.xipherlabs.podcastr;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xipherlabs.podcastr.model.Podcast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {
    static String TAG = "FavoritesFragment";
    private RecyclerView mMessageRecyclerView;
    private GridLayoutManager mGridLayoutManager;

    private FirebaseRecyclerAdapter<Podcast,FavoritesFragment.PodcastViewHolder> mFirebaseAdapter;
    private DatabaseReference mFirebaseDatabaseReference;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);

        String uid = user.getUid();

        mMessageRecyclerView = (RecyclerView) view.findViewById(R.id.favPodcastsRecyclerView);
        mGridLayoutManager = new GridLayoutManager(getContext(),2);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());
        mFirebaseDatabaseReference.keepSynced(true);
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Podcast, FavoritesFragment.PodcastViewHolder>(
                Podcast.class,
                R.layout.item_podcast,
                FavoritesFragment.PodcastViewHolder.class,
                mFirebaseDatabaseReference.child("favs")) {

            @Override
            protected Podcast parseSnapshot(DataSnapshot snapshot) {
                Podcast podcast = super.parseSnapshot(snapshot);
                if (podcast != null) {
                    //podcast.setId(snapshot.getKey());
                    //Log.d("PodcastSnap",""+snapshot.getValue());
                }
                return podcast;
            }

            @Override
            protected void populateViewHolder(final FavoritesFragment.PodcastViewHolder viewHolder,
                                              Podcast podcast, int position) {
                final Podcast mPodcast = podcast;

                viewHolder.podcastImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.d("____YYY____","Clicked?");
                        //Toast.makeText(getContext(),"Intent",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getContext(), PodcastDetail.class)
                                .putExtra(PodcastDetail.ARG_PODCAST, (Podcast) mPodcast);
                        startActivity(i);
                        //mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                });
                if (podcast.getName() != null) {
                    viewHolder.podcastImageView.setVisibility(ImageView.VISIBLE);
                    viewHolder.podcastIdTextView.setText(podcast.getId());
                    String imageUrl = podcast.getThumb();
                    Glide.with(viewHolder.podcastImageView.getContext())
                            .load(podcast.getThumb().replace("170x170","600x600"))
                            .skipMemoryCache(false)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(viewHolder.podcastImageView);

                }
            }
        };
        mMessageRecyclerView.setLayoutManager(mGridLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
        return view;
    }

    public static class PodcastViewHolder extends RecyclerView.ViewHolder{
        TextView podcastIdTextView;
        Podcast podcast;
        ImageView podcastImageView;
        View mView;
        Context mContext;
        public PodcastViewHolder(View v) {
            super(v);
            mView = v;
            mContext = v.getContext();
            podcastIdTextView = (TextView) itemView.findViewById(R.id.podcastId);
            podcastImageView = (ImageView) itemView.findViewById(R.id.podcastImageView);
        }

    }
}
