package com.example.framework.manager;

import android.content.Context;

import com.example.framework.bmob.IMUser;
import com.example.framework.helper.FileHelper;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

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

    public void uploadFirstPhoto(String nickName, File uploadFile, OnUploadPhotoListener listener) {
        IMUser user = getUser();
        BmobFile bmobFile = new BmobFile(uploadFile);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    user.setNickName(nickName);
                    user.setPhoto(bmobFile.getFileUrl());
                    user.setTokenNickName(nickName);
                    user.setTokenPhoto(bmobFile.getFileUrl());

                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                listener.OnUpdateDone();
                            } else {
                                listener.OnUpdateFail(e);
                            }
                        }
                    });
                } else {
                    listener.OnUpdateFail(e);
                }
            }
        });
    }

    public interface OnUploadPhotoListener {
        void OnUpdateDone();

        void OnUpdateFail(BmobException e);
    }

    public void queryPhoneUser(String phone, FindListener<IMUser> listener) {
        baseQuery("mobilePhoneNumber", phone, listener);
    }

    public void baseQuery(String key, String values, FindListener listener) {
        BmobQuery<IMUser> query = new BmobQuery<>();
        query.addWhereEqualTo(key, values);
        query.findObjects(listener);
    }

    public void queryAllUser(FindListener<IMUser> listener) {
        BmobQuery<IMUser> query = new BmobQuery<>();
        query.findObjects(listener);
    }

}
