package com.github.airsaid.androidwidget.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.github.airsaid.androidwidget.R;

import java.util.List;


/**
 * @author airsaid
 */
public class StaffView extends View implements Animator.AnimatorListener {

    private OnAnimatorListener mAnimatorListener;
    @ColorInt private int mNormalLabelColor;
    @ColorInt private int mSelectLabelColor;
    private ValueAnimator mValueAnimator;
    private Drawable mCursorDrawable;
    private boolean mIsShowCursor;
    private boolean mCancelCursor;
    private long mCurrentPlayTime;
    private boolean mPauseCursor;
    private float mLabelTextSize;
    private float mExtraTextSize;
    private int mExtraMarginLeft;
    private int mExtraMarginTop;
    private int mLabelMarginTop;
    private Bitmap mStaffBitmap;
    private RectF mStaffRectF;
    private List<Model> mData;
    private Rect mLabelRect;
    private float mValue;
    private Paint mPaint;

    /**
     * An animation listener receives notifications from cursor move animation.
     * Notifications indicate animation related events, such as the start or the
     * end of the animation.
     */
    public interface OnAnimatorListener {

        /**
         * Notifies the start of the animation.
         */
        void onStart();

        /**
         * Notifies the pause of the animation.
         */
        void onPause();

        /**
         * Notifies the resume of the animation.
         */
        void onResume();

        /**
         * Notifies the stops of the animation.
         */
        void onStop();

        /**
         * Notifies the end of the animation.
         */
        void onEnd();
    }

    public StaffView(Context context) {
        this(context, null);
    }

    public StaffView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.staffViewStyle);
    }

    public StaffView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLabelRect = new Rect();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.StaffView, defStyleAttr, 0);

        int staffResId = a.getResourceId(R.styleable.StaffView_sv_staff, 0);
        setStaff(staffResId);

        int normalLabelColor = a.getColor(
                R.styleable.StaffView_sv_normalLabelColor, 0);
        setNormalLabelColor(normalLabelColor);

        int selectLabelColor = a.getColor(
                R.styleable.StaffView_sv_selectLabelColor, 0);
        setSelectLabelColor(selectLabelColor);

        int labelMarginTop = a.getDimensionPixelOffset(
                R.styleable.StaffView_sv_labelMarginTop, 0);
        setLabelMarginTop(labelMarginTop);

        float labelTextSize = a.getDimension(
                R.styleable.StaffView_sv_labelTextSize, 28f);
        setLabelTextSize(labelTextSize);

        int extraMarginTop = a.getDimensionPixelOffset(
                R.styleable.StaffView_sv_extraMarginTop, 0);
        setExtraMarginTop(extraMarginTop);

        int extraMarginLeft = a.getDimensionPixelOffset(
                R.styleable.StaffView_sv_extraMarginLeft, 0);
        setExtraMarginLeft(extraMarginLeft);

        float extraTextSize = a.getDimension(
                R.styleable.StaffView_sv_extraTextSize, 18f);
        setExtraTextSize(extraTextSize);

        Drawable cursorDrawable = a.getDrawable(
                R.styleable.StaffView_sv_cursorDrawable);
        setCursorDrawable(cursorDrawable);

        boolean isShowCursor = a.getBoolean(
                R.styleable.StaffView_sv_showCursor, true);
        setShowCursor(isShowCursor);

        a.recycle();
    }

    /**
     * Set staff bitmap according to image resource id.
     *
     * @param resId image resource id
     */
    public void setStaff(int resId) {
        if (resId != 0) {
            mStaffBitmap = BitmapFactory.decodeResource(getResources(), resId);
        } else {
            mStaffBitmap = null;
        }
        invalidate();
    }

    /**
     * Set default label color according to argb color.
     *
     * @param color The color (including alpha) to set in the normal label text
     */
    public void setNormalLabelColor(@ColorInt int color) {
        if (color != mNormalLabelColor) {
            mNormalLabelColor = color;
            invalidate();
        }
    }

    /**
     * Set select label color according to argb color.
     *
     * @param color The color (including alpha) to set in the select label text
     */
    public void setSelectLabelColor(@ColorInt int color) {
        if (color != mSelectLabelColor) {
            mSelectLabelColor = color;
            invalidate();
        }
    }

    /**
     * Set label margin top.
     *
     * @param marginTop margin top (px)
     */
    public void setLabelMarginTop(@Px int marginTop) {
        if (marginTop != mLabelMarginTop) {
            mLabelMarginTop = marginTop;
            invalidate();
        }
    }

    /**
     * Set the label text size. This value must be > 0.
     *
     * @param size set the label text size in pixel units
     */
    public void setLabelTextSize(float size) {
        if (size != mLabelTextSize) {
            mLabelTextSize = size;
            invalidate();
        }
    }

    /**
     * Set extra margin top.
     *
     * @param marginTop margin top (px)
     */
    public void setExtraMarginTop(@Px int marginTop) {
        if (marginTop != mExtraMarginTop) {
            mExtraMarginTop = marginTop;
            invalidate();
        }
    }

    /**
     * Set extra margin left.
     *
     * @param marginLeft margin left (px)
     */
    public void setExtraMarginLeft(@Px int marginLeft) {
        if (marginLeft != mExtraMarginLeft) {
            mExtraMarginLeft = marginLeft;
            invalidate();
        }
    }

    /**
     * Set the extra text size. This value must be > 0.
     *
     * @param size set the extra text size in pixel units
     */
    public void setExtraTextSize(float size) {
        if (size != mExtraTextSize) {
            mExtraTextSize = size;
            invalidate();
        }
    }

    /**
     * Set cursor drawable.
     *
     * @param drawable {@link Drawable}
     */
    public void setCursorDrawable(Drawable drawable) {
        if (drawable != mCursorDrawable) {
            mCursorDrawable = drawable;
            mCursorDrawable.setBounds(0, 0,
                    drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            invalidate();
        }
    }

    /**
     * Set data list.
     *
     * @param lists {@link Model} list
     */
    public void setData(List<Model> lists) {
        mData = lists;
        invalidate();
    }

    /**
     * Get data list.
     *
     * @return {@link Model} list
     */
    public List<Model> getData() {
        return mData;
    }

    /**
     * Set show or hide cursor.
     *
     * @param isShowCursor true: show, false: hide
     */
    public void setShowCursor(boolean isShowCursor) {
        if (isShowCursor != mIsShowCursor) {
            mIsShowCursor = isShowCursor;
        }
        invalidate();
    }

    /**
     * Return cursor is show or hide.
     *
     * @return return true: show, false: hide
     */
    public boolean isShowCursor() {
        return mIsShowCursor;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mStaffRectF == null) {
            mStaffRectF = new RectF(0, 0, w, h);
        } else {
            mStaffRectF.right = w;
            mStaffRectF.bottom = h;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawStaff(canvas);
        drawLabel(canvas);
        drawCursor(canvas);
    }

    private void drawStaff(Canvas canvas) {
        if (mStaffBitmap != null) {
            canvas.drawBitmap(mStaffBitmap, null, mStaffRectF, mPaint);
        }
    }

    private void drawLabel(Canvas canvas) {
        List<Model> data = mData;
        if (data == null) {
            return;
        }

        int mw = getMeasuredWidth();
        for (Model model : data) {
            double time = model.getTime();
            String label = model.getLabel();
            mPaint.setTextSize(mLabelTextSize);
            mPaint.getTextBounds(label, 0, label.length(), mLabelRect);
            int labelWidth = mLabelRect.width();
            int labelHeight = mLabelRect.height();
            double labelHalf = ((double) labelWidth / 2) / mw;
            float textX = (float) (time * mw) - (labelWidth / 2);
            if (mValue >= time - labelHalf) {
                mPaint.setColor(mSelectLabelColor);
            } else {
                mPaint.setColor(mNormalLabelColor);
            }
            // single label selected
            /*if (mValue >= time - labelHalf && mValue <= time + labelHalf) {
                mPaint.setColor(mSelectLabelColor);
            }*/
            canvas.drawText(label, textX, labelHeight + mLabelMarginTop, mPaint);
            String extra = model.getExtra();
            if(!TextUtils.isEmpty(extra)){
                mPaint.setTextSize(mExtraTextSize);
                canvas.drawText(extra, textX + labelWidth + mExtraMarginLeft
                        , mPaint.measureText(extra) + mExtraMarginTop, mPaint);
            }
        }
    }

    private void drawCursor(Canvas canvas) {
        if (mCursorDrawable != null && mIsShowCursor) {
            canvas.save();
            canvas.translate(mValue * getMeasuredWidth(), 0);
            mCursorDrawable.draw(canvas);
            canvas.restore();
        }
    }

    /**
     * Start the cursor movement animation.
     *
     * @param duration animation duration
     */
    public void start(long duration) {
        if (mPauseCursor) mPauseCursor = false;
        if (mCancelCursor) mCancelCursor = false;

        if(mValueAnimator != null && mValueAnimator.isStarted()){
            mValueAnimator.cancel();
        }
        mValueAnimator = ValueAnimator.ofFloat(0f, 1f);
        mValueAnimator.addListener(this);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(animation -> {
            mValue = (float) animation.getAnimatedValue() - getCursorWidth();
            invalidate();
        });
        mValueAnimator.setDuration(getDuration(duration));
        mValueAnimator.start();
    }

    private long getDuration(long duration){
        return (long) ((getCursorWidth() * duration * 2) + duration);
    }

    private float getCursorWidth(){
        if (mCursorDrawable != null){
            return (float) mCursorDrawable.getIntrinsicWidth() / getMeasuredWidth();
        }
        return 0;
    }

    /**
     * Pause the cursor movement animation.
     */
    public void pause() {
        if (!mPauseCursor) {
            mCurrentPlayTime = mValueAnimator.getCurrentPlayTime();
            mValueAnimator.cancel();
            mPauseCursor = true;
            if (mAnimatorListener != null) {
                mAnimatorListener.onPause();
            }
        }
    }

    /**
     * Resume the cursor movement animation.
     */
    public void resume() {
        if (mPauseCursor) {
            mValueAnimator.start();
            mValueAnimator.setCurrentPlayTime(mCurrentPlayTime);
            mPauseCursor = false;
            mCancelCursor = false;
            if (mAnimatorListener != null) {
                mAnimatorListener.onResume();
            }
        }
    }

    /**
     * Stop the cursor movement animation.
     */
    public void stop() {
        resetCursor();
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
        if (mPauseCursor) mPauseCursor = false;
        if (mAnimatorListener != null) {
            mAnimatorListener.onStop();
        }
    }

    private void resetCursor() {
        mValue = 0;
        invalidate();
    }

    /**
     * Set cursor move animator listener.
     *
     * @param listener listener callback interface
     */
    public void setOnAnimatorListener(OnAnimatorListener listener) {
        mAnimatorListener = listener;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (!mPauseCursor && mAnimatorListener != null) {
            mAnimatorListener.onStart();
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (!mCancelCursor && mAnimatorListener != null) {
            mAnimatorListener.onEnd();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        mCancelCursor = true;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    public static class Model {

        private double time;
        private String label;
        private String extra;

        public Model() {}

        public Model(double time, String label) {
            this.time = time;
            this.label = label;
        }

        public Model(double time, String label, String extra) {
            this.time = time;
            this.label = label;
            this.extra = extra;
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        @Override
        public String toString() {
            return "Model{" +
                    "time=" + time +
                    ", label='" + label + '\'' +
                    ", extra='" + extra + '\'' +
                    '}';
        }
    }
}
