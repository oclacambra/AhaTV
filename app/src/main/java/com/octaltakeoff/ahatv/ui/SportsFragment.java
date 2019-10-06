package com.octaltakeoff.ahatv.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.octaltakeoff.ahatv.R;
import com.octaltakeoff.ahatv.adapter.ChannelListAdapter;
import com.octaltakeoff.ahatv.model.Channel;
import com.octaltakeoff.ahatv.utils.ColumnUtils;

import java.util.ArrayList;

public class SportsFragment extends android.support.v4.app.Fragment implements ChannelListAdapter.CustomItemClickListener{
    private static final String TAG = "SportsFragment";

    // variables
    private TextView footerTextView;
    public static ArrayList<Channel> channels = new ArrayList<Channel>();
    private RecyclerView gridRecyclerView;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    public int position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_layout, container, false);


        gridRecyclerView = view.findViewById(R.id.recycleView_grid);

        footerTextView = view.findViewById(R.id.footer_textView);
        footerTextView.setText("Please read our Privacy Policy");

        getMusicChannels();

        return view;
    }

    public void getMusicChannels() {
        FindSportsChannels findSportsChannels = new FindSportsChannels();
        findSportsChannels.execute();

    }

    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        intent.putParcelableArrayListExtra(VideoPlayerActivity.CHANNEL_SENT, new ArrayList<>(channels));
        intent.putExtra(VideoPlayerActivity.CHANNEL_CLICKED, position);

        // For shared element transition
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), (View) getView(), "profile");
//        startActivity(intent, options.toBundle());
        startActivity(intent);


    }

    private class FindSportsChannels extends AsyncTask<Void, Void, ArrayList<Channel>> {

        @Override
        protected ArrayList<Channel> doInBackground(Void... voids) {

            mDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mDatabase.getReference("channels");

            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    channels.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Channel channel = dataSnapshot1.getValue(Channel.class);
                        channels.add(channel);
                    }

                    // Set image to display only Music category in recyclerView
                    int totalChannels = channels.size();

                    for (int i = 0; i < totalChannels; i++) {
                        if (!channels.get(i).getRegion().equals("Sports")) {
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

            return channels;
        }
    }

    private void initRecyclerView() {
        //Log.d(TAG, getString(R.string.init_message_log));

        int mNoOfColumns = (int) Math.floor(ColumnUtils.calculateNoOfColumns(getContext()));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), mNoOfColumns);
        gridRecyclerView.setLayoutManager(layoutManager);
        ChannelListAdapter channelListAdapter = new ChannelListAdapter(getActivity(), channels, this);
        gridRecyclerView.setAdapter(channelListAdapter);


        //Toast.makeText(getActivity(), "columns :" + mNoOfColumns, Toast.LENGTH_SHORT).show();
    }

}
