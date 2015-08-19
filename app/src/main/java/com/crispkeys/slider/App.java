package com.crispkeys.slider;

import android.app.Application;
import timber.log.Timber;

/**
 * Created by Behzodbek Qodirov on 8/19/15.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
