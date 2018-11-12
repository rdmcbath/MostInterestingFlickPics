package com.rdm.mostinterestingflickpics;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.rdm.mostinterestingflickpics.Network.Description;
import com.rdm.mostinterestingflickpics.Network.Photo;
import com.rdm.mostinterestingflickpics.Network.Photos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Rebecca on 10/28/2017.
 */

public class DetailActivityFragment extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private Photos photos;
    private Photo photo;
    private Description description;
    private List<Photo> photoItems = new ArrayList<>();
    private static final String IMAGE_IMPORT = "image_import";
    private Parcelable mListState;
    private LinearLayoutManager mLayoutManager;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.image_description)
    TextView descriptionTextView;
    @BindView(R.id.link_to_flickr_page)
    TextView linkTextView;
    @BindView(R.id.image_detail_photo)
    ImageView imageDetailPhoto;
    @BindView(R.id.fab_share)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.coordinator_layout_image_detail)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.adView)
    AdView mAdView;
    private Unbinder unbinder;
    private InterstitialAd mInterstitialAd;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "DetailActivityFragment Free: onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        MobileAds.initialize(getContext(), "ca-app-pub-9768055085140533~6483188198");

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        loadInterstitialAd();

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        // Set the title of the chosen image and show the Up button in the action bar.
        collapsingToolbarLayout.setTitle(photo.getTitle());
        Log.d(LOG_TAG, "ImageTitle: " + photo.getTitle());
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.flickrlaunchicon);
        (((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Set the selected image onto the App Bar space
        String imageUrl = photo.getUrlO();
        Glide.with(getContext()).load(imageUrl)
                .placeholder(R.drawable.flickrlaunchicon)
                .fitCenter()
                .into(imageDetailPhoto);

        // Set the description of the chosen image and remove the html tags within it
        String description = photo.getDescription().getContent();
        String newDescription = Html.fromHtml(description).toString();
        descriptionTextView.setText(newDescription);

        linkTextView.setClickable(true);
        linkTextView.setText("See " + photo.getTitle() + " on Flickr");

        fab.setOnClickListener(this);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });

        return rootView;
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    @Override
    public void onClick(View view) {
        Log.d(LOG_TAG, "Share FAB button clicked");
        String textSubject = getString(R.string.extra_subject_share);
        String textExtra = getString(R.string.extra_text_share) + "\n\n" + "Title: " + photo.getTitle() + "\n\n" + photo.getUrlO();
        shareTextAndLink(textSubject, textExtra);
    }

    @Override
    public void onDestroyView() {
        Log.d(LOG_TAG, "DetailActivityFragment Free: onDestroyView");
        super.onDestroyView();
        unbinder.unbind();
    }

    // Share text and link to the Flickr photo url
    private void shareTextAndLink(String textSubject, String textExtra) {
        Log.d(LOG_TAG, "DetailActivityFragment Free: ShareText called");
        Intent shareTextIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareTextIntent.setType("text/plain");
        shareTextIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, textSubject);
        shareTextIntent.putExtra(Intent.EXTRA_TEXT, textExtra);
        startActivity(Intent.createChooser(shareTextIntent, "Share to"));
    }
}



