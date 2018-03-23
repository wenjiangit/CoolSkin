package com.wenjian.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;

import com.wenjian.core.R;
import com.wenjian.core.SkinResources;

/**
 * @author mac
 * @desc desc
 * @date 2018/3/18
 */

public class SkinThemeUtils {

    /**
     * 系统状态栏颜色,导航栏颜色属性
     */
    private static final int[] SYSTEM_STATUS_BAR_ATTR = {
            android.R.attr.statusBarColor, android.R.attr.navigationBarColor
    };

    /**
     * Appcompat-v7包的colorPrimaryDark的属性id值
     */
    private static final int[] APPCOMPAT_COLOR_PRIMARY_DARK = {
            android.support.v7.appcompat.R.attr.colorPrimaryDark
    };


    private static final int[] TYPEFACE_ATTR = {
            R.attr.skinTypeface
    };


    /**
     * 通过属性id获取对应的资源id 如:background属性id android.R.attr.background ===> 对应的资源id为 R.color.xxx 或 R.drawable.xxx
     *
     * @param context Context
     * @param attrs   属性数组,可以同时获取多个
     * @return 资源id数组
     */
    public static int[] getResIdByAttrId(Context context, int[] attrs) {
        int[] resIds = new int[attrs.length];
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            resIds[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return resIds;
    }

    /**
     * 更新系统状态栏颜色
     *
     * @param activity Activity
     */
    public static void updateStatusBarColor(Activity activity) {
        //sdk版本大于5.0才支持更换状态栏颜色
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        final int[] systemBarResIds = getResIdByAttrId(activity, SYSTEM_STATUS_BAR_ATTR);

        //设置状态栏颜色
        if (systemBarResIds[0] == 0) {
            //未配置相关属性,取v7包的colorPrimaryDark属性的资源id
            int[] resId = getResIdByAttrId(activity, APPCOMPAT_COLOR_PRIMARY_DARK);
            if (resId[0] != 0) {
                final int color = SkinResources.getInstance().getColor(resId[0]);
                activity.getWindow().setStatusBarColor(color);
            }

        } else {
            final int color = SkinResources.getInstance().getColor(systemBarResIds[0]);
            activity.getWindow().setStatusBarColor(color);
        }

        //设置导航栏颜色
        if (systemBarResIds[1] != 0) {
            final int color = SkinResources.getInstance().getColor(systemBarResIds[1]);
            activity.getWindow().setNavigationBarColor(color);
        }
    }

    /**
     * 获取字体
     *
     * @param context Context
     * @return Typeface
     */
    public static Typeface getTypeface(Context context) {
        int[] resIds = getResIdByAttrId(context, TYPEFACE_ATTR);
        return SkinResources.getInstance().getTypeface(resIds[0]);

    }
}
