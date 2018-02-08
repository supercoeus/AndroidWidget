package com.github.airsaid.androidwidget.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author airsaid
 *
 * 流布局. (目前只实现了基础功能, 以及子 View 的 margin, 待加入: gravity / 分割线 / 动画)
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
        // 获取布局 padding
        int pl = getPaddingLeft();
        int pt = getPaddingTop();
        int pr = getPaddingRight();
        int pb = getPaddingBottom();
        // 初始化布局的测量宽高
        int measuredWidth  = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = pt + pb;
        // 初始化子 View 位置信息
        int childCount = getChildCount();
        initLayout(childCount);
        // 当前行中已测量控件的总宽度
        int totalChildWidth = 0;
        // 当前行高度
        int lineHeight = 0;
        int childLeft  = pl;
        int childTop;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if(childView.getVisibility() == View.GONE){
                continue;
            }
            // 测量子 View 并获取子 View 尺寸信息
            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            int childMeasuredWidth  = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();
            int childWidth  = childMeasuredWidth + lp.leftMargin + lp.rightMargin;
            int childHeight = childMeasuredHeight + lp.topMargin + lp.bottomMargin;
            // 获取剩余宽度（剩余宽度 = 布局可用宽度 - 当前行中已测量控件的总宽度）
            int remainWidth = measuredWidth - pl - pr - totalChildWidth;
            // 判断当前控件是否能放置下
            if(childWidth > remainWidth){
                // 放不下, 换行处理
                childLeft = pl;
                measuredHeight += lineHeight;
                totalChildWidth = lineHeight = 0;
            }
            // 获取行高 (行高 = 当前行最大的 View 高度)
            lineHeight = Math.max(lineHeight, childHeight);
            // 能放下, 保存布局信息
            childLeft += lp.leftMargin;
            childTop = lp.topMargin + (measuredHeight - pt);
            saveLayout(i, childLeft, childTop, childMeasuredWidth, childMeasuredHeight);
            childLeft += childMeasuredWidth + lp.rightMargin;
            // 没有换行则宽度累加
            totalChildWidth += childWidth;
        }
        // 加上最后一行高度
        measuredHeight += lineHeight;
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

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }
}
