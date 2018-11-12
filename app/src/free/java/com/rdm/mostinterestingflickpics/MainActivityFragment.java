package com.rdm.mostinterestingflickpics;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.rdm.mostinterestingflickpics.Adapter.ImageAdapter;
import com.rdm.mostinterestingflickpics.Network.Description;
import com.rdm.mostinterestingflickpics.Network.FlickrClient;
import com.rdm.mostinterestingflickpics.Network.FlickrResponse;
import com.rdm.mostinterestingflickpics.Network.Photo;
import com.rdm.mostinterestingflickpics.Network.Photos;
import com.rdm.mostinterestingflickpics.Network.RetrofitFlickr;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rebecca on 10/27/2017.
 */

public class MainActivityFragment extends android.support.v4.app.Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    @BindView(R.id.image_grid_recycler)
    RecyclerView mImageGridRecyclerView;
    @BindView(R.id.images_frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.empty_view)
    @Nullable
    TextView mEmptyView;
    @BindView(R.id.adView)
    AdView mAdView;

    private Photo photo;
    private Photos photos;
    private List<Photo> photoItems = new ArrayList<>();
    private FlickrResponse flickrResponse;
    private Description description;
    private Context context;
    private boolean mDualPane;
    private static final String IMAGE_IMPORT = "image_grid";
    private Parcelable mListState;
    private LinearLayoutManager mLayoutManager;
    private ImageAdapter mImageAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(LOG_TAG, "MainActivityFragment Free: onCreateView");

        MobileAds.initialize(getActivity(), getString(R.string.interstitial_ad_unit_id));

        if (savedInstanceState != null) {
            photoItems = savedInstanceState.getParcelableArrayList(IMAGE_IMPORT);
        }

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mDualPane = getResources().getBoolean(R.bool.is_tablet);

        if (mDualPane) {
            mLayoutManager = new GridLayoutManager(getContext(), 4);
        } else {
            mLayoutManager = new GridLayoutManager(getContext(), 2);
        }
        mImageGridRecyclerView.setLayoutManager(mLayoutManager);
        mImageAdapter = new ImageAdapter(photoItems, description, getActivity());
        mImageGridRecyclerView.setAdapter(mImageAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, load lesson plan data
        if (networkInfo != null && networkInfo.isConnected()) {

            loadImageGrid();
            Log.i(LOG_TAG, "loadImageGrid Method Called");

        } else {
            Snackbar snackbar = Snackbar
                    .make(frameLayout, R.string.no_network, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

        return rootView;
    }

    private void loadImageGrid() {
        RetrofitFlickr retrofitFlickr = FlickrClient.getClient().create(RetrofitFlickr.class);
        Call<FlickrResponse> call = retrofitFlickr.getPhotoList();
        Log.i(LOG_TAG, "RetrofitFlickr - GetPhotostream Called");

        call.enqueue(new Callback<FlickrResponse>() {

            @Override
            public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {

                if (response.isSuccessful()) {
                    FlickrResponse flickrResponse = response.body();
                    photoItems = flickrResponse.getPhotos().getPhoto();
                    Log.i(LOG_TAG, "Response.Body Retrofit Called");
                    mImageAdapter = new ImageAdapter(photoItems, description, getContext());
                    mImageGridRecyclerView.setAdapter(mImageAdapter);
                    mLayoutManager.onRestoreInstanceState(mListState);

                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<FlickrResponse> call, Throwable t) {
                t.printStackTrace();
                mEmptyView.setText(R.string.no_network);
            }
        });
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public void setPhoto(List<Photo> photoItems) {
        this.photoItems = photoItems;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(IMAGE_IMPORT, (ArrayList<? extends Parcelable>) photoItems);
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageAdapter.notifyDataSetChanged();
    }
}
