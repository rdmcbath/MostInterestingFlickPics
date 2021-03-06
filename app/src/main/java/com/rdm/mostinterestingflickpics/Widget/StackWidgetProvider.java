package com.rdm.mostinterestingflickpics.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.rdm.mostinterestingflickpics.DetailActivity;
import com.rdm.mostinterestingflickpics.MainActivity;
import com.rdm.mostinterestingflickpics.Network.Photo;
import com.rdm.mostinterestingflickpics.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rebecca on 10/27/2017.
 */

public class StackWidgetProvider extends AppWidgetProvider {
    public static final String EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM";
    private List<Photo> mPhotoItems = new ArrayList<>();
    private Photo mPhoto;
    private Context mContext;

    public StackWidgetProvider() {
    }

    public StackWidgetProvider(Context context) {
        mContext = context;
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onEnabled(Context context) {
    }

    public void onWidgetUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.d("WIDGET", "updating app widget");

        AppWidgetManager.getInstance(context);
        // set intent for widget service that will create the views
        Intent serviceIntent = new Intent(context, StackWidgetService.class);
        // Add the app widget ID to the intent extras.
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // embed extras so they don't get ignored
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        // Instantiate the RemoteViews object for the app widget layout.
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        // Set up the RemoteViews object to use a RemoteViews adapter. This adapter connects
        // to a RemoteViewsService through the specified intent. This is how you populate the data.
        rv.setRemoteAdapter(R.id.stack_view, serviceIntent);
        //set remoteAdapter on textView for image title
        rv.setRemoteAdapter(R.id.title_text, serviceIntent);
        //set remoteAdapter on imageView for icon image
        rv.setImageViewResource(R.id.icon_image, R.mipmap.ic_launcher);
        rv.setRemoteAdapter(R.id.icon_image, serviceIntent);

        // Update the remoteView at this end
        appWidgetManager.updateAppWidget(appWidgetId, rv);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.title_text);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.icon_image);
        Log.d("WIDGET", "updated WIDGET StackView and TitleView in onWidgetUpdate");

        // set intent for item click (opens detail activity)
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Intent viewIntent = new Intent(context, DetailActivity.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity
                (context, 0, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, viewPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.title_text);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.icon_image);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            onWidgetUpdate(context, appWidgetManager, appWidgetId);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.title_text);
        }
    }
}


