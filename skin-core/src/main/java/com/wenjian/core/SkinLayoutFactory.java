package com.wenjian.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.wenjian.core.utils.SkinThemeUtils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * @author mac
 * @desc desc
 * @date 2018/3/17
 */

public class SkinLayoutFactory implements LayoutInflater.Factory2, Observer {

    private static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<String, Constructor<? extends View>>();

    private SkinAttribute mSkinAttribute;

    private static final String DOT = ".";

    private Activity mActivity;

    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.webkit.",
            "android.app.",
            "android.view."
    };

    SkinLayoutFactory(Activity activity) {
        this.mActivity = activity;
        this.mSkinAttribute = new SkinAttribute();
        this.mSkinAttribute.setTypeface(SkinThemeUtils.getTypeface(activity));
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view;
        if (name.contains(DOT)) {
            view = createView(name, context, attrs);
        } else {
            view = onCreateView(name, context, attrs);
        }

        if (view != null) {
            mSkinAttribute.load(view, attrs);
        }

        return view;

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;

        for (String prefix : sClassPrefixList) {
            view = createView(prefix + name, context, attrs);
            if (view != null) {
                break;
            }
        }

        return view;
    }

    private Constructor<? extends View> findConstructor(String fullName, Context context) {
        Constructor<? extends View> constructor = sConstructorMap.get(fullName);
        if (constructor == null) {
            try {
                Class<? extends View> clazz = context.getClassLoader()
                        .loadClass(fullName)
                        .asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                sConstructorMap.put(fullName, constructor);

            } catch (Exception e) {
            }
        }

        return constructor;
    }

    private View createView(String fullName, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = findConstructor(fullName, context);
        if (constructor != null) {
            try {
                return constructor.newInstance(context, attrs);
            } catch (Exception e) {
            }
        }

        return null;

    }

    @Override
    public void update(Observable o, Object arg) {
        SkinThemeUtils.updateStatusBarColor(mActivity);
        Typeface typeface = SkinThemeUtils.getTypeface(mActivity);
        mSkinAttribute.setTypeface(typeface);
        mSkinAttribute.applySkin();
    }
}
