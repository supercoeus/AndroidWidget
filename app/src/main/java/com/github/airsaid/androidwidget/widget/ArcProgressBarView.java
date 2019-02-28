package com.github.airsaid.androidwidget.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.math.MathUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import com.github.airsaid.androidwidget.R;

/**
 * 自定义弧形进度条。支持 {@link ArcProgressBarView#setMin(int)} 和 {@link ArcProgressBarView#setMax(int)} (int)} 方法。
 * <p>
 * TOTO：
 * <p>
 * 1，对 Progress 功能进行抽象。
 * 2，加入自定义动画支持。
 * 3，加入多线程支持。
 * 4，更多自定义样式。
 *
 * @author airsaid
 */
public class ArcProgressBarView extends View {

    private static final String VISUAL_PROGRESS = "visualProgress";

    /** Progress 动画插值器。 */
    private static final DecelerateInterpolator PROGRESS_ANIM_INTERPOLATOR =
            new DecelerateInterpolator();

    /**
     * Progress 进度改变监听回调接口。
     */
    public interface OnProgressBarChangeListener {

        /**
         * 当 Progress 改变时回调该方法。
         * <p>
         * 值的范围通过 {@link ArcProgressBarView#setMin(int)} 和 {@link ArcProgressBarView#setMax(int)} 方法指定。
         *
         * @param progressBar 当前 {@link ArcProgressBarView} View。
         * @param progress    进度值。
         */
        void onProgressChanged(ArcProgressBarView progressBar, int progress);
    }

    private OnProgressBarChangeListener mOnProgressBarChangeListener;

    /** 进度条的宽度 */
    private float mProgressWidth;
    /** 进度条的颜色 */
    private int   mProgressColor;
    /** 进度条背景的颜色 */
    private int   mBackgroundColor;

    /** 进度条进度 */
    private int mProgress;
    /** 进度条最小进度 */
    private int mMin;
    /** 进度条最大进度 */
    private int mMax;

    /** 进度条动画持续时间（单位毫秒） */
    private long    mDuration;
    /** 进度条动画每次是否需要重头开始 */
    private boolean mIsRestartAnimation;

    /** 视觉用的 progress 值，范围在 [0...1] */
    private float mVisualProgress;

    private Paint          mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF          mProgressRectF;
    private ObjectAnimator mProgressAnimator;

    public ArcProgressBarView(Context context) {
        this(context, null);
    }

    public ArcProgressBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressBarView);
        setProgressWidth(a.getDimension(R.styleable.ArcProgressBarView_apbv_progressWidth, 20));
        setProgressColor(a.getColor(R.styleable.ArcProgressBarView_apbv_progressColor, Color.BLUE));
        setBackgroundColor(a.getColor(R.styleable.ArcProgressBarView_apbv_progressBackground, Color.GRAY));
        setRestartAnimation(a.getBoolean(R.styleable.ArcProgressBarView_apbv_isRestartAnimation, false));
        setDuration(a.getInt(R.styleable.ArcProgressBarView_apbv_duration, 300));
        setProgress(a.getInt(R.styleable.ArcProgressBarView_android_progress, 0));
        setMin(a.getInt(R.styleable.ArcProgressBarView_apbv_min, 0));
        setMax(a.getInt(R.styleable.ArcProgressBarView_apbv_max, 100));
        a.recycle();
    }

    public synchronized void setProgress(int progress) {
        setProgressInternal(progress, false);
    }

    public synchronized void setProgress(int progress, boolean animator) {
        setProgressInternal(progress, animator);
    }

    synchronized boolean setProgressInternal(int progress, boolean animate) {
        progress = MathUtils.clamp(progress, mMin, mMax);

        if (progress == mProgress) {
            return false;
        }

        mProgress = progress;
        refreshProgress(mProgress, animate);
        return true;
    }

    synchronized void refreshProgress(int progress, boolean animate) {
        float range = mMax - mMin;
        final float scale = range > 0f ? (progress - mMin) / range : 0f;

        if (animate) {
            if (mIsRestartAnimation) mVisualProgress = 0;
            mProgressAnimator = ObjectAnimator.ofFloat(this, VISUAL_PROGRESS, mVisualProgress, scale);
            mProgressAnimator.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                onProgressRefresh((int) (value * range + mMin));
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mProgressAnimator.setAutoCancel(true);
            }
            mProgressAnimator.setInterpolator(PROGRESS_ANIM_INTERPOLATOR);
            mProgressAnimator.setDuration(mDuration);
            mProgressAnimator.start();
        } else {
            setVisualProgress(scale);
            onProgressRefresh(progress);
        }
    }

    void setVisualProgress(float progress) {
        mVisualProgress = progress;
        invalidate();
    }

    void onProgressRefresh(int progress) {
        if (mOnProgressBarChangeListener != null) {
            mOnProgressBarChangeListener.onProgressChanged(this, progress);
        }
    }

    public synchronized float getProgressWidth() {
        return mProgressWidth;
    }

    public synchronized void setProgressWidth(float progressWidth) {
        mProgressWidth = progressWidth;
        // 更新 Progress 区域
        int width = getMeasuredWidth();
        if (width > 0) {
            float half = mProgressWidth / 2f;
            float right = getMeasuredWidth() - half;
            float bottom = getMeasuredHeight() - half;
            setProgressRectF(half, half, right, bottom);
        }
    }

    synchronized void setProgressRectF(float left, float top, float right, float bottom) {
        mProgressRectF = new RectF(left, top, right, bottom);
    }

    synchronized RectF getProgressRectF() {
        if (mProgressRectF == null) {
            float half = mProgressWidth / 2f;
            float right = getMeasuredWidth() - half;
            float bottom = getMeasuredHeight() - half;
            setProgressRectF(half, half, right, bottom);
        }
        return mProgressRectF;
    }

    public synchronized int getProgressColor() {
        return mProgressColor;
    }

    public synchronized void setProgressColor(int progressColor) {
        mProgressColor = progressColor;
    }

    public synchronized int getBackgroundColor() {
        return mBackgroundColor;
    }

    public synchronized void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public synchronized int getProgress() {
        return mProgress;
    }

    public synchronized int getMin() {
        return mMin;
    }

    public synchronized void setMin(int min) {
        mMin = min;
        if (mProgress < mMin) {
            mProgress = mMin;
            refreshProgress(mProgress, false);
        }
    }

    public synchronized int getMax() {
        return mMax;
    }

    public synchronized void setMax(int max) {
        mMax = max;
        if (mProgress > mMax) {
            mProgress = mMax;
            refreshProgress(mProgress, false);
        }
    }

    public synchronized long getDuration() {
        return mDuration;
    }

    public synchronized void setDuration(long duration) {
        mDuration = duration;
    }

    public synchronized boolean isRestartAnimation() {
        return mIsRestartAnimation;
    }

    public synchronized void setRestartAnimation(boolean restartAnimation) {
        mIsRestartAnimation = restartAnimation;
    }

    public synchronized boolean isAnimating() {
        return mProgressAnimator != null && mProgressAnimator.isRunning();
    }

    public synchronized void setOnProgressBarChangeListener(OnProgressBarChangeListener listener) {
        mOnProgressBarChangeListener = listener;
    }

    public synchronized OnProgressBarChangeListener getOnProgressBarChangeListener() {
        return mOnProgressBarChangeListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawProgress(canvas);
    }

    void drawBackground(Canvas canvas) {
        mPaint.setColor(mBackgroundColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mProgressWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(getProgressRectF(), 165, 210, false, mPaint);
    }

    void drawProgress(Canvas canvas) {
        mPaint.setColor(mProgressColor);
        canvas.drawArc(getProgressRectF(), 165, 210f * mVisualProgress, false, mPaint);
    }

}
