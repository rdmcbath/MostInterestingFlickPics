package com.rdm.mostinterestingflickpics.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rdm.mostinterestingflickpics.DetailActivity;
import com.rdm.mostinterestingflickpics.Network.Description;
import com.rdm.mostinterestingflickpics.Network.FlickrResponse;
import com.rdm.mostinterestingflickpics.Network.Photo;
import com.rdm.mostinterestingflickpics.Network.Photos;
import com.rdm.mostinterestingflickpics.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rebecca on 10/28/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private static final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private FlickrResponse flickrResponse;
    private Photos photos;
    private Description description;
    private List<Photo> photoItems = new ArrayList<>();
    private Context context;

    public ImageAdapter(List<Photo> photoItems, Description description, Context context) {
        this.photoItems = photoItems;
        this.description = description;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_grid_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {

        String imageUrl = photoItems.get(position).getUrlO();

        Glide.with(context).load(imageUrl)
                .placeholder(R.drawable.flickrlaunchicon)
                .error(R.drawable.glide_empty_view)
                .centerCrop()
                .into(holder.flickrImage);

        holder.cardTitleTextView.setText(photoItems.get(position).getTitle());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.flickr_image)
        ImageView flickrImage;
        @BindView(R.id.card_title)
        TextView cardTitleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i(LOG_TAG, "onClick in ImageAdapter Called");
            // Start the ImageDetail activity
            Photo data = photoItems.get(getAdapterPosition());
            Intent imageDetailIntent = new Intent(context, DetailActivity.class);
            imageDetailIntent.putExtra("IMAGE", data);
            context.startActivity(imageDetailIntent);
        }
    }

    @Override
    public int getItemCount() {
        return photoItems.size();
    }
}

