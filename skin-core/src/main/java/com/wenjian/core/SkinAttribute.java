package com.wenjian.core;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjian.core.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mac
 * @desc desc
 * @date 2018/3/17
 */

class SkinAttribute {
    private static final String TAG = "SkinAttribute";

    private static List<String> sViewAttrs = new ArrayList<>();

    private Typeface mTypeface;

    SkinAttribute() {
    }

    static {
        sViewAttrs.add("background");
        sViewAttrs.add("src");
        sViewAttrs.add("textColor");
        sViewAttrs.add("srcCompact");
        sViewAttrs.add("drawableLeft");
        sViewAttrs.add("drawableRight");
        sViewAttrs.add("drawableTop");
        sViewAttrs.add("drawableBottom");
    }

    private List<SkinView> mSkinViews = new ArrayList<>();

    void load(View view, AttributeSet attrs) {

        List<SkinPair> skinPairs = new ArrayList<>();

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attributeName = attrs.getAttributeName(i);

            if (sViewAttrs.contains(attributeName)) {
                String attributeValue = attrs.getAttributeValue(i);
                //比如写死颜色值#ffffff，则跳过，不处理
                if (attributeValue.startsWith("#")) {
                    continue;
                }

                int resId;
                //取主题的预定义属性
                if (attributeValue.startsWith("?")) {

                    int attrId = Integer.valueOf(attributeValue.substring(1));

                    resId = SkinThemeUtils.getResIdByAttrId(view.getContext(), new int[]{attrId})[0];

                } else {
                    resId = Integer.valueOf(attributeValue.substring(1));
                }

                if (resId != 0) {
                    SkinPair skinPair = new SkinPair(attributeName, resId);
                    skinPairs.add(skinPair);
                }

            }

            //收集view,包含特定属性,或者是TextView,用来更换字体
            if (!skinPairs.isEmpty() || view instanceof TextView) {
                SkinView skinView = new SkinView(view, skinPairs);
                skinView.applySkin(mTypeface);
                mSkinViews.add(skinView);
            }
        }

        Log.d(TAG, "mSkinViews: " + mSkinViews);

    }

    void applySkin() {
        for (SkinView skinView : mSkinViews) {
            skinView.applySkin(mTypeface);
        }
    }

    void setTypeface(Typeface typeface) {
        mTypeface = typeface;
    }


    static class SkinPair {
        String attrName;

        int resId;

        SkinPair(String attrName, int resId) {
            this.attrName = attrName;
            this.resId = resId;
        }

        @Override
        public String toString() {
            return "SkinPair{" +
                    "attrName='" + attrName + '\'' +
                    ", resId=" + resId +
                    '}';
        }
    }


    static class SkinView {

        View view;

        List<SkinPair> skinPairs;

        SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        void applySkin(Typeface typeface) {
            applyTypeface(typeface);
            for (SkinPair skinPair : skinPairs) {
                Drawable left = null, right = null, top = null, bottom = null;
                switch (skinPair.attrName) {
                    case "background":
                        Object background = SkinResources.getInstance().getBackground(skinPair.resId);
                        if (background instanceof Integer) {
                            view.setBackgroundColor((Integer) background);
                        } else {
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case "src":
                    case "srcCompact":
                        Object src = SkinResources.getInstance().getBackground(skinPair.resId);
                        if (src instanceof Integer) {
                            ((ImageView) view).setImageDrawable(new ColorDrawable((Integer) src));
                        } else {
                            ((ImageView) view).setImageDrawable((Drawable) src);
                        }
                        break;
                    case "textColor":
                        ColorStateList colorStateList = SkinResources.getInstance().getColorStateList(skinPair.resId);
                        ((TextView) view).setTextColor(colorStateList);
                        break;

                    case "drawableLeft":
                        left = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;

                    case "drawableRight":
                        right = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;

                    case "drawableTop":
                        top = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;

                    case "drawableBottom":
                        bottom = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    default:
                }

                if (left != null || right != null || top != null || bottom != null) {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
                }
            }


        }


        private void applyTypeface(Typeface typeface) {
            if (view instanceof TextView) {
                ((TextView)view).setTypeface(typeface);
            }
        }

        @Override
        public String toString() {
            return "SkinView{" +
                    "view=" + view +
                    ", skinPairs=" + skinPairs +
                    '}';
        }
    }

}
