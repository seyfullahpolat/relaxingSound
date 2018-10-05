package com.seyfullahpolat.rahatlaticisesler.item;


/**
 * Created by seyfullahpolat on 15/07/2018.
 */

public class SoundItem {
    private int s_id;
    private String sound_name;
    private String sound_url;
    private String image_path;
    private int is_favorite;

    public int getS_id() {
        return s_id;
    }


    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public String getSound_name() {
        return sound_name;
    }

    public void setSound_name(String sound_name) {
        this.sound_name = sound_name;
    }

    public String getSound_url() {
        return sound_url;
    }

    public void setSound_url(String sound_url) {
        this.sound_url = sound_url;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }


}
