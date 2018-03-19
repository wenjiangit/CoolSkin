package com.wenjian.skin_core;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

    public SkinLayoutFactory(){
        this.mSkinAttribute = new SkinAttribute();
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        View view = null;
        if (!name.contains(".")) {
            view = onCreateView(name, context, attrs);
        } else {
            view = createView(name,context,attrs);
        }

        mSkinAttribute.load(view,attrs);

        return view;

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return createView("android.view." + name, context, attrs);
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
                e.printStackTrace();
            }
        }

        if (constructor != null) {
            try {
                constructor.newInstance(context, attrs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    @Override
    public void update(Observable o, Object arg) {
        mSkinAttribute.applySkin();
    }
}
