package com.wenjian.coolskin;

import android.app.Application;

import com.wenjian.core.SkinManager;

/**
 * Description: SkinApp
 * Date: 2018/3/20
 *
 * @author jian.wen@ubtrobot.com
 */

public class SkinApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        SkinManager.init(this);

    }
}
