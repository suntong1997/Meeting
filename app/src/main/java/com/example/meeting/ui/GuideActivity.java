package com.example.meeting.ui;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.framework.base.BasePageAdapter;
import com.example.framework.base.BaseUIActivity;
import com.example.framework.manager.MediaPlayerManager;
import com.example.framework.utils.AnimUtils;
import com.example.meeting.R;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseUIActivity implements View.OnClickListener {

    private ImageView iv_music_switch;
    private TextView tv_guide_skip;
    private ImageView iv_guide_point_1;
    private ImageView iv_guide_point_2;
    private ImageView iv_guide_point_3;
    private View view1;
    private View view2;
    private View view3;
    private ViewPager mViewLPager;

    private List<View> mPageList = new ArrayList<>();
    private BasePageAdapter mPageAdapter;

    private MediaPlayerManager mGuideMusic;

    private ObjectAnimator mAnim;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
    }

    private void initView() {
        iv_music_switch = findViewById(R.id.iv_music_switch);
        tv_guide_skip = findViewById(R.id.tv_guide_skip);
        iv_guide_point_1 = findViewById(R.id.iv_guide_point_1);
        iv_guide_point_2 = findViewById(R.id.iv_guide_point_2);
        iv_guide_point_3 = findViewById(R.id.iv_guide_point_3);
        mViewLPager = findViewById(R.id.mViewPager);

        iv_music_switch.setOnClickListener(this);
        tv_guide_skip.setOnClickListener(this);

        view1 = View.inflate(this, R.layout.layout_pager_guide_1, null);
        view2 = View.inflate(this, R.layout.layout_pager_guide_2, null);
        view3 = View.inflate(this, R.layout.layout_pager_guide_3, null);

        mPageList.add(view1);
        mPageList.add(view2);
        mPageList.add(view3);

        mViewLPager.setOffscreenPageLimit(mPageList.size());

        mPageAdapter = new BasePageAdapter(mPageList);
        mViewLPager.setAdapter(mPageAdapter);

        mViewLPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        startMusic();
    }

    private void selectPoint(int position) {
        switch (position) {
            case 0:
                iv_guide_point_1.setImageResource(R.drawable.img_guide_point_p);
                iv_guide_point_2.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_3.setImageResource(R.drawable.img_guide_point);
                break;
            case 1:
                iv_guide_point_1.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_2.setImageResource(R.drawable.img_guide_point_p);
                iv_guide_point_3.setImageResource(R.drawable.img_guide_point);
                break;
            case 2:
                iv_guide_point_1.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_2.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_3.setImageResource(R.drawable.img_guide_point_p);
                break;
        }
    }

    private void startMusic() {
        mGuideMusic = new MediaPlayerManager();
        mGuideMusic.setLooping(true);

        AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.guide);
        mGuideMusic.startPlay(file);

        mAnim = AnimUtils.rotation(iv_music_switch);
        mAnim.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_music_switch:
                mAnim.resume();
                if (mGuideMusic.MEDIA_STATUS == MediaPlayerManager.MEDIA_STATUS_PAUSE) {
                    mGuideMusic.continuePlay();
                    iv_music_switch.setImageResource(R.drawable.img_guide_music);
                } else if (mGuideMusic.MEDIA_STATUS == MediaPlayerManager.MEDIA_STATUS_PLAY) {
                    mAnim.pause();
                    mGuideMusic.pausePlay();
                    iv_music_switch.setImageResource(R.drawable.img_guide_music_off);
                }
                break;
            case R.id.tv_guide_skip:
                startActivity(new Intent(this, LoginActivity.class));
                mGuideMusic.stopPlay();
                finish();
                break;
        }
    }
}