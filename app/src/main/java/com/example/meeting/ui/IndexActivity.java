package com.example.meeting.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.framework.Constants;
import com.example.framework.manager.BmobManager;
import com.example.framework.utils.SpUtils;
import com.example.meeting.MainActivity;
import com.example.meeting.R;

public class IndexActivity extends AppCompatActivity {

    private static final int SKIP_MAIN = 1000;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case SKIP_MAIN:
                    startMain();
                    return true;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        SpUtils.getInstance().initSp(this);
        mHandler.sendEmptyMessageDelayed(SKIP_MAIN, 2 * 1000);
    }


    private void startMain() {
        boolean isFirstApp = SpUtils.getInstance().getBoolean(Constants.SP_IS_FIRST_APP, true);
        Intent intent = new Intent();
        if (isFirstApp) {
            intent.setClass(this, GuideActivity.class);
            SpUtils.getInstance().putBoolean(Constants.SP_IS_FIRST_APP, false);
        } else {
            String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
            if (TextUtils.isEmpty(token)) {
                if (BmobManager.getInstance().isLogin()) {
                    intent.setClass(this, MainActivity.class);
                } else {
                    intent.setClass(this, LoginActivity.class);
                }
            } else {
                intent.setClass(this, MainActivity.class);
            }
        }
        startActivity(intent);
        finish();
    }



}