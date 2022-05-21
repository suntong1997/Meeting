package com.example.farmework.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

public class SystemUI {
    public static void fixSystemUI(Activity mActivity){
        mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        mActivity. getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
}
