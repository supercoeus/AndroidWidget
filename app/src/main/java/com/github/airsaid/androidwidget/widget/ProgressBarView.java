package com.github.airsaid.androidwidget.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.github.airsaid.androidwidget.R;


/**
 * The progress bar view.
 *
 * @author airsaid
 */
public class ProgressBarView extends View {

    private float mProgress;
    private float mProgressWidth;
    private int mProgressColor;
    private int mProgressBackground;
    private float mProgressTextSize;
    private int mProgressTextColor;
    private String mDescribeText;
    private float mDescribeTextSize;
    private int mDescribeTextColor;
    private float mDescribeOffsetY;
    private float mStartAngle;

    private Paint mPaint;
    private RectF mArcRectF;
    private Rect mTextRect;
    private long mDuration;

    public ProgressBarView(Context context) {
        this(context, null);
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.progressBarViewStyle);
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTextRect = new Rect();
        mArcRectF = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressBarView, defStyleAttr, 0);
        setProgress(a.getFloat(R.styleable.ProgressBarView_pbv_progress, 0f));
        setProgressWidth(a.getDimension(R.styleable.ProgressBarView_pbv_progressWidth, 0f));
        setProgressColor(a.getColor(R.styleable.ProgressBarView_pbv_progressColor, Color.BLUE));
        setProgressBackground(a.getColor(R.styleable.ProgressBarView_pbv_progressBackground, 0));
        setProgressTextSize(a.getDimension(R.styleable.ProgressBarView_pbv_progressTextSize, 16f));
        setProgressTextColor(a.getColor(R.styleable.ProgressBarView_pbv_progressTextColor, Color.WHITE));
        setDescribeText(a.getString(R.styleable.ProgressBarView_pbv_describeText));
        setDescribeTextSize(a.getDimension(R.styleable.ProgressBarView_pbv_describeTextSize, 0f));
        setDescribeTextColor(a.getColor(R.styleable.ProgressBarView_pbv_describeTextColor, 0));
        setDescribeOffsetY(a.getDimension(R.styleable.ProgressBarView_pbv_describeOffsetY, 0));
        setStartAngle(a.getFloat(R.styleable.ProgressBarView_pbv_startAngle, 0f));
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgress(canvas);
        drawText(canvas);
    }

    private void drawProgress(Canvas canvas) {
        float size = mProgressWidth / 2f;
        mArcRectF.left = size;
        mArcRectF.top = size;
        mArcRectF.right = getWidth() - size;
        mArcRectF.bottom = getHeight() - size;

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mProgressWidth);

        // draw progress background.
        mPaint.setColor(mProgressBackground);
        canvas.drawArc(mArcRectF, 0f, 360f, false, mPaint);
        // draw progress
        mPaint.setColor(mProgressColor);
        canvas.drawArc(mArcRectF, mStartAngle, mProgress * 3.6f, false, mPaint);
    }

    private void drawText(Canvas canvas) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        mPaint.setStyle(Paint.Style.FILL);

        // draw progress text
        mPaint.setTextSize(mDescribeTextSize);
        float descTextHeight = getTextHeight(mDescribeText);

        mPaint.setColor(mProgressTextColor);
        mPaint.setTextSize(mProgressTextSize);
        String progressText = (int) mProgress + "%";
        float progressTextWidth = getTextWidth(progressText);
        float progressTextX = centerX - progressTextWidth / 2;
        float progressTextY = centerY - (mPaint.ascent() + mPaint.descent()) / 2 - (descTextHeight / 2);
        canvas.drawText(progressText, progressTextX, progressTextY, mPaint);

        // draw describe text
        if (!TextUtils.isEmpty(mDescribeText)) {
            mPaint.setColor(mDescribeTextColor);
            mPaint.setTextSize(mDescribeTextSize);
            float describeTextWidth = getTextWidth(mDescribeText);
            float describeTextX = centerX - describeTextWidth / 2;
            float describeTextY = progressTextY + mDescribeOffsetY + descTextHeight;
            canvas.drawText(mDescribeText, describeTextX, describeTextY, mPaint);
        }
    }

    private float getTextWidth(String text) {
        if (!TextUtils.isEmpty(text)) {
            return mPaint.measureText(text);
        }
        return 0;
    }

    private float getTextHeight(String text) {
        if (!TextUtils.isEmpty(text)) {
            mPaint.getTextBounds(text, 0, text.length(), mTextRect);
            return mTextRect.height();
        }
        return 0;
    }

    public void setProgress(@FloatRange(from = 0, to = 100) float progress) {
        setProgress(progress, false);
    }

    public void setProgress(float progress, boolean animator) {
        if (animator) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", 0, progress);
            objectAnimator.setInterpolator(new DecelerateInterpolator());
            objectAnimator.setDuration(mDuration);
            objectAnimator.start();
        } else {
            if (progress != mProgress) {
                mProgress = progress;
                invalidate();
            }
        }
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    @FloatRange(from = 0, to = 100)
    public float getProgress() {
        return mProgress;
    }

    public void setProgressWidth(float width) {
        if (width != mProgressWidth) {
            mProgressWidth = width;
            invalidate();
        }
    }

    public float getProgressWidth() {
        return mProgressWidth;
    }

    public void setProgressColor(@ColorInt int progressColor) {
        if (progressColor != mProgressColor) {
            mProgressColor = progressColor;
            invalidate();
        }
    }

    @ColorInt
    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressBackground(@ColorInt int background) {
        if (background != mProgressBackground) {
            mProgressBackground = background;
            invalidate();
        }
    }

    @ColorInt
    public int getProgressBackground() {
        return mProgressBackground;
    }

    public void setProgressTextSize(float textSize) {
        if (textSize != mProgressTextSize) {
            mProgressTextSize = textSize;
            invalidate();
        }
    }

    public float getProgressTextSize() {
        return mProgressTextSize;
    }

    public void setProgressTextColor(@ColorInt int textColor) {
        if (textColor != mProgressTextColor) {
            mProgressTextColor = textColor;
            invalidate();
        }
    }

    @ColorInt
    public int getProgressTextColor() {
        return mProgressTextColor;
    }

    public void setDescribeText(@Nullable String describeText) {
        if (describeText != null && mDescribeText != null && describeText.equals(mDescribeText)) {
            return;
        }
        mDescribeText = describeText;
        invalidate();
    }

    @Nullable
    public String getDescribeText() {
        return mDescribeText;
    }

    public void setDescribeTextSize(float textSize) {
        if (textSize != mDescribeTextSize) {
            mDescribeTextSize = textSize;
            invalidate();
        }
    }

    public float getDescribeTextSize() {
        return mDescribeTextSize;
    }

    public void setDescribeTextColor(@ColorInt int color) {
        if (color != mDescribeTextColor) {
            mDescribeTextColor = color;
            invalidate();
        }
    }

    @ColorInt
    public int getDescribeTextColor() {
        return mDescribeTextColor;
    }

    public void setDescribeOffsetY(float offsetY) {
        if (offsetY != mDescribeOffsetY) {
            mDescribeOffsetY = offsetY;
            invalidate();
        }
    }

    public float getDescribeOffsetY() {
        return mDescribeOffsetY;
    }

    public void setStartAngle(@FloatRange(from = 0f, to = 360f) float startAngle) {
        if (startAngle != mStartAngle) {
            mStartAngle = startAngle;
            invalidate();
        }
    }

    public float getStartAngle() {
        return mStartAngle;
    }
}
