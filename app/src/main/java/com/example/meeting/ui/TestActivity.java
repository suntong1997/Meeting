package com.example.meeting.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.framework.bmob.MyData;
import com.example.framework.utils.LogUtils;
import com.example.meeting.R;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd;
    Button btnDel;
    Button btnQuery;
    Button bntUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btnAdd = findViewById(R.id.btn_add);
        btnDel = findViewById(R.id.btn_del);
        btnQuery = findViewById(R.id.btn_query);
        bntUpdate = findViewById(R.id.btn_update);

        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
        bntUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                MyData myData = new MyData();
                myData.setName("小明");
                myData.setSex(0);
                myData.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Log.d(TestActivity.class.getSimpleName(), "done: " + s);
                            Toast.makeText(TestActivity.this, "aaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.btn_del:
                break;
            case R.id.btn_query:
                BmobQuery<MyData> bmobQuery = new BmobQuery<>();
                bmobQuery.getObject("77c2765861", new QueryListener<MyData>() {
                    @Override
                    public void done(MyData myData, BmobException e) {
                        if (e == null) {
                            LogUtils.i("mydata:" + myData.getName() + ":" + myData.getSex());
                        } else {
                            LogUtils.i("mydata:" + e.toString());
                        }
                    }
                });
                break;
            case R.id.btn_update:
                break;
        }
    }
}