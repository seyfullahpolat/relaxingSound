package com.seyfullahpolat.rahatlaticisesler.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.seyfullahpolat.rahatlaticisesler.R;
import com.seyfullahpolat.rahatlaticisesler.item.SoundItem;

import java.util.ArrayList;
/**
 * Created by seyfullahpolat on 15/07/2018.
 */
public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.Myholder> {
    private FavoriteListAdapter.ClickListener clickListener = null;

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
        public ImageView play_pause;
        public SeekBar volume;
        public TextView sound_name;
        public SimpleDraweeView sound_icon;


        public Myholder(View view) {
            super(view);
            fav_icon = (ImageView) view.findViewById(R.id.fav_icon);
            play_pause = (ImageView) view.findViewById(R.id.play_pause);
            volume = (SeekBar) view.findViewById(R.id.volume);
            sound_name = (TextView) view.findViewById(R.id.sound_name);
            sound_icon = (SimpleDraweeView) view.findViewById(R.id.sound_icon);
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
    }


    public FavoriteListAdapter(ArrayList<SoundItem> soundItems, Context context) {
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
                    clickListener.onClickFav(position, holder.play_pause);
                }
            }
        });
        holder.play_pause.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onClickPlayPause(view, position, holder.play_pause);
                }
            }
        });
        holder.volume.setMax(100);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.volume.setMin(0);
        }
        holder.volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        holder.volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (clickListener != null) {
                    clickListener.onClickVolume(position, seekBar, i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_list_row, parent, false);
        return new Myholder(itemView);
    }

    @Override
    public int getItemCount() {
        return soundItems.size();
    }

    public interface ClickListener {
        void onClickPlayPause(View view, int position, ImageView play_pause);

        void onClickVolume(int position, SeekBar seekBar, int value);

        void onClickFav(int position, ImageView play_pause);
    }


}