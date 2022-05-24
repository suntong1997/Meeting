package com.example.meeting.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.framework.Constants;
import com.example.framework.base.BaseUIActivity;
import com.example.framework.bmob.IMUser;
import com.example.framework.manager.BmobManager;
import com.example.framework.manager.DialogManager;
import com.example.framework.utils.SpUtils;
import com.example.framework.view.LoadingView;
import com.example.meeting.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

public class LoginActivity extends BaseUIActivity implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_code;
    private Button btn_send_code;
    private Button btn_login;
    private LoadingView mLoadingView;

    private static final int H_TIME = 1001;
    private static int TIME = 60;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case H_TIME:
                    TIME--;
                    btn_send_code.setText(TIME + "s");
                    if (TIME > 0) {
                        mHandler.sendEmptyMessageDelayed(H_TIME, 1000);
                    } else {
                        btn_send_code.setEnabled(true);
                        btn_send_code.setText(getString(R.string.text_login_send));
                        TIME = 60;
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        initDialogView();

        btn_send_code = findViewById(R.id.btn_send_code);
        btn_login = findViewById(R.id.btn_login);
        et_phone = findViewById(R.id.et_phone);
        et_code = findViewById(R.id.et_code);

        btn_send_code.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        et_phone.setOnClickListener(this);
        et_code.setOnClickListener(this);

        String phone = SpUtils.getInstance().getString(Constants.SP_PHONE, "");
        if (!TextUtils.isEmpty(phone)) {
            et_phone.setText(phone);
        }
    }

    private void initDialogView() {
        mLoadingView = new LoadingView(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_code:
                sendSMS();
                break;
            case R.id.btn_login:
                login();
                break;
        }

    }

    private void sendSMS() {
        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, getString(R.string.text_login_phone_null), Toast.LENGTH_SHORT).show();
            return;
        }
        BmobManager.getInstance().requestSMS(phone, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    mHandler.sendEmptyMessage(H_TIME);
                    btn_send_code.setEnabled(false);
                    Toast.makeText(LoginActivity.this, "短信验证码发送成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "短信验证码发送失败", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void login() {
        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, getString(R.string.text_login_phone_null), Toast.LENGTH_SHORT).show();
            return;
        }
        String code = et_code.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, getString(R.string.text_login_code_null), Toast.LENGTH_SHORT).show();
            return;
        }

        mLoadingView.show("正在登陆...");
        BmobManager.getInstance().signOrLoginByMobilePhone(phone, code, new LogInListener<IMUser>() {
            @Override
            public void done(IMUser imUser, BmobException e) {
                if (e == null) {
                    mLoadingView.hide();
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    SpUtils.getInstance().putString(Constants.SP_PHONE, phone);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    mLoadingView.hide();
                    Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}