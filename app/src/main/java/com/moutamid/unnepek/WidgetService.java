package com.moutamid.unnepek;


import android.content.Intent;
import android.widget.RemoteViewsService;
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
