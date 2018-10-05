package com.seyfullahpolat.rahatlaticisesler;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.Map;

/**
 * Created by seyfullahpolat on 15/07/2018.
 */

public class RahatlaticiSeslerApp extends Application {
    private Map<String, Object> map;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}

