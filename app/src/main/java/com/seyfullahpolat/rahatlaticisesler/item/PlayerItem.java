package com.seyfullahpolat.rahatlaticisesler.item;

import android.media.MediaPlayer;

import com.google.android.exoplayer2.SimpleExoPlayer;


/**
 * Created by seyfullahpolat on 15/07/2018.
 */

public class PlayerItem implements MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener{
    MediaPlayer player = new MediaPlayer();
    int soundId;

    SimpleExoPlayer simpleExoPlayer;
    public MediaPlayer getPlayer() {
        player.setOnBufferingUpdateListener(this);
        player.setOnCompletionListener(this);
        return player;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    public int getSoundId() {
        return soundId;
    }

    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }

    public SimpleExoPlayer getSimpleExoPlayer() {
        return simpleExoPlayer;
    }

    public void setSimpleExoPlayer(SimpleExoPlayer simpleExoPlayer) {
        this.simpleExoPlayer = simpleExoPlayer;
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }
}
