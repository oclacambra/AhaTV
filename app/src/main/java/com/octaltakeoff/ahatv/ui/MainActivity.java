package com.octaltakeoff.ahatv.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.octaltakeoff.ahatv.R;
import com.octaltakeoff.ahatv.adapter.SectionsPageAdapter;
import com.octaltakeoff.ahatv.utils.NetworkConnectionUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager viewPager;
    private Integer viewPagerId;
    Integer mViewPagerId;
    private ImageView noInternetView;

    private Parcelable state;

    private CustomViewPagerChannel customViewPagerChannel;
    private CustomViewPagerMovies customViewPagerMovies;
    //private CustomViewPagerNews customViewPagerNews;
    private CustomViewPagerAbout customViewPagerAbout;
    private TabLayout tabLayout;
    private Toolbar mToolbar;
    private TextView stripTextView;
    private ProgressBar progressBar;
    private CollapsingToolbarLayout collapsingToolbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean isConnected;
    private AdView mAdView;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            tabLayout.setupWithViewPager(null);
            //customViewPager.getAdapter().notifyDataSetChanged();

            if(isConnected) {
                switch (item.getItemId()) {
                    case R.id.navigation_channels:
                        customViewPagerMovies.setVisibility(View.GONE);
                        customViewPagerAbout.setVisibility(View.GONE);
                        customViewPagerChannel.setVisibility(View.VISIBLE);

                        setupViewPager(customViewPagerChannel);
                        tabLayout.setupWithViewPager(customViewPagerChannel);

                        viewPagerId = 0;

                        return true;
                    case R.id.navigation_movieTrailers:
                        customViewPagerChannel.setVisibility(View.GONE);
                        customViewPagerAbout.setVisibility(View.GONE);
                        customViewPagerMovies.setVisibility(View.VISIBLE);

                        setupViewPagerMovies(customViewPagerMovies);
                        tabLayout.setupWithViewPager(customViewPagerMovies);

                        viewPagerId = 1;

                        return true;

                    case R.id.navigation_about:
                        customViewPagerChannel.setVisibility(View.GONE);
                        customViewPagerMovies.setVisibility(View.GONE);
                        customViewPagerAbout.setVisibility(View.VISIBLE);


                        setupViewPagerAbout(customViewPagerAbout);
                        tabLayout.setupWithViewPager(customViewPagerAbout);

                        viewPagerId = 2;

                        return true;
                }
            }else{
                noInternetView.setVisibility(View.VISIBLE);
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.splashScreenTheme); // Display logo at the Start of the App
        setTheme(R.style.AppTheme_NoActionBar); // Set the theme for no action bar

        super.onCreate(savedInstanceState);
        // Show EULA
        new AppEula(this).show();

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //stripTextView = findViewById(R.id.stripView);
        progressBar = findViewById(R.id.progress_indicator);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        coordinatorLayout = findViewById(R.id.mainLayout);
        noInternetView = findViewById(R.id.no_internet);

        isConnected = NetworkConnectionUtils.isNetworkConnection(this);

        //Set up the toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        setTitle("");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Display the home button

        //Set up the tabs and ViewPager with the section adapter;
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        customViewPagerChannel = findViewById(R.id.container);
        customViewPagerMovies = findViewById(R.id.container2);
        customViewPagerAbout = findViewById(R.id.container4);
        tabLayout = findViewById(R.id.tabs);


        //Admob implementation
        MobileAds.initialize(this, "ca-app-pub-5138732050544010~6527615257");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //For Device orientation  -- correction for state of rotation
        if (savedInstanceState != null) {

            mViewPagerId = savedInstanceState.getInt("ViewPagerId");
            //tabLayout.setupWithViewPager((ViewPager) state);

            switch (mViewPagerId) {
                case 0:
                    customViewPagerMovies.setVisibility(View.GONE);
                    customViewPagerChannel.setVisibility(View.VISIBLE);
                    customViewPagerAbout.setVisibility(View.GONE);

                    setupViewPager(customViewPagerChannel);
                    tabLayout.setupWithViewPager(customViewPagerChannel);
                    break;
                case 1:
                    customViewPagerChannel.setVisibility(View.GONE);
                    customViewPagerMovies.setVisibility(View.VISIBLE);
                    customViewPagerAbout.setVisibility(View.GONE);

                    setupViewPagerMovies(customViewPagerMovies);
                    tabLayout.setupWithViewPager(customViewPagerMovies);
                    break;

                case 2:

                    customViewPagerChannel.setVisibility(View.GONE);
                    customViewPagerMovies.setVisibility(View.GONE);
                    customViewPagerAbout.setVisibility(View.VISIBLE);

                    setupViewPagerAbout(customViewPagerAbout);
                    tabLayout.setupWithViewPager(customViewPagerAbout);

                    break;
            }

        } else {

            if(isConnected){
                setupViewPager(customViewPagerChannel);
                tabLayout.setupWithViewPager(customViewPagerChannel);
                viewPagerId = 0;
                noInternetView.setVisibility(View.GONE);

            }else{
                noInternetView.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
                return;
            }

        }

    }


    // This method will call to invoke selection of which fragment is to display in a click of a more button at home fragment
    public  void selectFragment(int position){
        customViewPagerChannel.setCurrentItem(position, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        int id = item.getItemId();

        if (id == R.id.action_about_theApp) {
            String webString = "https://docs.google.com/document/d/e/2PACX-1vSwfnGOqZROQBLMOvO6gbZR9c_FW9QqlRCxu08LOoSM3NVkHEvtSclU94lb6gkuKpl_cfRykZLnT5Sw/pub";
            String titleString = "About the App";

            Intent intent = new Intent(MainActivity.this, WebViewNavActivity.class);
            intent.putExtra("WebString", webString);
            intent.putExtra("TitleString", titleString);

            // Get a reference to the ConnectivityManager to check state of network connectivity
            ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            // If there is a network connection, fetch data
            if (networkInfo != null && networkInfo.isConnected()) {

                if (intent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, "Please install Google Docs", Toast.LENGTH_SHORT).show();
                    return true;
                }
            } else {
                Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        }


        if (id == R.id.action_licenseAgreement) {
            String webString = "https://docs.google.com/document/d/e/2PACX-1vSD5rsSVEgEGJ-De06fq0FJrQRofVZB_tbEDyXzbfnVElfBcCsSiJA4CYPrkvaT3g_ygk6u6moGwZSc/pub";
            String titleString = "License Agreement";

            Intent intent = new Intent(MainActivity.this, WebViewNavActivity.class);
            intent.putExtra("WebString", webString);
            intent.putExtra("TitleString", titleString);

            // Get a reference to the ConnectivityManager to check state of network connectivity
            ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            // If there is a network connection, fetch data
            if (networkInfo != null && networkInfo.isConnected()) {

                if (intent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, "Please install Google Docs", Toast.LENGTH_SHORT).show();
                    return true;
                }
            } else {
                Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        }
        if (id == R.id.action_privacyPolicy) {

            String webString = "https://docs.google.com/document/d/e/2PACX-1vTnG1szoC1L8-rRNMxCMKisKXKFtDL4-bUM22ep2O2w9rUWj-5Oo8XHPt26gJEVQVtc-q6gVGARrLvw/pub";
            String titleString = "Privacy Policy";

            Intent intent = new Intent(MainActivity.this, WebViewNavActivity.class);
            intent.putExtra("WebString", webString);
            intent.putExtra("TitleString", titleString);

            // Get a reference to the ConnectivityManager to check state of network connectivity
            ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            // If there is a network connection, fetch data
            if (networkInfo != null && networkInfo.isConnected()) {

                if (intent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, "Please install Google Docs", Toast.LENGTH_SHORT).show();
                    return true;
                }
            } else {
                Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Sets up the ViewPager
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new MiddleEastFragment(), "Arabic");
        adapter.addFragment(new ArabNewsFragment(), "News");
        adapter.addFragment(new SportsFragment(), "Sports");

        viewPager.setAdapter(adapter);

    }

    private void setupViewPagerMovies(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new InTheatersFragment(), "In Theaters");
        adapter.addFragment(new ComingSoonFragment(), "Coming Soon");
        adapter.addFragment(new PopularFragment(), "Popular");
        adapter.addFragment(new TopRatedFragment(), "Top Rated");

        viewPager.setAdapter(adapter);
    }

    private void setupViewPagerAbout(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new AboutAppFragment(), "About the App");
        adapter.addFragment(new ContactUsFragment(), "Contact Us");

        viewPager.setAdapter(adapter);
    }


    //  For device orientation - correction for tab state and viewpager
    @Override
    protected void onSaveInstanceState(@Nullable Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        if (viewPagerId != null && isConnected) {
            saveInstanceState.putInt("ViewPagerId", viewPagerId);
        }else {
            viewPagerId = mViewPagerId;
            saveInstanceState.putInt("ViewPagerId", viewPagerId);
            saveInstanceState.putBoolean("IsConnectionFalse", false);
        }

    }

}
