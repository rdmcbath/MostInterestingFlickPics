package com.rdm.mostinterestingflickpics.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Rebecca on 10/27/2017.
 */

public class StackWidgetService extends RemoteViewsService {

    public StackWidgetService() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(getApplicationContext(), intent);
    }
}

