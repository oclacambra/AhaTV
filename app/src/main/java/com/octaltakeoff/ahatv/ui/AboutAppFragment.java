package com.octaltakeoff.ahatv.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.octaltakeoff.ahatv.R;

public class AboutAppFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "AboutAppFragment";

    // variables
    private TextView footerTextView, bodyTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.about_layout, container, false);


        footerTextView = (TextView) view.findViewById(R.id.footer_textView);
        footerTextView.setText("This is AboutApp Fragment");
        bodyTextView = (TextView) view.findViewById(R.id.body_textView);
        bodyTextView.setText("\nSofiTV \nVersion 1.0\n\nThis app is a collection of Arabian TV links and at the same time a file of movie trailer from TMDb.\n\nThe app is mainly for Middle Eastern users spread around the world to make it closer to their home countries. \n\nThis app also utilizes The MovieDatabase TMDb for movie trailers using Google’s YouTube");

        return view;
    }


}
