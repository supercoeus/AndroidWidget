package com.github.airsaid.androidwidget.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;


import com.github.airsaid.androidwidget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author airsaid
 *
 * 使用贝塞尔曲线实现的自定义弹跳 View。
 */
public class BounceView extends View {

    private static final String TAG = BounceView.class.getSimpleName();

    private Path mPath;
    private Paint mPaint;
    private Drawable mDrawable;
    private ValueAnimator mAnimator;
    private PathMeasure mPathMeasure;
    private float[] mCurPosition = new float[2];
    private List<Quadratic> mQuadratics = new ArrayList<>();

    private boolean mDebug;
    private int mBounceCount;
    private long mBounceTime;

    public BounceView(Context context) {
        this(context, null);
    }

    public BounceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BounceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BounceView);
        Drawable drawable = a.getDrawable(R.styleable.BounceView_bbv_drawable);
        setDrawable(drawable);
        a.recycle();

        mPath = new Path();
        mPathMeasure = new PathMeasure();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 设置弹跳 drawable（该 drawable 可以是一个 shape，也可以是一个图片）。
     *
     * @param drawable 弹跳 drawable
     */
    public void setDrawable(Drawable drawable) {
        this.mDrawable = drawable;
    }

    /**
     * 设置是否开启 debug 模式，开启后会额外绘制辅助线（默认是关闭）。
     *
     * @param debug true 开启，false 关闭
     */
    public void setDebug(boolean debug) {
        this.mDebug = debug;
        postInvalidate();
    }

    /**
     * 设置弹跳参数。
     *
     * @param count 弹跳次数
     * @param time  弹跳时间
     */
    public void setBounceParams(int count, long time) {
        this.mBounceCount = count;
        this.mBounceTime = time;
    }

    /**
     * 获取弹跳次数。
     *
     * @return 弹跳次数
     */
    public int getBounceCount() {
        return mBounceCount;
    }

    /**
     * 获取弹跳时间。
     *
     * @return 弹跳时间
     */
    public long getBounceTime() {
        return mBounceTime;
    }

    /**
     * 开始弹跳。
     */
    public void startBounce() {
        // 计算贝塞尔曲线的数据点和控制点坐标
        computePoints();
        // 生成 Path
        generatePath();
        // 开始动画
        startAnim();
    }

    private void computePoints() {
        // 弹跳次数
        int bounceCount = mBounceCount;
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        // 每次弹跳宽度
        int bounceWidth = w / bounceCount;
        // 计算数据点和控制点坐标
        int x = 0, y = h - mDrawable.getMinimumHeight();
        mQuadratics.clear();
        for (int index = 1; index <= bounceCount; index++) {
            Point startPoint = new Point(x, y);
            Point endPoint = new Point(bounceWidth * index, y);
            Point controlPoint = new Point(endPoint.x - bounceWidth / 2, -h + mDrawable.getMinimumHeight());
            mQuadratics.add(new Quadratic(startPoint, endPoint, controlPoint));
            x = endPoint.x;
        }
    }

    private void generatePath(){
        mPath.reset();
        mPaint.setStrokeWidth(4f);
        mPaint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < mQuadratics.size(); i++) {
            Quadratic q = mQuadratics.get(i);
            if (i == 0) {
                mPath.moveTo(q.startPoint.x, q.startPoint.y);
            }
            mPath.quadTo(q.controlPoint.x, q.controlPoint.y, q.endPoint.x, q.endPoint.y);
            Log.e(TAG, "Quadratic: " + q.toString());
        }
        mPathMeasure.setPath(mPath, false);
    }

    private void startAnim() {
        mAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        mAnimator.setDuration(mBounceTime);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float distance = (float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(distance, mCurPosition, null);
                postInvalidate();
            }
        });
        mAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // debug 模式下绘制辅助线
        if (mDebug) {
            drawDebug(canvas);
        }
        // 绘制跳动 drawable
        drawDrawable(canvas);
    }

    private void drawDebug(Canvas canvas) {
        mPaint.setStrokeWidth(4f);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, mPaint);
        for (Quadratic q : mQuadratics) {
            mPaint.setStrokeWidth(20f);
            mPaint.setColor(Color.BLUE);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawPoint(q.startPoint.x, q.startPoint.y, mPaint);
            canvas.drawPoint(q.endPoint.x, q.endPoint.y, mPaint);
            canvas.drawPoint(q.controlPoint.x, q.controlPoint.y, mPaint);
        }
    }

    private void drawDrawable(Canvas canvas) {
        if (mDrawable != null && mAnimator != null && mAnimator.isStarted()) {
            if (mDrawable.getBounds().right <= 0) {
                //  设置一次大小即可
                int width = mDrawable.getMinimumWidth();
                int height = mDrawable.getIntrinsicHeight();
                mDrawable.setBounds(new Rect(0, 0, width, height));
            }
            canvas.save();
            canvas.translate(mCurPosition[0], mCurPosition[1]);
            mDrawable.draw(canvas);
            canvas.restore();
        }
    }

    private class Quadratic {
        Point startPoint, endPoint, controlPoint;

        Quadratic(Point s, Point e, Point c) {
            this.startPoint = s;
            this.endPoint = e;
            this.controlPoint = c;
        }

        @Override
        public String toString() {
            return "Quadratic{" +
                    "startPoint=" + startPoint +
                    ", endPoint=" + endPoint +
                    ", controlPoint=" + controlPoint +
                    '}';
        }
    }
}
