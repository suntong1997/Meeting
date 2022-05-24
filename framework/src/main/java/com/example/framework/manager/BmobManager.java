package com.example.framework.manager;

import android.content.Context;

import com.example.framework.bmob.IMUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

public class BmobManager {
    private volatile static BmobManager mInstance = null;

    private static final String BMOB_SDK_ID = "b78530a0aa2a6a829ed72bf6db9252f7";

    private BmobManager() {
    }

    public static BmobManager getInstance() {
        if (mInstance == null) {
            synchronized (BmobManager.class) {
                if (mInstance == null) {
                    mInstance = new BmobManager();
                }
            }
        }
        return mInstance;
    }

    public void initBmob(Context mContext) {
        Bmob.initialize(mContext, BMOB_SDK_ID);
    }

    public void requestSMS(String phone, QueryListener<Integer> listener) {
        BmobSMS.requestSMSCode(phone, "", listener);
    }

    public void signOrLoginByMobilePhone(String phone, String code, LogInListener<IMUser> listener) {
        BmobUser.signOrLoginByMobilePhone(phone, code, listener);
    }

    public IMUser getUser() {
        return BmobUser.getCurrentUser(IMUser.class);
    }


    public boolean isLogin() {
        return BmobUser.isLogin();
    }
}
