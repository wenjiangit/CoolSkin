package com.wenjian.skin_core;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.wenjian.skin_core.utils.SkinPreference;

import java.lang.reflect.Method;

/**
 * @author mac
 * @desc desc
 * @date 2018/3/17
 */

public class SkinManager {


    private static SkinManager sInstance;

    private Application mApplication;


    public static void init(Application application){
        synchronized (SkinManager.class) {
            if (sInstance == null) {
                sInstance = new SkinManager(application);
            }
        }
    }


    public static SkinManager getInstance() {
        return sInstance;
    }

    private SkinManager(Application application){
        this.mApplication = application;
        application.registerActivityLifecycleCallbacks(new SkinActivityLifecycle());
        SkinPreference.init(application);
    }

    public void loadSkin(String path){
        if (TextUtils.isEmpty(path)) {
            SkinPreference.getInstance().setSkin("");

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


            } catch (Exception e) {
                e.printStackTrace();
            }


            SkinPreference.getInstance().setSkin(path);




        }
    }



}
