package com.octaltakeoff.ahatv.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.octaltakeoff.ahatv.R;

public class WebViewNavActivity extends AppCompatActivity {

    private WebView webView;
    private android.support.v7.widget.Toolbar mToolBar;

    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_webnav_view);

        // Set and show the toolbar and the home button
        mToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);   // Shows the home button (arrow key)

        // Makes Progress bar Visible
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        Intent receivedIntent = getIntent();
        final String webString = receivedIntent.getStringExtra("WebString");
        final String titleString = receivedIntent.getStringExtra("TitleString");

        webView = (WebView) findViewById(R.id.web_view);
        webView.setWebContentsDebuggingEnabled(false);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_indicator);

        // Set the Progress Bar
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                setTitle("Loading...");

                setProgress(progress * 100); //Make the bar disappear after URL is loaded
                // Return the app name after finish loading
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                    setTitle(titleString);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(webString);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    // This method will return to the previous fragment  or activity if homeButton is pressed. Be sure to indicate the parent Meta Data in manifest.
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentIntent = NavUtils.getParentActivityIntent(this);
                parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(parentIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

