package org.roborunners.score;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class ScoreApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}