package com.moutamid.unnepek.services;


import android.content.Intent;
import android.widget.RemoteViewsService;

import com.moutamid.unnepek.widget.GridRemoteViewsFactory;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
