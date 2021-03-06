package com.rdm.mostinterestingflickpics.Widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.rdm.mostinterestingflickpics.Network.FlickrClient;
import com.rdm.mostinterestingflickpics.Network.FlickrResponse;
import com.rdm.mostinterestingflickpics.Network.Photo;
import com.rdm.mostinterestingflickpics.Network.RetrofitFlickr;
import com.rdm.mostinterestingflickpics.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;

/**
 * Created by Rebecca on 10/27/2017.
 */

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Photo photo;
    private List<Photo> mPhotoItems = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        Log.d("WIDGET", "RemoteViewsFactory Constructor");
        mContext = context;
    }

    public StackRemoteViewsFactory(Context context) {
        this.mContext = context;
    }

    public void onCreate() {
    }

    public int getCount() {
        Log.d("WIDGET", "getCount = " + mPhotoItems.size());
        return mPhotoItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // position will always range from 0 to getCount() - 1.
        Log.d("WIDGET", "getViewAt = " + position);

        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.title_text, mPhotoItems.get(position).getTitle());
        Log.d("WIDGET", "Image Title is " + mPhotoItems.get(position).getTitle());
        String imageUrl = mPhotoItems.get(position).getUrlO();
        Log.d("WIDGET", "imageUrl is " + mPhotoItems.get(position).getUrlO());
        Bitmap flickrImage = null;
        try {
            flickrImage = Glide.with(mContext)
                    .load(imageUrl)
                    .asBitmap()
                    .placeholder(R.drawable.flickrplaceholder)
                    .error(R.drawable.glide_empty_view)
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        rv.setImageViewBitmap(R.id.flickr_image, flickrImage);

        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.
        Intent fillInIntent = new Intent();
        photo = mPhotoItems.get(position);
        fillInIntent.putExtra("IMAGE", photo);
        rv.setOnClickFillInIntent(R.id.widget_click_item, fillInIntent);
        Log.d("WIDGET", "FillInIntent Called");

        // Return the remote views object.
        return rv;
    }

    public void onDataSetChanged() {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
        RetrofitFlickr retrofitFlickr = FlickrClient.getClient().create(RetrofitFlickr.class);
        Call<FlickrResponse> call = retrofitFlickr.getPhotoList();

        try {
            FlickrResponse flickrResponse = call.execute().body();
            mPhotoItems = flickrResponse.getPhotos().getPhoto();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("WIDGET", "datasetchanged");
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDestroy() {
    }
}

