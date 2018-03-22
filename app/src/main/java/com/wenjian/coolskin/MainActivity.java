package com.wenjian.coolskin;

import android.Manifest;
import android.content.pm.PermissionInfo;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wenjian.core.SkinManager;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void reset(View view) {
        SkinManager.getInstance().loadSkin("");
    }

    public void changeSkin(View view) {

        File file = new File(Environment.getExternalStorageDirectory(), "skin-debug.apk");

        String absolutePath = file.getAbsolutePath();

        Log.d(TAG, "changeSkin: " + absolutePath);
        SkinManager.getInstance().loadSkin(absolutePath);
    }
}
