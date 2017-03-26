package com.harputyazilim.gezdir;

/**
 * Created by furkan on 26.03.2017.
 */

import android.media.Image;
import android.widget.ImageView;


public class Match {
    private String name;
    private ImageView image;


    public Match() {
    }

    public Match(String name,ImageView image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }



}