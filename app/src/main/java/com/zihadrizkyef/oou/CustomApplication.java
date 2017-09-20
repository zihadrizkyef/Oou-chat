package com.zihadrizkyef.oou;

import android.app.Application;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 19/09/17.
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new CustomLifeCycleCallback());
    }
}
