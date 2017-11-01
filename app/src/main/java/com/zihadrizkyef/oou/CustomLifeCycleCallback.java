package com.zihadrizkyef.oou;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.zihadrizkyef.oou.helper.api.ApiHelper;

import java.io.IOException;

import me.leolin.shortcutbadger.ShortcutBadger;

import static android.content.Context.MODE_PRIVATE;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 19/09/17.
 */

public class CustomLifeCycleCallback implements Application.ActivityLifecycleCallbacks {
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
        if (activity.getClass().getSimpleName().equals("Activity_ChatRoom")) {
            updateBadge(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        if (activity.getClass().getSimpleName().equals("Activity_Main")) {
            updateBadge(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    private void updateBadge(final Context context) {
        String shrPrfName = context.getString(R.string.shared_pref_name);
        SharedPreferences sharedPreferences = context.getSharedPreferences(shrPrfName, MODE_PRIVATE);
        final int id = sharedPreferences.getInt("id", -1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int badge = ApiHelper.getOouApiClient().countAllUnreaded(id).execute().body().getUnreaded();
                    ShortcutBadger.applyCount(context, badge);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
