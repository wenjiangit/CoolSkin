package com.wenjian.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;

import com.wenjian.core.utils.SkinThemeUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author mac
 * @desc desc
 * @date 2018/3/17
 */

public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private Map<Activity, SkinLayoutFactory> mFactoryMap = new WeakHashMap<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        SkinThemeUtils.updateStatusBarColor(activity);

        LayoutInflater layoutInflater = LayoutInflater.from(activity);

        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.set(layoutInflater, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        SkinLayoutFactory factory = new SkinLayoutFactory(activity);
        LayoutInflaterCompat.setFactory2(layoutInflater, factory);

        SkinManager.getInstance().addObserver(factory);
        mFactoryMap.put(activity, factory);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        SkinLayoutFactory factory = mFactoryMap.remove(activity);
        SkinManager.getInstance().deleteObserver(factory);
    }
}
