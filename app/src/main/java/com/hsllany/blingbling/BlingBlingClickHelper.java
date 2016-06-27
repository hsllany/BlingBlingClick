package com.hsllany.blingbling;

import android.app.Activity;
import android.view.ViewGroup;

/**
 * @author Yang Tao on 16/6/27.
 */
public class BlingBlingClickHelper {

    public static void enableBlingBlingClick(Activity activity) {
        BlingBlingClickSurfaceView imageView = new BlingBlingClickSurfaceView(activity);
        imageView.setZOrderOnTop(true);
        activity.addContentView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
