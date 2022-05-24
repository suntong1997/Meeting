package com.example.framework.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.framework.R;
import com.example.framework.manager.DialogManager;
import com.example.framework.utils.AnimUtils;

public class LoadingView {

    private ImageView iv_loading;
    private TextView tv_loading_text;
    private DialogView mLoadingView;
    private ObjectAnimator mAnim;

    public LoadingView(Context mContext) {
        mLoadingView = DialogManager.getInstance().initView(mContext, R.layout.dialog_loding);
        iv_loading = mLoadingView.findViewById(R.id.iv_loding);
        tv_loading_text = mLoadingView.findViewById(R.id.tv_loding_text);
        mAnim = AnimUtils.rotation(iv_loading);
    }

    public void setLoadingText(String text) {
        if (!TextUtils.isEmpty(text)) {
            tv_loading_text.setText(text);
        }
    }

    public void show() {
        DialogManager.getInstance().show(mLoadingView);
        mAnim.start();
    }

    public void show(String text) {
        setLoadingText(text);
        mAnim.start();
        DialogManager.getInstance().show(mLoadingView);
    }


    public void hide() {
        mAnim.pause();
        DialogManager.getInstance().hide(mLoadingView);
    }


}
