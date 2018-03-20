package com.wenjian.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * @author mac
 * @desc desc
 * @date 2018/3/17
 */

public class SkinLayoutFactory implements LayoutInflater.Factory2 ,Observer{

    static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<String, Constructor<? extends View>>();

    private SkinAttribute mSkinAttribute;

    private static final String [] PKG_PREFIX = {
            "android.view.",
            "android.widget.",
            "android.webkit."
    };

    SkinLayoutFactory(){
        this.mSkinAttribute = new SkinAttribute();
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        View view = null;
        if (name.contains(".")) {
            view = createView(name,context,attrs);
        } else {
            view = onCreateView(name, context, attrs);
        }

        if (view != null) {
            mSkinAttribute.load(view,attrs);
        }

        return view;

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;

        for (String pkgPrefix : PKG_PREFIX) {
            view = createView(pkgPrefix + name, context, attrs);
            if (view != null) {
                break;
            }
        }

        return view;
    }

    private View createView(String fullName, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = sConstructorMap.get(fullName);
        if (constructor == null) {
            try {
                Class<? extends View> clazz = context.getClassLoader().loadClass(fullName).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                sConstructorMap.put(fullName,constructor);

            } catch (Exception e) {
            }
        }

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
        mSkinAttribute.applySkin();
    }
}
