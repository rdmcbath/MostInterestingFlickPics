package com.rdm.mostinterestingflickpics;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.rdm.mostinterestingflickpics.Network.FlickrResponse;
import com.rdm.mostinterestingflickpics.Network.Photo;
import com.rdm.mostinterestingflickpics.Network.Photos;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.empty_view)
    @Nullable
    TextView mEmptyView;
    private String STATE_KEY = "list_state";
    private Parcelable mListState;
    private Photos photos;
    private List<Photo> photoItems = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MainActivityFragment mainActivityFragment = new MainActivityFragment();
        mainActivityFragment.setPhotos(photos);
        mainActivityFragment.setPhoto(photoItems);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, mainActivityFragment)
                .commit();

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.main_toolbar_title);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        Log.i(LOG_TAG, "MainActivity :onSaveInstanceState");
        super.onSaveInstanceState(state);
        mLayoutManager = new LinearLayoutManager(this);
        mListState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mLayoutManager = new LinearLayoutManager(this);
        Log.i(LOG_TAG, "MainActivity :onRestoreInstanceState");
        if (state != null) {
            mListState = state.getParcelable(STATE_KEY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "MainActivity :onResumeInstanceState");
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }
}
