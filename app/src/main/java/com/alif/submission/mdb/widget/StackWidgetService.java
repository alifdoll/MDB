package com.alif.submission.mdb.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.alif.submission.mdb.widget.StackRemoteViewsFactory;

public class StackWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}
