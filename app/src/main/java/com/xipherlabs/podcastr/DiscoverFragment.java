package com.xipherlabs.podcastr;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ValueEventListener;
import com.xipherlabs.podcastr.model.Podcast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {
    static String TAG = "DiscoverFragment";
    private RecyclerView mMessageRecyclerView;
    private GridLayoutManager mGridLayoutManager;

    private FirebaseRecyclerAdapter<Podcast, PodcastViewHolder> mFirebaseAdapter;
    private DatabaseReference mFirebaseDatabaseReference;
    private View bottomSheet;
    static private BottomSheetBehavior mBottomSheetBehavior = null;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment DiscoverFragment.
     */
    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);


        bottomSheet = view.findViewById( R.id.bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        mBottomSheetBehavior.setPeekHeight(0);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        String uid = user.getUid();

        mMessageRecyclerView = (RecyclerView) view.findViewById(R.id.podcastsRecyclerView);
        mGridLayoutManager = new GridLayoutManager(getContext(),2);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());
        mFirebaseDatabaseReference.keepSynced(true);
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Podcast, PodcastViewHolder>(
                Podcast.class,
                R.layout.item_podcast,
                PodcastViewHolder.class,
                mFirebaseDatabaseReference.child("popular")) {

            @Override
            protected Podcast parseSnapshot(DataSnapshot snapshot) {
                Podcast podcast = super.parseSnapshot(snapshot);
                if (podcast != null) {
                    podcast.setId(snapshot.getKey());
                    Log.d("PodcastSnap",""+snapshot.getValue());
                }
                return podcast;
            }

            @Override
            protected void populateViewHolder(final PodcastViewHolder viewHolder,
                                              Podcast podcast, int position) {
                final Podcast mPodcast = podcast;

                viewHolder.podcastImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("____YYY____","Clicked?");
                        Toast.makeText(getContext(),"Intent",Toast.LENGTH_LONG).show();
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
