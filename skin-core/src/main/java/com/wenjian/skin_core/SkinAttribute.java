package com.wenjian.skin_core;

import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.wenjian.skin_core.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mac
 * @desc desc
 * @date 2018/3/17
 */

public class SkinAttribute {

    private static final List<String> VIEW_ATTRS = new ArrayList<>();

    static {
        VIEW_ATTRS.add("background");
        VIEW_ATTRS.add("src");
        VIEW_ATTRS.add("textColor");
        VIEW_ATTRS.add("srcCompact");
        VIEW_ATTRS.add("drawableLeft");
        VIEW_ATTRS.add("drawableRight");
        VIEW_ATTRS.add("drawableTop");
        VIEW_ATTRS.add("drawableBottom");

    }

    private List<SkinView> mSkinViews = new ArrayList<>();

    public void load(View view, AttributeSet attrs) {

        List<SkinPair> skinPairs = new ArrayList<>();

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attributeName = attrs.getAttributeName(i);

            if (VIEW_ATTRS.contains(attributeName)) {
                String attributeValue = attrs.getAttributeValue(i);
                //比如写死颜色值#ffffff，则跳过，不处理
                if (attributeValue.startsWith("#")){
                    continue;
                }

                int resId;
                //取主题的预定义属性
                if (attributeValue.startsWith("?")) {

                    int attrId = Integer.valueOf(attributeValue.substring(1));

                    resId = SkinThemeUtils.getResIds(view.getContext(), new int[]{attrId})[0];

                } else {
                    resId = Integer.valueOf(attributeValue.substring(1));
                }

                if (resId != 0) {
                    SkinPair skinPair = new SkinPair(attributeName, resId);
                    skinPairs.add(skinPair);
                }

            }

            if (!skinPairs.isEmpty()) {
                SkinView skinView = new SkinView(view, skinPairs);
                skinView.applySkin();
                mSkinViews.add(skinView);
            }

        }

    }

    public void applySkin() {
        for (SkinView skinView : mSkinViews) {
            skinView.applySkin();
        }
    }


    static class SkinPair{
        String attrName;

        int resId;

        public SkinPair(String attrName, int resId) {
            this.attrName = attrName;
            this.resId = resId;
        }
    }


    static class SkinView{

        View view;

        List<SkinPair> skinPairs;

        public SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        public void applySkin() {
            for (SkinPair skinPair : skinPairs) {
                switch (skinPair.attrName) {
                    case "background":
                        Object background = SkinResources.getInstance().getBackground(skinPair.resId);
                        if (background instanceof Integer) {
                            view.setBackgroundColor((Integer) background);
                        } else {
                            view.setBackgroundDrawable((Drawable) background);
                        }
                        break;
                        default:
                }
            }


        }
    }

}
