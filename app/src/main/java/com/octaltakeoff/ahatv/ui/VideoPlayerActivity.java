package com.octaltakeoff.ahatv.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.octaltakeoff.ahatv.R;
import com.octaltakeoff.ahatv.adapter.ChannelListAdapter;
import com.octaltakeoff.ahatv.adapter.ChannelListAdapterPlayback;
import com.octaltakeoff.ahatv.model.Channel;
import com.octaltakeoff.ahatv.utils.ColumnUtils;
import com.octaltakeoff.ahatv.utils.NetworkConnectionUtils;
import com.octaltakeoff.ahatv.utils.NetworkUtils;
import com.octaltakeoff.ahatv.utils.VideoPlayerConfig;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayerActivity extends AppCompatActivity implements Player.EventListener, ChannelListAdapterPlayback.ItemClickListener{

    // Listener for Recyclerview
    public interface Callback {
        void onNewChannelSelected(int position);
    }

    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.topLayout)
    FrameLayout topLayout;
    @BindView(R.id.bottomLayout)
    FrameLayout bottomLayout;
    @BindView(R.id.player_view)
    PlayerView simpleExoPlayerView;
    @BindView(R.id.noVideoIv)
    ImageView noVideo;
    @BindView(R.id.iv_tvPoster_details)
    ImageView tvPosterView;
    @BindView(R.id.tv_title_details)
    TextView tvTitleTextView;
    @BindView(R.id.tv_category_details)
    TextView tvCategoryTextView;
    @BindView(R.id.tv_region_details)
    TextView tvRegionTextView;
    @BindView(R.id.nextVideoBtn)
    ImageView nextVideo;
    @BindView(R.id.prevVideoBtn)
    ImageView prevVideo;
    @BindView(R.id.currentStep)
    TextView currentStep;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.iv_sofilogo)
    ImageView ivSofiLogo;
    @BindView(R.id.tv_scrim)
    TextView tvScrim;

    @BindView(R.id.toolbar2)
    Toolbar toolbar;

    @BindView(R.id.recycleView_video_playback)
    RecyclerView recyclerViewVideoPlayback;


   // private String hlsVideoUri = "https://drive.google.com/uc?export=view&id=1vw7ReojX26cGEl_fPGkxuRRW1MiX6TH8";
    private String hlsVideoUri = "https://drive.google.com/uc?export=1y1-AhDlT5dt15tSbZdzQFZdBW23OEos";
   //private String hlsVideoUri = "https://drive.google.com/open?id=1y1-AhDlT5dt15tSbZdzQFZdBW23OEos";


    private SimpleExoPlayer player;
    private String selectedChannelTitle, selectedGChannelCategory, selectedVideoUrl,
            selectedChannelRegion, selectedImageUrl;

    private Channel channel;
    private ArrayList<Channel> channels = new ArrayList<>();
    private int position = 0;
    private int numberOfChannels;
    private int exoplayer_position;
    private RecyclerView gridRecyclerView;
    Context context;
    private static final DefaultBandwidthMeter BANDWIDTH_METER =
            new DefaultBandwidthMeter();

    private int selectedPos = RecyclerView.NO_POSITION;
    boolean isConnected;
    boolean isExoPlayerPlaying;
    private AdView mAdview;

   // private static final String PLAYER_POSITION = "exoplayer_position";
    public static final String CHANNEL_SENT = "channel_sent_videoplayer_activity";
    public static final String CHANNEL_CLICKED = "channel_clicked_video_palyer_activity";
    public static final String NUMBER_OF_CHANNELS = "number_of_channels";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar); // Set the theme for no action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playback_layout);

        int orientation = getResources().getConfiguration().orientation;
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Display the home button

        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            if(Build.VERSION.PREVIEW_SDK_INT < 16){
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
           toolbar.setBackgroundColor(Color.TRANSPARENT);
        }else{
            toolbar.setBackgroundColor(Color.parseColor("#E64A19"));
        }

        ButterKnife.bind(this);

        //Received the intent
        Intent receivedIntent = getIntent();
        channels = receivedIntent.getParcelableArrayListExtra(CHANNEL_SENT);
        position = receivedIntent.getIntExtra(CHANNEL_CLICKED, 0);
        numberOfChannels = receivedIntent.getIntExtra(NUMBER_OF_CHANNELS, 1);

        //  Admob implementation
        MobileAds.initialize(this, "ca-app-pub-5138732050544010~6527615257");
        mAdview= findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

    }

    @Override
    protected void onStart() {
        super.onStart();

        initPlayActivity(position);

        // Set visibility of next and previous button
        setButtonControls(position);

        prevVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.release();
                if(position == 0){
                    position = 0;
                    prevVideo.setVisibility(View.INVISIBLE);

                }else{
                    prevVideo.setVisibility(View.VISIBLE);
                    position--;
                    nextVideo.setVisibility(View.VISIBLE);

                }
                initPlayActivity(position);
            }
        });

        nextVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.release();
                if (position == channels.size()-1){
                    position = channels.size()-1;
                    nextVideo.setVisibility(View.INVISIBLE);

                }else{
                    nextVideo.setVisibility(View.VISIBLE);
                    position++;
                    prevVideo.setVisibility(View.VISIBLE);
                }

                initPlayActivity(position);
            }
        });

    }

    private void setButtonControls(int position){
        prevVideo.setVisibility(View.VISIBLE);
        nextVideo.setVisibility(View.VISIBLE);
        if (position == 0){
            prevVideo.setVisibility(View.INVISIBLE);
            nextVideo.setVisibility(View.VISIBLE);
        } else if (position == channels.size() - 1){
            nextVideo.setVisibility(View.INVISIBLE);
            prevVideo.setVisibility(View.VISIBLE);
        }
    }

    private void initPlayActivity(final int position) {

        channel = null;

        channel = channels.get(position);
        selectedChannelTitle = channel.getTitle();
        selectedGChannelCategory = channel.getCategory();
        selectedVideoUrl = channel.getVideoUrl();
        selectedChannelRegion = channel.getRegion();
        selectedImageUrl = channel.getImageUrl();

        setTitle(selectedChannelTitle);

        //1. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl(
                new DefaultAllocator(true, 16),
                VideoPlayerConfig.MIN_BUFFER_DURATION,
                VideoPlayerConfig.MAX_BUFFER_DURATION,
                VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER, -1, true);

        // 2. Create a default TrackSelector A factory to create an AdaptiveVideoTrackSelection
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);


        // 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this), trackSelector, loadControl);


        simpleExoPlayerView = findViewById(R.id.player_view);
        simpleExoPlayerView.setPlayer(player);

        // 4. The media source
        MediaSource mediaSource = buildMediaSource(Uri.parse(selectedVideoUrl));

        player.addListener(this);
        simpleExoPlayerView.requestFocus();
        //player.seekTo(position);
        player.setPlayWhenReady(true);


        player.prepare(mediaSource, true, false);


        progressBar = findViewById(R.id.progressBar);

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.sofilogo);
        Glide.with(this)
                .setDefaultRequestOptions(options)
                .load(selectedImageUrl)
                .into(tvPosterView);

        tvTitleTextView.setText(selectedChannelTitle);
        tvCategoryTextView.setText(selectedGChannelCategory);
        tvRegionTextView.setText(selectedChannelRegion);

        initRecyclerView();


    }

    private void initializePlayerMp4() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(),
                new DefaultLoadControl()
        );
        simpleExoPlayerView.setPlayer(player);
       // player.setPlayWhenReady(playWhenReady);
        //player.seekTo(currentWindow, playbackPosition);
    }

    private MediaSource buildMediaSource(Uri uri) {
        String lastPath = uri.getLastPathSegment();
        String userAgent = "ahatv";
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, userAgent), BANDWIDTH_METER);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);

//        return new ExtractorMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(uri);

        //DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        //DefaultHttpDataSourceFactory defaultHttpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent);

        if (lastPath.contains("mp3") || lastPath.contains("mp4")) {
            return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER))
                .createMediaSource(uri);

        }else if (lastPath.contains("m3u8")){
            //DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
            return new HlsMediaSource.Factory(
                    //new DefaultDataSourceFactory(this, Util.getUserAgent(this,userAgent), BANDWIDTH_METER))
                    //new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER))
                    new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER))
                    .createMediaSource(uri);
        }else{
            //DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
            /**
            DataSource.Factory manifestDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent);
            DashChunkSource.Factory dashChunkSourceFactory = new DefaultDashChunkSource.Factory(
                    new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER));
            return new DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory)
                    .createMediaSource(uri);
             */

            return new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER))
                    .createMediaSource(uri);
        }

    }



    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {


    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        switch (playbackState) {
            case Player.STATE_BUFFERING:
                //You can use progress dialog to show user that video is preparing or buffering so please wait
                progressBar.setVisibility(View.VISIBLE);
                noVideo.setVisibility(View.GONE);
                break;
            case Player.STATE_IDLE:
                //idle state
                break;
            case Player.STATE_READY:
                // dismiss your dialog here because our video is ready to play now
                progressBar.setVisibility(View.GONE);
                noVideo.setVisibility(View.GONE);
                // Disable the screen time off
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                break;
            case Player.STATE_ENDED:
                // do your processing after ending of video

                break;

        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

        // Alert  dailog for error warning
//        AlertDialog.Builder adb = new AlertDialog.Builder(VideoPlayerActivity.this);
//        adb.setTitle("Could not able to stream video");
//        adb.setMessage("It seems that something is going wrong.\nPlease try again.");
//        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
//                finish(); // take out user from this activity. you can skip this
//            }
//        });
//        AlertDialog ad = adb.create();
//        ad.show();
        noVideo.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        isExoPlayerPlaying = false;



    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {


    }

    @Override
    protected void onStop() {
        super.onStop();
        simpleExoPlayerView.setPlayer(null);
        player.release();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (player != null) {
            player.setPlayWhenReady(false); //to pause a video because now our video player is not in focus
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Go back to last view with transition support
            supportFinishAfterTransition();
            player.release();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        // Go to last view
        onBackPressed();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int player_position = position;
        outState.putInt("PLAYER_POSITION", player_position);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("PLAYER_POSITION", 0);
        player.release();
        initPlayActivity(position);
        setButtonControls(position);
    }

    private void initRecyclerView() {
        //Log.d(TAG, getString(R.string.init_message_log));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVideoPlayback.setLayoutManager(layoutManager);
        ChannelListAdapterPlayback channelListAdapterPlayback = new ChannelListAdapterPlayback(this, channels, this);
        recyclerViewVideoPlayback.setAdapter(channelListAdapterPlayback);

        // Keep the position of the list from where it was left, clicked item must be at the center or the first item
        int mNoOfColumns = ColumnUtils.calculateNoOfColumns2(this);
        //recyclerViewVideoPlayback.scrollToPosition(position - (mNoOfColumns/3)); // center
        recyclerViewVideoPlayback.scrollToPosition(position); // first

        // The the color of selected channel
        //holder.itemView.setBackgroundColor(selected_position == position ? Color.WHITE : Color.TRANSPARENT);


    }

    @Override
    public void onItemClick(View v, int mPosition) {
        player.release();
        position = mPosition;
        initPlayActivity(position);
        setButtonControls(position);

        //simpleExoPlayerView.setPlayer(null);
        //initVLCPlayer(position);




//        Intent intent = new Intent(this, VideoPlayerActivity.class);
//        intent.putParcelableArrayListExtra(VideoPlayerActivity.CHANNEL_SENT, new ArrayList<>(channels));
//        intent.putExtra(VideoPlayerActivity.CHANNEL_CLICKED, position);
//        startActivity(intent);

    }

    public void initVLCPlayer(int position){

        selectedVideoUrl = channels.get(position).getVideoUrl();
        selectedChannelTitle = channels.get(position).getTitle();
        isConnected = NetworkConnectionUtils.isNetworkConnection(this);

        int vlcRequestCode = 42;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage("org.videolan.vlc");
        intent.setDataAndTypeAndNormalize(Uri.parse(selectedVideoUrl), "video/*");
        intent.putExtra("title", selectedChannelTitle);
        intent.putExtra("from_start", false);
        intent.putExtra("position", 900001);

        if(isConnected){

            if(intent.resolveActivity(getBaseContext().getPackageManager())!= null){
                startActivityForResult(intent, vlcRequestCode);
            } else{
                Toast.makeText(VideoPlayerActivity.this, "Please install VLC Player for Android", Toast.LENGTH_SHORT).show();
                return;
            }


        } else {
            Toast.makeText(VideoPlayerActivity.this, "No internet connection!. Please connect your device to internet.", Toast.LENGTH_SHORT).show();
            return;
        }








        /**
        String webString = null;
        String titleString = selectedChannelTitle;

        String initialWebString = selectedChannelTitle;
        initialWebString.replaceAll("\\s", "_");
        String finalWebString = initialWebString;
        finalWebString.replaceAll("'", "%27s");
        webString = "https://en.wikipedia.org/wiki/" + finalWebString;

        // Set the intent to go to webActivity
        if (TextUtils.isEmpty(selectedChannelTitle)) {
            Toast.makeText(VideoPlayerActivity.this, selectedChannelTitle, Toast.LENGTH_SHORT).show();
            return;
        }
        // Set the intent and variables to pass
        Intent intent = new Intent(VideoPlayerActivity.this, VLCWebViewActivity.class);
        intent.putExtra("WebString", webString);
        intent.putExtra("TitleString", titleString);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) VideoPlayerActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            if (intent.resolveActivity(VideoPlayerActivity.this.getPackageManager()) != null) {
                startActivity(intent);

            } else {
                Toast.makeText(VideoPlayerActivity.this, "Please install VLC Player", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(VideoPlayerActivity.this, "No internet", Toast.LENGTH_SHORT).show();
            return;
        }
        */
    }

}
