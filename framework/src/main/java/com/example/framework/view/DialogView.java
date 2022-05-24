package com.example.framework.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogView extends Dialog {
    public DialogView(@NonNull Context context) {
        super(context);
    }

    public DialogView(@NonNull Context mContext, int layout, int themeResId, int gravity) {
        super(mContext, themeResId);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
    }

}
