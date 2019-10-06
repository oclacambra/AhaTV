package com.octaltakeoff.ahatv.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.octaltakeoff.ahatv.R;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlaybackActivity extends AppCompatActivity {

    @BindView(R.id.root)
    LinearLayout root;

    @BindView(R.id.topLayout)
    FrameLayout topLayout;

    @BindView(R.id.bottomLayout)
    FrameLayout bottomLayout;

    @BindView(R.id.player_view)
    PlayerView playerView;

    @BindView(R.id.noVideoIv)
    ImageView noVideo;

    @BindView(R.id.nextVideoBtn)
    FloatingActionButton nextVideo;

    @BindView(R.id.prevVideoBtn)
    FloatingActionButton prevVideo;

    @BindView(R.id.currentStep)
    TextView currentStep;

    private SimpleExoPlayer mExoPlayer;

    private Integer position;


    private Integer numberOfSteps;

    private boolean isTablet;

    private Long exoplayer_position;

    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;
    private boolean isConnected;
    Handler mainHandler;

    int width = 0;
    int height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playback_layout);

        ButterKnife.bind(this);
        mainHandler = new Handler();



        initializePlayer();


    }

    private void initializeVideoPlayer() {

        String videoUrl = "https://drive.google.com/open?id=1y1-_AhDlT5dt15tSbZdzQFZdBW23OEos";
        //String videoUrl = "http://playertest.longtailvideo.com/adaptive/bbbfull/bbbfull.m3u8";
        //String videoUrl = "https://nl2.streamlive.to/vlc/rn4lwbfvamx7szx/playlist.m3u8?wmsAuthSign=c2VydmVyX3RpbWU9OS81LzIwMTkgNzoxOTozOSBQTSZoYXNoX3ZhbHVlPStZU1o0VEV5TUhWMnRvRGdCYm9IZnc9PSZ2YWxpZG1pbnV0ZXM9NjAmc3RybV9sZW49MTkmaWQ9MA==";
        //String videoUrl = "https://drive.google.com/uc?export=view&id=1y1-_AhDlT5dt15tSbZdzQFZdBW23OEos";
        //String videoUrl = "https://nl2.streamlive.to/vlc/5uon546q8gjsaor/playlist.m3u8?wmsAuthSign=c2VydmVyX3RpbWU9OS81LzIwMTkgNzo1Njo0NiBQTSZoYXNoX3ZhbHVlPTk3MUdzb2RFR0Y0Ky9wNUpISW01bmc9PSZ2YWxpZG1pbnV0ZXM9NjAmc3RybV9sZW49MTkmaWQ9MA==";
//        if (!videoUrl.isEmpty()) {
//            //noVideo.setVisibility(View.GONE);
//            //playerView.setVisibility(View.VISIBLE);
//            initializePlayer(Uri.parse(videoUrl));
//        } else {
//            //noVideo.setVisibility(View.VISIBLE);
//            //playerView.setVisibility(View.GONE);
//        }
    }

    private void initializePlayer() {
//        if (mExoPlayer == null) {
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        //String filePath = Environment.getExternalStorageDirectory() + File.separator + "video" + File.separator + "20190905_225532.mp4";

        //Uri uri = Uri.parse("https://drive.google.com/uc?export=view&id=1y1-_AhDlT5dt15tSbZdzQFZdBW23OEos");
        //Uri uri = Uri.parse("https://nl2.streamlive.to/vlc/rn4lwbfvamx7szx/playlist.m3u8");
        //Uri uri = Uri.parse("https://nl2.streamlive.to/vlc/91dwwk48w3x0bki/playlist.m3u8?wmsAuthSign=c2VydmVyX3RpbWU9OS81LzIwMTkgNzoyODozMiBQTSZoYXNoX3ZhbHVlPXVXbHZNczRScHpPaWNXcVk3bUxXWHc9PSZ2YWxpZG1pbnV0ZXM9NjAmc3RybV9sZW49MTkmaWQ9MA==");
        // Uri uri = Uri.parse("https://drive.google.com/open?id=1y1-_AhDlT5dt15tSbZdzQFZdBW23OEos");
        //Uri uri = Uri.parse(" https://nl2.streamlive.to/vlc/5uon546q8gjsaor/playlist.m3u8?wmsAuthSign=c2VydmVyX3RpbWU9OS81LzIwMTkgNzo1Njo0NiBQTSZoYXNoX3ZhbHVlPTk3MUdzb2RFR0Y0Ky9wNUpISW01bmc9PSZ2YWxpZG1pbnV0ZXM9NjAmc3RybV9sZW49MTkmaWQ9MA==");
        Uri uri = Uri.parse("https://dai2.xumo.com/amagi_hls_data_xumo1212A-adventuresportsnetwork/CDN/768x432_1200000/index.m3u8");




        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                this, Util.getUserAgent(this, "ahatv"));


        // for regular videos mp4
        ExtractorMediaSource videoSource = new ExtractorMediaSource
                .Factory(dataSourceFactory)
                .createMediaSource(uri);



        // for live streaming videos
       HlsMediaSource hlsMediaSource = new HlsMediaSource
               .Factory(dataSourceFactory)
               .createMediaSource(uri);

        //mExoPlayer.prepare(videoSource);
        mExoPlayer.prepare(hlsMediaSource);
        playerView.setPlayer(mExoPlayer);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        mExoPlayer.setPlayWhenReady(true);


    }

//    private void initPlayer(){
//        Handler handler = new Handler();
//
//        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder()
//                .setEventLsitener(mainHandler, bandWidthMeterEventListener)
//                .build();
//


 //   }

    @Override
    protected void onStart() {
        super.onStart();

        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        hideSystemUI();
        if((Util.SDK_INT <=23 || mExoPlayer == null)){
            initializePlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(Util.SDK_INT > 23) {
            releasePlayer();
        }

    }

    @SuppressLint("InLinedApi")
    private void hideSystemUI(){
        playerView.setSystemUiVisibility (View.SYSTEM_UI_FLAG_LOW_PROFILE
        | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }
    private  void releasePlayer(){
        long playbackPosition = 0;
        boolean playerWhenReady = true;
        int currentWindow = 0;
        if(mExoPlayer != null){
            playbackPosition = mExoPlayer.getCurrentPosition();
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            playerWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }



}
