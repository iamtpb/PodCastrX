package com.xipherlabs.podcastr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xipherlabs.podcastr.model.Podcast;

import java.util.ArrayList;
import java.util.List;

public class DiscoverAdapter extends
        RecyclerView.Adapter<DiscoverAdapter.ViewHolder> {

    private List<Podcast> mPodcasts;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public DiscoverAdapter(Context context, List<Podcast> podcasts) {
        mPodcasts = podcasts;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View podcastView = inflater.inflate(R.layout.podcast_discover_list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(podcastView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Podcast contact = mPodcasts.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.podcastNameView;
        textView.setText("HELLO");
        //textView.setText(contact.getName());
        ImageView imageView = holder.imageView;
        /*Glide.with(this)
                .load("IMAGE URL HERE")
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imagenotfound)
                .into(imageView);*/
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView podcastNameView;
        public ImageView imageView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            podcastNameView = (TextView) itemView.findViewById(R.id.podcast_name);
            imageView = (ImageView) itemView.findViewById(R.id.thumb);
        }

    }
    public void add(Podcast podcast){
        mPodcasts.add(podcast);
        notifyDataSetChanged();
    }
    public void reloadList(ArrayList<Podcast> mPodcasts) {
        this.mPodcasts.addAll(mPodcasts);
        notifyDataSetChanged();
    }
    public void printPods(){
        for(Podcast p: mPodcasts){
            Log.d("Podcast Stored:",""+p.getName());
        }
    }
}