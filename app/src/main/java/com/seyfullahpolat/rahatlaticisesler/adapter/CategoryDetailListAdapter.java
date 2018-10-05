package com.seyfullahpolat.rahatlaticisesler.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.seyfullahpolat.rahatlaticisesler.R;
import com.seyfullahpolat.rahatlaticisesler.item.SoundItem;

import java.util.ArrayList;

/**
 * Created by seyfullahpolat on 15/07/2018.
 */

public class CategoryDetailListAdapter extends RecyclerView.Adapter<CategoryDetailListAdapter.Myholder> {
    private CategoryDetailListAdapter.ClickListener clickListener = null;

    public ClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private ArrayList<SoundItem> soundItems;
    private Context context;


    AudioManager audioManager;

    public class Myholder extends RecyclerView.ViewHolder {
        public ImageView fav_icon;
        public TextView sound_name;
        public SimpleDraweeView sound_icon;


        public Myholder(View view) {
            super(view);
            fav_icon = (ImageView) view.findViewById(R.id.fav_icon);
            sound_name = (TextView) view.findViewById(R.id.sound_name);
            sound_icon = (SimpleDraweeView) view.findViewById(R.id.sound_icon);
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
    }


    public CategoryDetailListAdapter(ArrayList<SoundItem> soundItems, Context context) {
        this.soundItems = soundItems;
        this.context = context;
    }


    @Override
    public void onBindViewHolder(final Myholder holder, final int position) {
        final SoundItem resultsItem = soundItems.get(position);
        holder.sound_name.setText(resultsItem.getSound_name());
        Uri uri = Uri.parse(resultsItem.getImage_path());
        holder.sound_icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.sound_icon.setImageURI(uri);
        holder.fav_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClickFav(position);
                }
            }
        });

    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_detail_list_row, parent, false);
        return new Myholder(itemView);
    }

    @Override
    public int getItemCount() {
        return soundItems.size();
    }

    public interface ClickListener {

        void onClickFav(int position);
    }


}