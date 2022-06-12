package com.example.framework.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class StretchableScrollView extends ScrollView {
    private View mZoomView;
    private int mZoomViewWidth;
    private int mZoomViewHeight;

    private boolean isScrolling = false;
    private float firstPosition;
    private float mScrollRate = 0.3f;
    private float mReplyRate = 0.5f;

    public StretchableScrollView(Context context) {
        super(context);
    }

    public StretchableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StretchableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildAt(0) != null) {
            ViewGroup group = (ViewGroup) getChildAt(0);
            if (group.getChildAt(0) != null) {
                mZoomView = group.getChildAt(0);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mZoomViewWidth <= 0 || mZoomViewHeight <= 0) {
            mZoomViewWidth = mZoomView.getWidth();
            mZoomViewHeight = mZoomView.getHeight();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (!isScrolling) {
                    if (getScrollY() == 0) {
                        firstPosition = ev.getY();
                    } else {
                        break;
                    }
                }
                int distance = (int) ((ev.getY() - firstPosition) * mScrollRate);
                if (distance < 0) {
                    break;
                }
                isScrolling = true;
                setZoomView(distance);
                break;
            case MotionEvent.ACTION_UP:
                isScrolling = false;
                replyZoomView();
        }
        return true;
    }

    private void replyZoomView() {
        int distance = mZoomView.getMeasuredWidth() - mZoomViewWidth;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(distance, 0)
                .setDuration((long) (distance * mReplyRate));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setZoomView((Integer) valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    private void setZoomView(int zoom) {
        if (mZoomViewWidth <= 0 || mZoomViewHeight <= 0) {
            return;
        }
        ViewGroup.LayoutParams lp = mZoomView.getLayoutParams();
        lp.width = mZoomViewWidth + zoom;
        lp.height = mZoomViewHeight * ((mZoomViewWidth + zoom) / mZoomViewWidth);
        ((MarginLayoutParams) lp).setMargins(-(lp.width - mZoomViewWidth) / 2, 0, 0, 0);
        mZoomView.setLayoutParams(lp);
    }
}
