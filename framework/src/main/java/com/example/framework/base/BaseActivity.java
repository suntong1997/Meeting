package com.example.framework.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.framework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    private String[] mPermissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private List<String> mPerList = new ArrayList<>();
    private List<String> mPerDeniedList = new ArrayList<>();

    private OnPermissionsResult permissionsResult;
    private int requestCode;

    protected boolean checkPermission(String permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int check = checkSelfPermission(permissions);
            return check == PackageManager.PERMISSION_GRANTED;
        }

        return false;
    }

    protected boolean checkPermissions() {
        mPerList.clear();
        for (int i = 0; i < mPermissions.length; i++) {
            boolean check = checkPermission(mPermissions[i]);
            if (!check) {
                mPerList.add(mPermissions[i]);
            }
        }

        return mPerList.size() > 0 ? false : true;
    }

    protected void request(int requestCode, OnPermissionsResult permissionsResult) {
        if (!checkPermissions()) {
            requestPermissionAll(requestCode, permissionsResult);
        }
    }

    protected void requestPermission(String[] mPermissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(mPermissions, requestCode);
        }
    }

    protected void requestPermissionAll(int requestCode, OnPermissionsResult permissionsResult) {
        this.permissionsResult = permissionsResult;
        this.requestCode = requestCode;
        requestPermission(mPerList.toArray(new String[mPerList.size()]), requestCode);
    }


    protected interface OnPermissionsResult {
        void onSuccess();

        void onFailure(List<String> perDeniedList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPerDeniedList.clear();
        if (requestCode == requestCode) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        mPerDeniedList.add(permissions[i]);
                    }
                }
                if (permissionsResult != null) {
                    if (mPerDeniedList.size() == 0) {
                        permissionsResult.onSuccess();
                    } else {
                        permissionsResult.onFailure(mPerDeniedList);
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected boolean checkWindowPermissins() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);
        }
        return true;
    }

    protected void requestWindowPermissions(int requestCode) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, requestCode);

    }


}
