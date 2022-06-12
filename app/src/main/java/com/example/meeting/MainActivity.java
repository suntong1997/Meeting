package com.example.meeting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.framework.Constants;
import com.example.framework.base.BaseUIActivity;
import com.example.framework.json.TokenBean;
import com.example.framework.manager.BmobManager;
import com.example.framework.manager.DialogManager;
import com.example.framework.manager.HttpManager;
import com.example.framework.utils.LogUtils;
import com.example.framework.utils.SpUtils;
import com.example.framework.view.DialogView;
import com.example.meeting.fragment.ChatFragment;
import com.example.meeting.fragment.MeFragment;
import com.example.meeting.fragment.SquareFragment;
import com.example.meeting.fragment.StarFragment;
import com.example.meeting.service.CloudService;
import com.example.meeting.ui.FirstUploadActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseUIActivity implements View.OnClickListener {

    //星球
    private ImageView iv_star;
    private TextView tv_star;
    private LinearLayout ll_star;
    private StarFragment mStarFragment = null;
    private FragmentTransaction mStarTransaction = null;

    //广场
    private ImageView iv_square;
    private TextView tv_square;
    private LinearLayout ll_square;
    private SquareFragment mSquareFragment = null;
    private FragmentTransaction mSquareTransaction = null;

    //聊天
    private ImageView iv_chat;
    private TextView tv_chat;
    private LinearLayout ll_chat;
    private ChatFragment mChatFragment = null;
    private FragmentTransaction mChatTransaction = null;

    //我的
    private ImageView iv_me;
    private TextView tv_me;
    private LinearLayout ll_me;
    private MeFragment mMeFragment = null;
    private FragmentTransaction mMeTransaction = null;

    private static final int PERMISSION_REQUEST_CODE = 1000;
    private static final int UPLOAD_REQUEST_CODE = 1002;

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void requestPermission() {
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


    private void initView() {
        iv_star = (ImageView) findViewById(R.id.iv_star);
        tv_star = (TextView) findViewById(R.id.tv_star);
        ll_star = (LinearLayout) findViewById(R.id.ll_star);

        iv_square = (ImageView) findViewById(R.id.iv_square);
        tv_square = (TextView) findViewById(R.id.tv_square);
        ll_square = (LinearLayout) findViewById(R.id.ll_square);

        iv_chat = (ImageView) findViewById(R.id.iv_chat);
        tv_chat = (TextView) findViewById(R.id.tv_chat);
        ll_chat = (LinearLayout) findViewById(R.id.ll_chat);

        iv_me = (ImageView) findViewById(R.id.iv_me);
        tv_me = (TextView) findViewById(R.id.tv_me);
        ll_me = (LinearLayout) findViewById(R.id.ll_me);

        ll_star.setOnClickListener(this);
        ll_square.setOnClickListener(this);
        ll_chat.setOnClickListener(this);
        ll_me.setOnClickListener(this);

        tv_star.setText(getString(R.string.text_main_star));
        tv_chat.setText(getString(R.string.text_main_chat));
        tv_square.setText(getString(R.string.text_main_square));
        tv_me.setText(getString(R.string.text_main_me));

        requestPermission();
        initFragment();

        checkMainTab(0);
        checkToken();
    }


    private void initFragment() {
        if (mStarFragment == null) {
            mStarFragment = new StarFragment();
            mStarTransaction = getSupportFragmentManager().beginTransaction();
            mStarTransaction.add(R.id.mMainLayout, mStarFragment);
            mStarTransaction.commit();
        }
        if (mChatFragment == null) {
            mChatFragment = new ChatFragment();
            mChatTransaction = getSupportFragmentManager().beginTransaction();
            mChatTransaction.add(R.id.mMainLayout, mChatFragment);
            mChatTransaction.commit();
        }
        if (mSquareFragment == null) {
            mSquareFragment = new SquareFragment();
            mSquareTransaction = getSupportFragmentManager().beginTransaction();
            mSquareTransaction.add(R.id.mMainLayout, mSquareFragment);
            mSquareTransaction.commit();
        }
        if (mMeFragment == null) {
            mMeFragment = new MeFragment();
            mMeTransaction = getSupportFragmentManager().beginTransaction();
            mMeTransaction.add(R.id.mMainLayout, mMeFragment);
            mMeTransaction.commit();
        }
    }

    private void hideAllFragment(FragmentTransaction transaction) {
        if (mStarFragment != null) {
            transaction.hide(mStarFragment);
            transaction.hide(mSquareFragment);
            transaction.hide(mChatFragment);
            transaction.hide(mMeFragment);
            transaction.commitAllowingStateLoss();
        }
    }

    private void showFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hideAllFragment(transaction);
            transaction.show(fragment);
        }
    }

    private void checkMainTab(int index) {
        switch (index) {
            case 0:

                showFragment(mStarFragment);
                iv_star.setImageResource(R.drawable.img_star_p);
                iv_square.setImageResource(R.drawable.img_square);
                iv_chat.setImageResource(R.drawable.img_chat);
                iv_me.setImageResource(R.drawable.img_me);
                tv_star.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_square.setTextColor((Color.BLACK));
                tv_chat.setTextColor((Color.BLACK));
                tv_me.setTextColor((Color.BLACK));
                break;
            case 1:
                showFragment(mSquareFragment);
                iv_star.setImageResource(R.drawable.img_star);
                iv_square.setImageResource(R.drawable.img_square_p);
                iv_chat.setImageResource(R.drawable.img_chat);
                iv_me.setImageResource(R.drawable.img_me);
                tv_square.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_star.setTextColor((Color.BLACK));
                tv_chat.setTextColor((Color.BLACK));
                tv_me.setTextColor((Color.BLACK));
                break;
            case 2:
                showFragment(mChatFragment);
                iv_star.setImageResource(R.drawable.img_star);
                iv_square.setImageResource(R.drawable.img_square);
                iv_chat.setImageResource(R.drawable.img_chat_p);
                iv_me.setImageResource(R.drawable.img_me);
                tv_chat.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_square.setTextColor((Color.BLACK));
                tv_star.setTextColor((Color.BLACK));
                tv_me.setTextColor((Color.BLACK));
                break;
            case 3:
                showFragment(mMeFragment);
                iv_star.setImageResource(R.drawable.img_star);
                iv_square.setImageResource(R.drawable.img_square);
                iv_chat.setImageResource(R.drawable.img_chat);
                iv_me.setImageResource(R.drawable.img_me_p);
                tv_me.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_square.setTextColor((Color.BLACK));
                tv_chat.setTextColor((Color.BLACK));
                tv_star.setTextColor((Color.BLACK));
                break;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_star:
                checkMainTab(0);
                break;
            case R.id.ll_square:
                checkMainTab(1);
                break;
            case R.id.ll_chat:
                checkMainTab(2);
                break;
            case R.id.ll_me:
                checkMainTab(3);
                break;
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (mStarFragment != null && fragment instanceof StarFragment) {
            mStarFragment = (StarFragment) fragment;
        }
        if (mSquareFragment != null && fragment instanceof SquareFragment) {
            mSquareFragment = (SquareFragment) fragment;
        }
        if (mChatFragment != null && fragment instanceof ChatFragment) {
            mChatFragment = (ChatFragment) fragment;
        }
        if (mMeFragment != null && fragment instanceof MeFragment) {
            mMeFragment = (MeFragment) fragment;
        }
    }

    private void checkToken() {
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        if (!TextUtils.isEmpty(token)) {
            startCloundService();
        } else {
            String tokenPhoto = BmobManager.getInstance().getUser().getTokenPhoto();
            String tokenName = BmobManager.getInstance().getUser().getTokenNickName();
            if (!TextUtils.isEmpty(tokenPhoto) && !TextUtils.isEmpty(tokenName)) {
                createToken();
            } else {
                createUploadDialog();
            }
        }
    }

    private void createUploadDialog() {
        DialogView mUploadView = DialogManager.getInstance().initView(this, R.layout.dialog_first_upload);
        ImageView iv_go_upload = mUploadView.findViewById(R.id.iv_go_upload);
        mUploadView.setCancelable(false);
        iv_go_upload.setOnClickListener(view -> {
            DialogManager.getInstance().hide(mUploadView);
            FirstUploadActivity.startActivity(MainActivity.this, UPLOAD_REQUEST_CODE);
        });
        DialogManager.getInstance().show(mUploadView);
    }

    private void createToken() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", BmobManager.getInstance().getUser().getObjectId());
        map.put("name", BmobManager.getInstance().getUser().getTokenNickName());
        map.put("portraitUri", BmobManager.getInstance().getUser().getTokenPhoto());

        disposable = Observable.create((ObservableOnSubscribe<String>) emitter -> {
                    String json = HttpManager.getInstance().postCloudToken(map);
                    emitter.onNext(json);
                }).subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<String>) s -> parsingCloundToken(s));
    }

    private void parsingCloundToken(String s) {
        TokenBean tokenBean = new Gson().fromJson(s, TokenBean.class);
        if (tokenBean.getCode() == 200) {
            if (TextUtils.isEmpty(tokenBean.getToken())) {
                SpUtils.getInstance().getString(Constants.SP_TOKEN, tokenBean.getToken());
                startCloundService();
            }
        }
    }

    private void startCloundService() {
        startService(new Intent(this, CloudService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}