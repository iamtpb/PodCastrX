package com.xipherlabs.podcastr;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NowPlaying#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NowPlaying extends Fragment{
    MediaPlayer mMediaPlayer;
    //AudioService mService;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Activity mActivity;
    OnFragmentInteractionListener mListener;
    ImageView playButton;

    public NowPlaying() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NowPlaying newInstance() {
        /*NowPlaying fragment = new NowPlaying();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, "");
        args.putString(ARG_PARAM2, "");
        fragment.setArguments(args);
        return fragment;*/
        NowPlaying fragment = new NowPlaying();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
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
       // View view =inflater.inflate(R.layout.fragment_now_playing, container, false);

        /*
        JcPlayerView jcplayerView = (JcPlayerView) view.findViewById(R.id.jcplayer);
        ArrayList<JcAudio> jcAudios = new ArrayList<>();
        jcAudios.add(JcAudio.createFromURL("url audio","https://audioboom.com/posts/5817862-q-a-with-payne-lindsey-and-philip-holloway-04-13-17.mp3?source=rss&stitched=1"));
        jcplayerView.initAnonPlaylist(jcAudios);
        jcplayerView.createNotification();*/
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_music_player, container, false);

        playButton = (ImageView)view.findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.togglePlayButton();
            }
        });

        view.findViewById(R.id.rewind).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mListener.startSeek();
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    mListener.stopSeekBack();
                }
                return false;
            }
        });

        view.findViewById(R.id.fast_forward).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mListener.startSeek();
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    mListener.stopSeekForward();
                }
                return false;
            }
        });

        view.findViewById(R.id.quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.handleFinish();
            }
        });

        //S
        return  view;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentMusicPlayer.
     */
/*

    public static NowPlaying newInstance() {
        FragmentMusicPlayerUI fragment = new FragmentMusicPlayerUI();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mActivity = (Activity)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mActivity = null;
    }

    public void populateSongInfo(String title, String artist, String coverImage) {
        ((TextView)mActivity.findViewById(R.id.title)).setText(title);
        ((TextView)mActivity.findViewById(R.id.artist)).setText(artist);
       // ((ImageView)mActivity.findViewById(R.id.cover_image)).setImageURI(Uri.parse(coverImage));
    }

    public void hidePlayerControls() {
        mActivity.findViewById(R.id.preparing_progress).setVisibility(View.VISIBLE);
        mActivity.findViewById(R.id.player_controls).setVisibility(View.GONE);
    }

    public void showPlayerControls() {
        mActivity.findViewById(R.id.preparing_progress).setVisibility(View.GONE);
        mActivity.findViewById(R.id.player_controls).setVisibility(View.VISIBLE);
    }

    public void setPlayButton() {
        playButton.setImageResource(R.drawable.media_playback_start);
    }

    public void setPauseButton() {
        playButton.setImageResource(R.drawable.media_playback_pause);
    }

    public interface OnFragmentInteractionListener {
        void handleNetworkError();
        void handleFinish();
        void togglePlayButton();
        void startSeek();
        void stopSeekForward();
        void stopSeekBack();
    }


}
