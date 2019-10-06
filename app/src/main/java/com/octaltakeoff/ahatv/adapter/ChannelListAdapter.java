package com.octaltakeoff.ahatv.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octaltakeoff.ahatv.R;
import com.octaltakeoff.ahatv.model.Channel;
import com.octaltakeoff.ahatv.ui.VideoPlayerActivity;
import com.octaltakeoff.ahatv.utils.ColumnUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChannelListAdapter  extends RecyclerView.Adapter<ChannelListAdapter.ViewHolder> {
    private static final String TAG = "ChannelListAdapter";

    List<Channel> mChannel;
    Activity mActivity;
    Context context;
    private int currentPosition;
    private int selected;
    private int iconWidth;
    public int mNoOfColumns;
    CustomItemClickListener customItemClickListener;

    public ChannelListAdapter(Activity mActivity, List<Channel> mChannel, CustomItemClickListener customItemClickListener) {
        this.mChannel = mChannel;
        this.mActivity = mActivity;
        this.customItemClickListener = customItemClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channellist_adapter_layout, parent, false);
        //ViewHolder holder = new ViewHolder(view);
        final ChannelListAdapter.ViewHolder holder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customItemClickListener.onItemClick(view, holder.getAdapterPosition());
            }
        });



        return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder : called.");

        // Change the size of the thumbnails
        mNoOfColumns = (int) Math.floor(ColumnUtils.calculateNoOfColumns(mActivity));
        iconWidth = (int) Math.floor(ColumnUtils.calculateIconWidth(mActivity, mNoOfColumns));
        int iconHeight = (int) (iconWidth * 1.41);
        int margin = 12;

        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) holder.parentLayout.getLayoutParams();
        params.width = iconWidth;
        params.height = iconHeight;
        ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) holder.parentLayout.getLayoutParams();
        params2.setMargins(12,12,12,12);
        holder.parentLayout.setLayoutParams(params2);



        // Get the value of the views
        final Channel currentChannel = mChannel.get(position);

        final String currentChannelTitle = currentChannel.getTitle();
        final String currentChannelCategory = currentChannel.getCategory();
        final String currentVideoUrl = currentChannel.getVideoUrl();
        final String currentChannelRegion = currentChannel.getRegion();
        final String currentImageUrl = currentChannel.getImageUrl();

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.sofilogo);
        Glide.with(mActivity)
                .setDefaultRequestOptions(options)
                .load(currentImageUrl)
                .into(holder.imageUrl_imageView);

        holder.channelTitle_textView.setText(currentChannelTitle);
        holder.channelCategory_textView.setText(currentChannelCategory);
        // get the position of the item in the list
        currentPosition = holder.getAdapterPosition();


        // set on click listener to the parent layout
//        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: click on:" + currentChannelTitle);
//                //Toast.makeText(mActivity, currentImageTitle, Toast.LENGTH_SHORT).show();
//
//                // Select values to pass to other activity
//
//                Intent intent = new Intent(mActivity, VideoPlayerActivity.class);
//
//                intent.putExtra("SelectedChannel", currentChannel);
//                intent.putExtra("SelectedPosition", currentPosition);
//
//                // for shared element transition animation
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, (View) view, "profile");
//
//                // add options to bundle for transition animation
//                mActivity.startActivity(intent, options.toBundle());
//
//            }
//        });

    }
    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mChannel.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageUrl_imageView;
        TextView channelTitle_textView;
        TextView channelCategory_textView;
        CardView parentLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            imageUrl_imageView = itemView.findViewById(R.id.iv_channelPoster);
            channelTitle_textView = itemView.findViewById(R.id.tv_channelTitle);
            channelCategory_textView = itemView.findViewById(R.id.tv_channelCategory);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }

    }
    public interface CustomItemClickListener {
        public void onItemClick(View v, int position);
    }


}


