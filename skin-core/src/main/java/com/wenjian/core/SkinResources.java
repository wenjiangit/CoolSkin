package com.wenjian.core;

import android.app.Application;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * @author mac
 * @desc desc
 * @date 2018/3/18
 */

public class SkinResources {

    private static SkinResources sInstance;

    private Resources mAppResources;

    private Resources mSkinResources;

    private String mSkinPkgName;

    private boolean isDefaultSkin = true;

    private SkinResources(Application application) {
        this.mAppResources = application.getResources();
    }

    public static void init(Application application) {
        if (sInstance == null) {
            synchronized (SkinResources.class) {
                if (sInstance == null) {
                    sInstance = new SkinResources(application);
                }
            }
        }
    }

    public static SkinResources getInstance() {
        return sInstance;
    }

    public void apply(Resources resources, String packageName) {
        this.mSkinResources = resources;
        this.mSkinPkgName = packageName;
        this.isDefaultSkin = resources == null || TextUtils.isEmpty(packageName);
    }

    public void reset() {
        mSkinResources = null;
        mSkinPkgName = "";
        isDefaultSkin = true;
    }

    public int getIdentifyId(int resId) {
        if (isDefaultSkin) {
            return resId;
        }

        String resName = mAppResources.getResourceEntryName(resId);
        String typeName = mAppResources.getResourceTypeName(resId);

        return mSkinResources.getIdentifier(resName, typeName, mSkinPkgName);
    }

    public int getColor(int resId) {
        int color = mAppResources.getColor(resId);
        if (isDefaultSkin) {
            return color;
        }

        int skinId = getIdentifyId(resId);
        if (skinId == 0) {
            return color;
        }
        return mSkinResources.getColor(skinId);
    }

    public Drawable getDrawable(int resId) {
        Drawable drawable = mAppResources.getDrawable(resId);
        if (isDefaultSkin) {
            return drawable;
        }

        int skinId = getIdentifyId(resId);
        if (skinId == 0) {
            return drawable;
        }

        return mSkinResources.getDrawable(skinId);
    }

    /**
     * @param resId
     * @return
     */
    public Object getBackground(int resId) {
        String typeName = mAppResources.getResourceTypeName(resId);
        if ("color".equals(typeName)) {
            return getColor(resId);
        } else {
            return getDrawable(resId);
        }
    }


    public String getString(int resId) {
        String string = mAppResources.getString(resId);
        if (isDefaultSkin) {
            return string;
        }

        int skinId = getIdentifyId(resId);
        if (skinId == 0) {
            return string;
        }

        return mSkinResources.getString(skinId);
    }


    public ColorStateList getColorStateList(int resId) {
        ColorStateList colorStateList = mAppResources.getColorStateList(resId);
        if (isDefaultSkin) {
            return colorStateList;
        }

        int skinId = getIdentifyId(resId);
        if (skinId == 0) {
            return colorStateList;
        }

        return mSkinResources.getColorStateList(skinId);
    }

    public Typeface getTypeface(int resId) {
        String skinTypefacePath = getString(resId);
        if (TextUtils.isEmpty(skinTypefacePath)) {
            return Typeface.DEFAULT;
        }
        try {
            Typeface typeface;
            if (isDefaultSkin) {
                typeface = Typeface.createFromAsset(mAppResources.getAssets(), skinTypefacePath);
                return typeface;

            }
            typeface = Typeface.createFromAsset(mSkinResources.getAssets(), skinTypefacePath);
            return typeface;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return Typeface.DEFAULT;
    }


}
