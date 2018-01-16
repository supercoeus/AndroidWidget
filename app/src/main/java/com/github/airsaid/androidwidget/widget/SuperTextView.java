package com.github.airsaid.androidwidget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.util.AttributeSet;

import com.github.airsaid.androidwidget.R;

/**
 * @author airsaid
 *
 * 增加版 TextView. (目前只有根据项目需求进行扩展的, 后续还会进行增加更多功能)
 */
public class SuperTextView extends AppCompatTextView {

    /** 背景 (与原有背景不同的是, 它会忽略掉文字的行间距) */
    private Drawable mSuperBackground;

    Rect mBounds = new Rect();

    public SuperTextView(Context context) {
        this(context, null);
    }

    public SuperTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SuperTextView);
        mSuperBackground = a.getDrawable(R.styleable.SuperTextView_stv_background);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawSuperBackground(canvas);
        super.onDraw(canvas);
    }

    private void drawSuperBackground(Canvas canvas){
        if (mSuperBackground != null) {
            int pl = getPaddingLeft();
            int pr = getPaddingRight();
            int pt = getPaddingTop();
            int pb = getPaddingBottom();
            Layout layout = getLayout();
            int lineCount = layout.getLineCount();
            int lineSpacingExtra = (int) getLineSpacingExtra();
            for (int i = 0; i < lineCount; i++) {
                mBounds.left   = (int) layout.getLineLeft(i);
                mBounds.right  = (int) (layout.getLineRight(i) + pl + pr);
                mBounds.top    = layout.getLineTop(i);
                int lineBottom = layout.getLineBottom(i);
                mBounds.bottom = i != lineCount - 1 ? lineBottom - lineSpacingExtra + pt + pt
                        : lineBottom + pt + pb;
                canvas.save();
                mSuperBackground.setBounds(mBounds);
                mSuperBackground.draw(canvas);
                canvas.restore();
            }
        }
    }

    /**
     * 设置 super 背景.
     * @param drawable 背景 drawable.
     */
    public void setSuperBackground(Drawable drawable){
        if(drawable != mSuperBackground){
            mSuperBackground = drawable;
            invalidate();
        }
    }

    /**
     * 获取 super 背景.
     * @return 背景 drawable.
     */
    public Drawable getSuperBackground(){
        return mSuperBackground;
    }
}
