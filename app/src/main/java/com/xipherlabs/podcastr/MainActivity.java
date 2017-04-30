package com.xipherlabs.podcastr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xipherlabs.podcastr.api.ApiClient;
import com.xipherlabs.podcastr.api.ApiInterface;
import com.xipherlabs.podcastr.model.Episode;
import com.xipherlabs.podcastr.model.Feed;
import com.xipherlabs.podcastr.model.MusicController;
import com.xipherlabs.podcastr.model.Podcast;
import com.xipherlabs.podcastr.model.PopularPodcasts;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public View bottomSheet = null;

    BottomSheetBehavior mBottomSheetBehavior1;
    Button mButton1;
    Fragment fragment = null;
    Class fragmentClass = DiscoverFragment.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        Utils.initFirebasePersistence();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Init Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    syncPodcasts();
                    //Toast.makeText(getApplicationContext(),"Signed in", Toast.LENGTH_LONG).show();
                    Log.d("FirebaseAuth", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Toast.makeText(getApplicationContext(),"Not Signed in", Toast.LENGTH_LONG).show();
                    Log.d("FirebaseAuth", "onAuthStateChanged:signed_out");
                }
            }
        };
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragment!=null){
            fragmentManager.beginTransaction().replace(R.id.content_pane, fragment).commit();
        }
        fragmentManager.beginTransaction().replace(R.id.content_pane, (Fragment) DiscoverFragment.newInstance()).commit();

        //Bottom Sheet Player stuff?
        View bottomSheet = findViewById(R.id.bottom_sheet1);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior1.setHideable(true);
        mBottomSheetBehavior1.setPeekHeight(20);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);
       /*  mButton1 = (Button) findViewById(R.id.button1);
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                    mButton1.setText("XXX");
                }
                else {
                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    mButton1.setText("_X_");
                }
            }
        });
*/
        /*JcPlayerView jcplayerView = (JcPlayerView) findViewById(R.id.jcp);

        jcplayerView.initAnonPlaylist(jcAudios);
        jcplayerView.createNotification();*/

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Fragment fragment = null;
        //Class fragmentClass = DiscoverFragment.class;
        boolean audioPlay = false;
        if (id == R.id.nav_discover) {
            fragmentClass = DiscoverFragment.class;
        } else if (id == R.id.nav_favorites) {
            fragmentClass = FavoritesFragment.class;
        } else if (id == R.id.nav_nowplaying) {
            if(!audioPlay) {
                //handleVolley();
                //call service.
                /*Intent intent = new Intent(getApplicationContext(), ServiceMusicPlayer.class);
                intent.putExtra("media_file", url);
                intent.putExtra("duration", 100);
                startService(intent);*/
                Intent intent = new Intent(this,AudioActivity.class);
                startActivity(intent);
                fragmentClass = NowPlaying.class;
            }else{
                fragmentClass = DiscoverFragment.class;
                Toast.makeText(this, "No Playback.", Toast.LENGTH_SHORT).show();
            }
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_pane, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
//        controller.hide();
    }

    public void syncPodcasts(){
        if(!isNetworkAvailable())
            return;
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<PopularPodcasts> call = apiService.getPopularPodcasts();
        call.enqueue(new Callback<PopularPodcasts>() {
            @Override
            public void onResponse(Call<PopularPodcasts> call, Response<PopularPodcasts> response) {
                int statusCode = response.code();
                PopularPodcasts popularPodcasts = response.body();
                //Toast.makeText(getApplicationContext(),"Podcasts:"+popularPodcasts.getTotal()+" \n",Toast.LENGTH_LONG).show();
                int x = popularPodcasts.getTotal();
                List<Podcast> podcasts = popularPodcasts.getPodcasts();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference myRef = database.getReference(user.getUid());
                myRef.keepSynced(true);
                myRef.child("popular").setValue(podcasts);
            }

            @Override
            public void onFailure(Call<PopularPodcasts> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error updating Firebase Backend", Toast.LENGTH_LONG).show();
            }
        });
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
