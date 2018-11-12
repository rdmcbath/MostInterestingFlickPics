package com.rdm.mostinterestingflickpics.Network;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Rebecca on 10/26/2017.
 */

public interface RetrofitFlickr {

    // This call executes the photo search and retrieves public photos from the flickr.interestingness.getList.
    // Here is what the query looks like:
    // https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&api_key=
    // 4213ea16fba058bed6fd4b879a87cabd&extras=url_z,description&per_page=500&format=json&nojsoncallback=1

    @GET("rest/?method=flickr.interestingness.getList&api_key=4213ea16fba058bed6fd4b879a87cabd&extras=url_z," +
            "description&per_page=500&format=json&nojsoncallback=1")
    Call<FlickrResponse> getPhotoList();
}