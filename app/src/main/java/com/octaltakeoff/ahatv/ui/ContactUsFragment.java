package com.octaltakeoff.ahatv.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.octaltakeoff.ahatv.R;

public class ContactUsFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "ContactUsFragment";

    // variables
    private TextView footerTextView, bodyTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.about_layout, container, false);


        footerTextView = (TextView) view.findViewById(R.id.footer_textView);
        footerTextView.setText("This is AboutApp Fragment");
        bodyTextView = (TextView) view.findViewById(R.id.body_textView);
        bodyTextView.setText("\n Contact Information:\n\n SofiTV\n\n By: ElyOctal \n\nEmail : elyoctal@gmail.com");

        return view;
    }

}

