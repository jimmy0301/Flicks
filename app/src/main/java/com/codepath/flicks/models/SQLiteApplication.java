package com.codepath.flicks.models;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by keyulun on 2017/2/19.
 */

public class SQLiteApplication extends Application {
   @Override
   public void onCreate() {
      super.onCreate();

      FlowManager.init(new FlowConfig.Builder(this).build());
   }
}
