package com.xipherlabs.podcastr;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xipherlabs.podcastr.model.Episode;

import java.util.List;

/**
 * Created by xipher on 21/4/17.
 */

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>{
    private String[] mDataset;
    List<Episode> episodes;

    public static class EpisodeViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personAge;

        ImageView personPhoto;
        EpisodeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }

    }

    EpisodesAdapter(List<Episode> episodes){
        this.episodes = episodes;
    }

    @Override
    public EpisodeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.episode_item, viewGroup, false);
        EpisodeViewHolder pvh = new EpisodeViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(EpisodeViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(episodes.get(i).getTitle());
        personViewHolder.personAge.setText(episodes.get(i).getDescription().split("<")[0]);
        //personViewHolder.personPhoto.setImageResource(R.mipmap.ic_placeholder);
        Glide.with(personViewHolder.personPhoto.getContext())
                .load(episodes.get(i).getThumb())
                .skipMemoryCache(false)
                .thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(personViewHolder.personPhoto);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }
}
