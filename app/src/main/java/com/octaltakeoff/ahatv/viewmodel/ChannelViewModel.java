package com.octaltakeoff.ahatv.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.octaltakeoff.ahatv.adapter.ChannelListAdapter;
import com.octaltakeoff.ahatv.model.Channel;
import com.octaltakeoff.ahatv.utils.ColumnUtils;

import java.util.ArrayList;

public class ChannelViewModel {

    // variables
    public static ArrayList<Channel> channels = new ArrayList<Channel>();
    public static Channel channel;
    private RecyclerView gridRecyclerView;
    Activity activity;


    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = mDatabase.getReference("channels");

    public DatabaseReference getmDatabaseReference() {

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                channels.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Channel channel = dataSnapshot1.getValue(Channel.class);
                    channels.add(channel);
                }

                // Set image to display only Music category in recyclerView
                int channelGroupCount = 0;
                int totalChannels = channels.size();

                for (int i = 0; i < totalChannels; i++) {
                    if (!channels.get(i).getCategory().equals("Music")) {
                        channels.remove(i);
                        totalChannels--;
                        i--;
                    }
                }

                initRecyclerView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return mDatabaseReference;
    }

    private void initRecyclerView() {
        //Log.d(TAG, getString(R.string.init_message_log));

        int mNoOfColumns = ColumnUtils.calculateNoOfColumns(activity);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(activity, mNoOfColumns);
        gridRecyclerView.setLayoutManager(layoutManager);
//        ChannelListAdapter channelListAdapter = new ChannelListAdapter(activity, channels, );
//        gridRecyclerView.setAdapter(channelListAdapter);
    }

}
