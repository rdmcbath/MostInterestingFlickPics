package com.rdm.mostinterestingflickpics;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.rdm.mostinterestingflickpics.Network.Photo;
import com.rdm.mostinterestingflickpics.Network.Photos;

import butterknife.ButterKnife;

/**
 * Created by Rebecca on 10/28/2017.
 */

public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    public boolean mDualPane;
    private Photo photo;
    private Photos photos;
    private String STATE_KEY = "list_state";
    private Parcelable mListState;
    private LinearLayoutManager mLayoutManager;
    public static final String KEY_IMAGE_DETAIL = "IMAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        photo = intent.getParcelableExtra(KEY_IMAGE_DETAIL);

        DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
        detailActivityFragment.setPhotos(photos);
        detailActivityFragment.setPhoto(photo);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_detail_container, detailActivityFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        Log.i(LOG_TAG, "DetailActivity :onSaveInstanceState");
        super.onSaveInstanceState(state);
        state.putParcelable(STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        Log.i(LOG_TAG, "DetailActivity :onRestoreInstanceState");
        if (state != null) {
            mListState = state.getParcelable(STATE_KEY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "DetailActivity :onResumeInstanceState");
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }
}
