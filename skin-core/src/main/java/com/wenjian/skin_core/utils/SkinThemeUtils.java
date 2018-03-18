package com.wenjian.skin_core.utils;

import android.content.Context;
import android.content.res.TypedArray;

import org.w3c.dom.Attr;

/**
 * @author mac
 * @desc desc
 * @date 2018/3/18
 */

public class SkinThemeUtils {

    public static int[] getResIds(Context context, int[] attrs) {
        int[] resIds = new int[attrs.length];
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            resIds[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return resIds;
    }

}
