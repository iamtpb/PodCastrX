package com.xipherlabs.podcastr;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.xipherlabs.podcastr.model.Podcast;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {
    static String TAG = "DiscoverFragment";
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;

    private FirebaseRecyclerAdapter<Podcast, MessageViewHolder> mFirebaseAdapter;
    private DatabaseReference mFirebaseDatabaseReference;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public DiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment DiscoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, "param1");
        args.putString(ARG_PARAM2, "param2");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);
        String uid = user.getUid();
        /*
        Copy Starts Here
         */

        mMessageRecyclerView = (RecyclerView) view.findViewById(R.id.podcastsRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getContext());

        mGridLayoutManager = new GridLayoutManager(getContext(),2);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());

        mFirebaseDatabaseReference.keepSynced(true);
        FirebaseDatabase.getInstance().goOffline();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Podcast, MessageViewHolder>(
                Podcast.class,
                R.layout.item_podcast,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child("popular")) {

            @Override
            protected Podcast parseSnapshot(DataSnapshot snapshot) {
                Podcast friendlyMessage = super.parseSnapshot(snapshot);
                if (friendlyMessage != null) {
                    friendlyMessage.setId(snapshot.getKey());
                }
                return friendlyMessage;
            }

            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder,
                                              Podcast friendlyMessage, int position) {
                if (friendlyMessage.getName() != null) {
                    /*viewHolder.podcastTextView.setText(friendlyMessage.getName());
                    viewHolder.podcastTextView.setVisibility(TextView.VISIBLE);*/
                    viewHolder.podcastImageView.setVisibility(ImageView.VISIBLE);

                    String imageUrl = friendlyMessage.getThumb();
                    Glide.with(viewHolder.podcastImageView.getContext())
                            .load(friendlyMessage.getThumb().replace("170x170","600x600"))
                            .skipMemoryCache(false)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(viewHolder.podcastImageView);

                }


                //viewHolder.messengerTextView.setText(friendlyMessage.getName());
               /* if (friendlyMessage.getThumb() == null) {
                    viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(getContext(),
                            R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(DiscoverFragment.this)
                            .load(friendlyMessage.getThumb())
                            .into(viewHolder.podcastImageView);
                }*/

                /*if (friendlyMessage.getName() != null) {
                    // write this message to the on-device index
                    FirebaseAppIndex.getInstance().update(getMessageIndexable(friendlyMessage));
                }

                // log a view action on it
                FirebaseUserActions.getInstance().end(getMessageViewAction(friendlyMessage));*/
            }
        };


      /*  mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
               *//* if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }*//*
            }
        });
*/
        mMessageRecyclerView.setLayoutManager(mGridLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);



        //
        //
        //
        //
        //
        //
        /*
        String uid = user.getUid();
        if(uid==null){return view;}
        Log.d(TAG,"Moving Further");
        DatabaseReference userBranch = database.getReference(user.getUid()).child("popular");
        userBranch.keepSynced(true);
        Log.d(TAG,userBranch.getKey());
        final ArrayList<Podcast> podcasts = new ArrayList<>();
        RecyclerView rvPodcasts = (RecyclerView) view.findViewById(R.id.discover_list);
        rvPodcasts.setLayoutManager(new LinearLayoutManager(getContext()));
        // Initialize contacts
        //podcasts = Contact.createContactsList(20);
        // Create adapter passing in the sample user data
        final DiscoverAdapter adapter = new DiscoverAdapter(getContext(), podcasts);
        podcasts.add(new Podcast("asd","asd","asd","asd"));
        rvPodcasts.setAdapter(adapter);
        userBranch.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Podcast podcast = dataSnapshot.getValue(Podcast.class);
                //podcasts.add(podcast);
                adapter.add(podcast);
                adapter.notifyDataSetChanged();

                Log.d("DiscoveredPodcasts",""+podcast.getName());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Podcast podcast = dataSnapshot.getValue(Podcast.class);
                //podcasts.remove(arrayAdapter.getItem(Integer.parseInt(dataSnapshot.getKey())));
                podcasts.add(podcast);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       *//* userBranch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Podcast> podcasts = (ArrayList<Podcast>) dataSnapshot.getValue();
                Toast.makeText(getContext(),"Changed Count: "+dataSnapshot.getChildrenCount(),Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(),"Pod0: "+podcasts.get(0).getName(),Toast.LENGTH_LONG).show();

                //values = new ArrayList<String>();
                *//**//*values.add(podcasts.get(0).getName());
                values.add(podcasts.get(1).getName());
                values.add(podcasts.get(2).getName());*//**//*
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Count: "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });*//*

        // Attach the adapter to the recyclerview to populate items

        adapter.notifyDataSetChanged();
        adapter.printPods();
        Log.d("Items:","no: "+adapter.getItemCount());
        // Set layout manager to position the items

*/
        return view;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        //TextView podcastTextView;
        ImageView podcastImageView;

        public MessageViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"Clicked: "+getAdapterPosition(),Toast.LENGTH_LONG).show();
                }
            });
            //podcastTextView = (TextView) itemView.findViewById(R.id.podcastTextView);
            podcastImageView = (ImageView) itemView.findViewById(R.id.podcastImageView);

        }
    }


}
