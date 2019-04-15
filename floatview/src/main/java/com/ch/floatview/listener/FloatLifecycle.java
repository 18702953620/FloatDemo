package com.ch.floatview.listener;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 作者： ch
 * 时间： 2019/4/12 0012-下午 5:08
 * 描述：
 * 来源：
 */

public class FloatLifecycle extends BroadcastReceiver implements Application.ActivityLifecycleCallbacks {

    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private LifecycleListener mLifecycleListener;
    private Class[] activities;

    public void setActivities(Class[] activities) {
        this.activities = activities;
    }

    public FloatLifecycle(Context context, Class[] activities, LifecycleListener lifecycleListener) {
        mLifecycleListener = lifecycleListener;
        this.activities = activities;
        ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(this);
    }


    @Override
    public void onActivityResumed(Activity activity) {
        if (needShow(activity)) {
            mLifecycleListener.onShow();
        } else {
            mLifecycleListener.onHide();
        }
    }

    private boolean needShow(Activity activity) {
        if (activities == null) {
            return true;
        }
        for (Class a : activities) {
            if (a.isInstance(activity)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onActivityPaused(final Activity activity) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
    }


    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                mLifecycleListener.onBackToDesktop();
            }
        }
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }


    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}
