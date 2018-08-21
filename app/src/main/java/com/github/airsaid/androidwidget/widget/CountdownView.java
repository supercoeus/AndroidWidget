package com.github.airsaid.androidwidget.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.github.airsaid.androidwidget.R;

/**
 * 自定义倒计时 View。
 *
 * @author airsaid
 */
public class CountdownView extends View {

    @ColorInt private int   mBorderColor;
    private           float mBorderWidth;
    @ColorInt private int   mProgressColor;
    private           float mProgressWidth;
    @ColorInt private int   mTextColor;
    private           float mTextSize;
    private           int   mTime;

    private float mCurTime;
    private int   mCurSecond;
    private RectF mRectF = new RectF();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private OnCountdownListener mOnCountdownListener;

    public interface OnCountdownListener {
        /**
         * 倒计时。
         *
         * @param view 倒计时 View
         * @param time 当前时间（单位秒）
         */
        void onCountdown(CountdownView view, int time);
    }

    public CountdownView(Context context) {
        this(context, null);
    }

    public CountdownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountdownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountdownView);
        setBorderColor(a.getColor(R.styleable.CountdownView_cv_borderColor, Color.WHITE));
        setBorderWidth(a.getDimension(R.styleable.CountdownView_cv_borderWidth, 4f));
        setProgressColor(a.getColor(R.styleable.CountdownView_cv_progressColor, Color.WHITE));
        setProgressWidth(a.getDimension(R.styleable.CountdownView_cv_progressWidth, 10f));
        setTextColor(a.getColor(R.styleable.CountdownView_cv_textColor, Color.WHITE));
        setTextSize(a.getDimensionPixelSize(R.styleable.CountdownView_cv_textSize, 28));
        a.recycle();
    }

    /**
     * 设置倒计时监听。
     *
     * @param listener 监听接口
     */
    public void setOnCountdownListener(OnCountdownListener listener) {
        mOnCountdownListener = listener;
    }

    /**
     * 设置倒计时边框颜色。
     *
     * @param color 倒计时边框颜色
     */
    public void setBorderColor(@ColorInt int color) {
        if (color != mBorderColor) {
            mBorderColor = color;
        }
    }

    /**
     * 设置倒计时边框宽度。
     *
     * @param width 倒计时边框宽度
     */
    public void setBorderWidth(float width) {
        if (width != mBorderWidth) {
            mBorderWidth = width;
        }
    }

    /**
     * 设置倒计时进度颜色。
     *
     * @param color 倒计时进度颜色
     */
    public void setProgressColor(@ColorInt int color) {
        if (color != mProgressColor) {
            mProgressColor = color;
        }
    }

    /**
     * 设置倒计时进度宽度。
     *
     * @param width 倒计时进度宽度
     */
    public void setProgressWidth(float width) {
        if (width != mProgressWidth) {
            mProgressWidth = width;
        }
    }

    /**
     * 设置倒计时文字颜色。
     *
     * @param color 文字颜色
     */
    public void setTextColor(@ColorInt int color) {
        if (color != mTextColor) {
            mTextColor = color;
        }
    }

    /**
     * 设置倒计时文字大小。
     *
     * @param size 文字大小
     */
    public void setTextSize(float size) {
        if (size != mTextSize) {
            mTextSize = size;
        }
    }

    /**
     * 设置倒计时时间。单位为秒。
     *
     * @param time 倒计时时间
     */
    public void setTime(int time) {
        if (time != mTime) {
            mTime = time;
        }
    }

    /**
     * 开始倒计时。
     */
    public void startCountdown() {
        startCountdown(mTime);
    }

    /**
     * 开始倒计时并指定倒计时时间。
     *
     * @param time 倒计时时间
     */
    public void startCountdown(int time) {
        setTime(time);
        start();
    }

    private void start() {
        if (mTime <= 0) return;

        ValueAnimator animator = ValueAnimator.ofFloat(0, mTime).setDuration(mTime * 1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurTime = (float) animation.getAnimatedValue();
                int time = mTime - (int) mCurTime;
                if (time != mCurSecond) {
                    mCurSecond = time;
                    if (mOnCountdownListener != null) {
                        mOnCountdownListener.onCountdown(CountdownView.this, time);
                    }
                }
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw border
        mPaint.setColor(mBorderColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        float cx = getWidth() / 2;
        float cy = getHeight() / 2;
        float radius = Math.min(cx, cy) - mProgressWidth / 2;
        canvas.drawCircle(cx, cy, radius, mPaint);
        // draw progress
        if (mTime > 0) {
            mRectF.left = 0 + mProgressWidth / 2;
            mRectF.top = 0 + mProgressWidth / 2;
            mRectF.right = getWidth() - mProgressWidth / 2;
            mRectF.bottom = getHeight() - mProgressWidth / 2;
            mPaint.setColor(mProgressColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(mProgressWidth);
            canvas.drawArc(mRectF, 270, 360 / mTime * mCurTime, false, mPaint);
        }
        // draw count down text
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        String time = String.valueOf(mCurSecond);
        canvas.drawText(time, 0, time.length(), cx - mPaint.measureText(time) / 2,
                cy - (mPaint.ascent() + mPaint.descent()) / 2, mPaint);
    }

}
