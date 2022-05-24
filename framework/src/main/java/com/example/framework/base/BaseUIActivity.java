package com.example.framework.base;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.framework.utils.SystemUI;

public class BaseUIActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.fixSystemUI(this);
    }
}
