package com.wenjian.core;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.wenjian.core.utils.SkinPreference;

import java.lang.reflect.Method;
import java.util.Observable;

/**
 * @author mac
 * @desc desc
 * @date 2018/3/17
 */

public class SkinManager extends Observable{

    private static volatile SkinManager sInstance;

    private Application mApplication;


    public static void init(Application application){
        if (sInstance == null) {
            synchronized (SkinManager.class) {
                if (sInstance == null) {
                    sInstance = new SkinManager(application);
                }
            }
        }
    }


    public static SkinManager getInstance() {
        return sInstance;
    }

    private SkinManager(Application application){
        this.mApplication = application;
        SkinPreference.init(application);
        SkinResources.init(application);
        application.registerActivityLifecycleCallbacks(new SkinActivityLifecycle());
        loadSkin(SkinPreference.getInstance().getSkin());
    }

    @SuppressLint("PrivateApi")
    public void loadSkin(String path){
        if (TextUtils.isEmpty(path)) {
            SkinPreference.getInstance().setSkin("");
            SkinResources.getInstance().reset();

        } else {
            try {
                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPath = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
                addAssetPath.setAccessible(true);
                addAssetPath.invoke(assetManager, path);
                Resources appResource = mApplication.getResources();
                Resources skinResource = new Resources(assetManager,
                        appResource.getDisplayMetrics(),
                        appResource.getConfiguration());
                PackageManager packageManager = mApplication.getPackageManager();
                PackageInfo info = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                SkinResources.getInstance().apply(skinResource,info.packageName);
                SkinPreference.getInstance().setSkin(path);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //应用皮肤
            setChanged();
            //通知更新
            notifyObservers();
        }
    }



}
