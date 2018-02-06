package com.github.airsaid.androidwidget.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author airsaid
 *
 * 流布局. (目前只实现了基础功能, 待加入: gravity / 分割线)
 */
public class FlowLayout extends ViewGroup {

    private int[] mChildLeft, mChildTop, mChildRight, mChildBottom;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取布局宽度
        int widthSize  = MeasureSpec.getSize(widthMeasureSpec);
        // 获取布局 padding
        int pl = getPaddingLeft();
        int pt = getPaddingTop();
        int pr = getPaddingRight();
        int pb = getPaddingBottom();
        // 初始化布局的测量宽高
        int measuredWidth  = widthSize;
        int measuredHeight = pt + pb;
        // 初始化当前行中已测量控件的总宽度
        int totalChildWidth = 0;
        // 初始化子 View 位置信息
        int childCount = getChildCount();
        initLayout(childCount);
        int childLeft  = pl;
        int childTop   = pt;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if(childView.getVisibility() == View.GONE){
                continue;
            }
            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            int childMeasuredWidth  = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();
            // 获取剩余宽度（剩余宽度 = 布局宽度 - 当前行中已测量控件的总宽度）
            int remainWidth = widthSize - totalChildWidth;
            // 判断当前控件是否能放置下
            if(childMeasuredWidth <= remainWidth){
                // 能放下，保存布局信息
                saveLayout(i, childLeft, childTop, childMeasuredWidth, childMeasuredHeight);
                childLeft += childMeasuredWidth;
                // 没有换行则宽度累加
                totalChildWidth += childMeasuredWidth;
                // 设置默认的测量宽度
                if(measuredHeight == pt + pb){
                    measuredHeight += childMeasuredHeight;
                }
            }else{
                // 换行
                childLeft = pl;
                childTop += childMeasuredHeight;
                saveLayout(i, childLeft, childTop, childMeasuredWidth, childMeasuredHeight);
                childLeft += childMeasuredWidth;
                totalChildWidth = childMeasuredWidth;
                measuredHeight += childMeasuredHeight;
            }
        }
        measuredWidth  = resolveSize(measuredWidth, widthMeasureSpec);
        measuredHeight = resolveSize(measuredHeight, heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private void initLayout(int childCount){
        mChildLeft   = new int[childCount];
        mChildTop    = new int[childCount];
        mChildRight  = new int[childCount];
        mChildBottom = new int[childCount];
    }

    private void saveLayout(int i, int l, int t, int w, int h){
        mChildLeft[i]   = l;
        mChildTop[i]    = t;
        mChildRight[i]  = l + w;
        mChildBottom[i] = t + h;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if(childView.getVisibility() != View.GONE){
                childView.layout(mChildLeft[i], mChildTop[i], mChildRight[i], mChildBottom[i]);
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override @SuppressWarnings("all")
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
}
