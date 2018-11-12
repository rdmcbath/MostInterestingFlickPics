package com.rdm.mostinterestingflickpics.Network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rebecca on 10/26/2017.
 */

public class FlickrResponse {

    @SerializedName("photos")
    @Expose
    private Photos photos;
    @SerializedName("stat")
    @Expose
    private String stat;

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

}
