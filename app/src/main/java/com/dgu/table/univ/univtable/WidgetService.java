package com.dgu.table.univ.univtable;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.tsengvn.typekit.TypekitContextWrapper;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new WidgetViewFactory(this.getApplicationContext(), intent));
    }
}