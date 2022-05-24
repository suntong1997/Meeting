package com.example.meeting.ui;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import com.example.framework.base.BaseUIActivity;
import com.example.framework.bmob.IMUser;
import com.example.framework.manager.BmobManager;
import com.example.framework.utils.LogUtils;
import com.example.meeting.R;

import java.util.List;

public class MainActivity extends BaseUIActivity {

    private static final int PERMISSION_REQUEST_CODE=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        request(PERMISSION_REQUEST_CODE, new OnPermissionsResult() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(List<String> perDeniedList) {
                LogUtils.i("Denied Permissions:" + perDeniedList.toString());
            }
        });
    }
}