package com.github.airsaid.androidwidget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.github.airsaid.androidwidget.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author airsaid
 *
 * 简单的线性布局, 实现了 orientation, grivity, layout_grivity, 适应 ScrollView / RecyclerView 等.
 */
public class SimpleLinearLayout extends ViewGroup {

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {}

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL   = 1;
    private int mOrientation;

    /** 子 View 总宽 / 高 */
    private int mTotalChildWidth, mTotalChildHeight;
    private int mGravity = Gravity.LEFT | Gravity.TOP;

    public SimpleLinearLayout(Context context) {
        this(context, null);
    }

    public SimpleLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleLinearLayout);
        mOrientation = a.getInt(R.styleable.SimpleLinearLayout_android_orientation, HORIZONTAL);
        int gravity = a.getInt(R.styleable.SimpleLinearLayout_gravity, mGravity);
        setGravity(gravity);
        a.recycle();
    }

    /**
     * 设置子 View 的摆放方向. 可设置水平摆放 {@link #HORIZONTAL} 和垂直摆放 {@link #VERTICAL},
     * 默认是 {@link #HORIZONTAL}.
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setOrientation(@OrientationMode int orientation){
        this.mOrientation = orientation;
        requestLayout();
    }

    /**
     * 获取子 View 的摆放方向.
     * @return orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    @OrientationMode
    public int getOrientation(){
        return mOrientation;
    }

    /**
     * 设置子 View 的摆放位置.
     * @param gravity View 的摆放位置. 参数为 {@link Gravity} 类的静态成员.
     */
    public void setGravity(int gravity){
        if(mGravity != gravity){
            mGravity = gravity;
            requestLayout();
        }
    }

    /**
     * 获取子 View 的摆放位置.
     * @return View 的摆放位置.
     */
    public int getGravity(){
        return mGravity;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mOrientation == VERTICAL){
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        }else{
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 测量垂直摆放时, 当前布局的尺寸.
     * @param widthMeasureSpec  父控件给的建议宽度 {@link MeasureSpec}.
     * @param heightMeasureSpec 父控件给的建议高度 {@link MeasureSpec}.
     */
    private void measureVertical(int widthMeasureSpec, int heightMeasureSpec){
        mTotalChildHeight = 0;
        // 获取当前布局的 size 和 mode
        int widthSize  = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode  = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        // 当前布局的测量尺寸
        int measuredWidth  = getPaddingLeft() + getPaddingRight();
        int measuredHeight = getPaddingTop() + getPaddingBottom();
        // 当前布局的可用尺寸
        int usableWidth  = widthSize - getPaddingLeft() - getPaddingRight();
        int usableHeight = heightSize - getPaddingTop() - getPaddingBottom();
        // 循环测量子 View (要想确定当前这个父布局的大小, 那么必须得先知道子控件的大小）
        for (int i = 0; i < getChildCount(); i++) {
            // 获取子 View
            View childView = getChildAt(i);
            // 不计算被 Gone 掉的子 View
            if(childView.getVisibility() == GONE){
                continue;
            }

            // 子 View 的 size 和 mode
            int childViewWidthSize, childViewHeightSize;
            int childViewWidthMode, childViewHeightMode;
            // 获取子 View 的 layout_ 开头参数
            LayoutParams lp = getChildLayoutParams(childView);
            // 可用宽高要减去自己的 margin
            usableWidth -= lp.leftMargin + lp.rightMargin;
            usableHeight -= lp.topMargin + lp.bottomMargin;
            // 判断子 View 所设置的宽高
            switch (lp.width){
                case LayoutParams.MATCH_PARENT:
                    // 填满空间, 那么此时要根据当前布局的 mode 分为两种情况
                    if(widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST){
                        // 如果是 EXACTLY 或者是 AT_MOST (意味着有上限)
                        // 那么子 View 的 size 就是当前布局的可用大小, mode 则是 EXACTLY
                        childViewWidthSize = usableWidth;
                        childViewWidthMode = MeasureSpec.EXACTLY;
                    }else{
                        // UNSPECIFIED 的话, 就直接传递给子 View (我都不知道无上限是多大, 抛给你你自己玩去吧)
                        childViewWidthSize = 0;
                        childViewWidthMode = MeasureSpec.UNSPECIFIED;
                    }
                    break;
                case LayoutParams.WRAP_CONTENT:
                    // 子 View 自适应
                    if(widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST){
                        // 如果是 EXACTLY 或者是 AT_MOST (意味着有上限)
                        // 那么子 View 的 size 同样是当前布局的可用大小
                        // mode 则是 AT_MOST (告诉子 View, 我就这么大, 你可不要超过了这个大小)
                        childViewWidthSize = usableWidth;
                        childViewWidthMode = MeasureSpec.AT_MOST;
                    }else{
                        childViewWidthSize = 0;
                        childViewWidthMode = MeasureSpec.UNSPECIFIED;
                    }
                    break;
                default:
                    // 具体的数值, 那么直接就限制这个值
                    childViewWidthSize  = lp.width;
                    childViewWidthMode  = MeasureSpec.EXACTLY;
                    break;
            }
            switch (lp.height){
                case LayoutParams.MATCH_PARENT:
                    if(heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST){
                        childViewHeightSize = usableHeight;
                        childViewHeightMode = MeasureSpec.EXACTLY;
                    }else{
                        childViewHeightSize = 0;
                        childViewHeightMode = MeasureSpec.UNSPECIFIED;
                    }
                    break;
                case LayoutParams.WRAP_CONTENT:
                    if(heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST){
                        childViewHeightSize = usableHeight;
                        childViewHeightMode = MeasureSpec.AT_MOST;
                    }else{
                        childViewHeightSize = 0;
                        childViewHeightMode = MeasureSpec.UNSPECIFIED;
                    }
                    break;
                default:
                    childViewHeightSize  = lp.height;
                    childViewHeightMode  = MeasureSpec.EXACTLY;
                    break;
            }

            // 压缩对应的 MeasureSpec 对子 View 进行测量
            int childViewWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    childViewWidthSize, childViewWidthMode);
            int childViewHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    childViewHeightSize, childViewHeightMode);
            childView.measure(childViewWidthMeasureSpec, childViewHeightMeasureSpec);

            // 获取子 View 的测量宽高
            int childViewMeasuredWidth = childView.getMeasuredWidth();
            int childViewMeasuredHeight = childView.getMeasuredHeight();

            // 宽度 = 宽度最宽的子 View + 子 View margin + 当前布局的 paddingLeft + paddingRight
            measuredWidth = Math.max(measuredWidth, childViewMeasuredWidth
                    + lp.leftMargin + lp.rightMargin + getPaddingLeft() + getPaddingRight());
            // 高度 = 每个子 View 的高度 + 子 View margin + 当前布局的 paddingTop + paddingBottom
            measuredHeight += childViewMeasuredHeight + lp.topMargin + lp.bottomMargin;

            // 可用高度减少
            usableHeight -= childViewMeasuredHeight;

            mTotalChildHeight += childViewMeasuredHeight + lp.topMargin + lp.bottomMargin;
        }
        // 设置当前布局的尺寸
        measuredWidth  = resolveSize(measuredWidth, widthMeasureSpec);
        measuredHeight = resolveSize(measuredHeight, heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    /**
     * 测量水平摆放时, 当前布局的尺寸.
     * @param widthMeasureSpec  父控件给的建议宽度 {@link MeasureSpec}.
     * @param heightMeasureSpec 父控件给的建议高度 {@link MeasureSpec}.
     */
    private void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec){
        mTotalChildWidth = 0;
        // 获取当前布局的 size 和 mode
        int widthSize  = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode  = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        // 当前布局的测量尺寸
        int measuredWidth  = getPaddingLeft() + getPaddingRight();
        int measuredHeight = getPaddingTop() + getPaddingBottom();
        // 当前布局的可用尺寸
        int usableWidth  = widthSize - getPaddingLeft() - getPaddingRight();
        int usableHeight = heightSize - getPaddingTop() - getPaddingBottom();
        // 循环测量子 View (要想确定当前这个父布局的大小, 那么必须得先知道子控件的大小）
        for (int i = 0; i < getChildCount(); i++) {
            // 获取子 View
            View childView = getChildAt(i);
            // 不计算被 Gone 掉的子 View
            if(childView.getVisibility() == GONE){
                continue;
            }

            // 子 View 的 size 和 mode
            int childViewWidthSize, childViewHeightSize;
            int childViewWidthMode, childViewHeightMode;
            // 获取子 View 的 layout_ 开头参数
            LayoutParams lp = getChildLayoutParams(childView);
            // 可用宽高要减去自己的 margin
            usableWidth -= lp.leftMargin + lp.rightMargin;
            usableHeight -= lp.topMargin + lp.bottomMargin;
            // 判断子 View 所设置的宽高
            switch (lp.width){
                case LayoutParams.MATCH_PARENT:
                    // 填满空间, 那么此时要根据当前布局的 mode 分为两种情况
                    if(widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST){
                        // 如果是 EXACTLY 或者是 AT_MOST (意味着有上限)
                        // 那么子 View 的 size 就是当前布局的可用大小, mode 则是 EXACTLY
                        childViewWidthSize = usableWidth;
                        childViewWidthMode = MeasureSpec.EXACTLY;
                    }else{
                        // UNSPECIFIED 的话, 就直接传递给子 View (我都不知道无上限是多大, 抛给你你自己玩去吧)
                        childViewWidthSize = 0;
                        childViewWidthMode = MeasureSpec.UNSPECIFIED;
                    }
                    break;
                case LayoutParams.WRAP_CONTENT:
                    // 子 View 自适应
                    if(widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST){
                        // 如果是 EXACTLY 或者是 AT_MOST (意味着有上限)
                        // 那么子 View 的 size 同样是当前布局的可用大小
                        // mode 则是 AT_MOST (告诉子 View, 我就这么大, 你可不要超过了这个大小)
                        childViewWidthSize = usableWidth;
                        childViewWidthMode = MeasureSpec.AT_MOST;
                    }else{
                        childViewWidthSize = 0;
                        childViewWidthMode = MeasureSpec.UNSPECIFIED;
                    }
                    break;
                default:
                    // 具体的数值, 那么直接就限制这个值
                    childViewWidthSize  = lp.width;
                    childViewWidthMode  = MeasureSpec.EXACTLY;
                    break;
            }
            switch (lp.height){
                case LayoutParams.MATCH_PARENT:
                    if(heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST){
                        childViewHeightSize = usableHeight;
                        childViewHeightMode = MeasureSpec.EXACTLY;
                    }else{
                        childViewHeightSize = 0;
                        childViewHeightMode = MeasureSpec.UNSPECIFIED;
                    }
                    break;
                case LayoutParams.WRAP_CONTENT:
                    if(heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST){
                        childViewHeightSize = usableHeight;
                        childViewHeightMode = MeasureSpec.AT_MOST;
                    }else{
                        childViewHeightSize = 0;
                        childViewHeightMode = MeasureSpec.UNSPECIFIED;
                    }
                    break;
                default:
                    childViewHeightSize  = lp.height;
                    childViewHeightMode  = MeasureSpec.EXACTLY;
                    break;
            }

            // 压缩对应的 MeasureSpec 对子 View 进行测量
            int childViewWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    childViewWidthSize, childViewWidthMode);
            int childViewHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    childViewHeightSize, childViewHeightMode);
            childView.measure(childViewWidthMeasureSpec, childViewHeightMeasureSpec);

            // 获取子 View 的测量宽高
            int childViewMeasuredWidth = childView.getMeasuredWidth();
            int childViewMeasuredHeight = childView.getMeasuredHeight();

            // 宽度 = 每个显示的子 View 的宽度 + 子 View 的 margin + 当前布局的 paddingLeft and paddingRight
            measuredWidth += childViewMeasuredWidth + lp.leftMargin + lp.rightMargin;
            // 高度 = 最高的子 View 高度 + 子 View 的 margin + 当前布局的 paddingTop and paddingBottom
            measuredHeight = Math.max(measuredHeight, childViewMeasuredHeight
                    + lp.topMargin + lp.bottomMargin + getPaddingTop() + getPaddingBottom());

            // 可用宽度减少
            usableWidth -= childViewMeasuredWidth;

            mTotalChildWidth += childViewMeasuredWidth + lp.leftMargin + lp.rightMargin;
        }
        // 设置当前布局的尺寸
        measuredWidth  = resolveSize(measuredWidth, widthMeasureSpec);
        measuredHeight = resolveSize(measuredHeight, heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(mOrientation == VERTICAL){
            layoutVertical(l, t, r, b);
        }else{
            layoutHorizontal(l, t, r, b);
        }
    }

    private void layoutVertical(int l, int t, int r, int b){
        // 获取布局的宽 / 高度
        int width  = r - l;
        int height = b - t;
        int childTop  = getPaddingTop();
        if((mGravity & Gravity.BOTTOM) != 0){
            // 居下: 布局高度 - 子 View 总高度
            childTop = height - mTotalChildHeight;
        }else if((mGravity & Gravity.CENTER_VERTICAL) != 0 || (mGravity & Gravity.CENTER) != 0){
            // 垂直居中: 布局高度的一半 - 子 View 总高度的一半 + 处理 padding margin
            childTop = height / 2 - mTotalChildHeight / 2 + getPaddingTop() - getPaddingBottom();
        }
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if(childView.getVisibility() == GONE){
                continue;
            }
            int measuredWidth = childView.getMeasuredWidth();
            int measuredHeight = childView.getMeasuredHeight();
            LayoutParams lp = getChildLayoutParams(childView);
            int childLeft = getPaddingLeft() + lp.leftMargin;
            // 处理子 View 的 layout_gravity, 只处理 left / right / center_horizontal / center
            // 处理代码同布局的 gravity, 因为要优先处理子 View 的 layout_gravity, 所以代码放前面
            int gravity = lp.gravity;
            if((gravity & Gravity.LEFT) != 0){
                childLeft = getPaddingLeft() + lp.leftMargin;
            }else if((gravity & Gravity.RIGHT) != 0){
                childLeft += width - measuredWidth - lp.rightMargin;
            }else if((gravity & Gravity.CENTER_HORIZONTAL) != 0 || (gravity & Gravity.CENTER) != 0){
                // 水平居中优先级低于 left 和 right
                childLeft += width / 2 - measuredWidth / 2 - getPaddingRight() - lp.rightMargin;
            }else if((mGravity & Gravity.RIGHT) != 0){
                // 居右: 布局宽度 - 子 View 的宽度 + 处理 margin
                childLeft += width - measuredWidth - lp.rightMargin;
            }else if((mGravity & Gravity.CENTER_HORIZONTAL) != 0 || (mGravity & Gravity.CENTER) != 0){
                // 水平居中: 布局宽度一半 - 子 View 宽度一半 + 处理 padding margin
                childLeft += width / 2 - measuredWidth / 2 - getPaddingRight() - lp.rightMargin;
            }
            childTop += lp.topMargin;
            childView.layout(childLeft, childTop, childLeft + measuredWidth, childTop + measuredHeight);
            childTop += measuredHeight + lp.bottomMargin;
        }
    }

    private void layoutHorizontal(int l, int t, int r, int b){
        // 获取布局的宽 / 高度
        int width  = r - l;
        int height = b - t;
        int childLeft = getPaddingLeft();
        if((mGravity & Gravity.RIGHT) != 0){
            // 居右: 布局宽度 - 子 View 的总宽度
            childLeft = width - mTotalChildWidth;
        }else if((mGravity & Gravity.CENTER_HORIZONTAL) != 0 || (mGravity & Gravity.CENTER) != 0){
            // 水平居中: 布局的宽度一半 - 子 View 总宽度的一半
            childLeft += width / 2 - mTotalChildWidth / 2;
        }
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if(childView.getVisibility() == GONE){
                continue;
            }
            int measuredWidth  = childView.getMeasuredWidth();
            int measuredHeight = childView.getMeasuredHeight();
            LayoutParams lp    = getChildLayoutParams(childView);
            childLeft += lp.leftMargin;
            int childTop = getPaddingTop() + lp.topMargin;
            // 处理子 View 的 layout_gravity, 只处理 top / bottom / center_vertical / center
            // 处理代码同布局的 gravity, 因为要优先处理子 View 的 layout_gravity, 所以代码放前面
            int gravity = lp.gravity;
            if((gravity & Gravity.TOP) != 0){
                childTop = getPaddingTop() + lp.topMargin;
            }else if((gravity & Gravity.BOTTOM) != 0){
                childTop = height - measuredHeight - lp.bottomMargin - getPaddingBottom();
            }else if((gravity & Gravity.CENTER_VERTICAL) != 0 || (gravity & Gravity.CENTER) != 0){
                // 垂直居中优先级低于 bottom 和 top
                childTop = height / 2 - measuredHeight / 2 + lp.topMargin - lp.bottomMargin
                        + getPaddingTop() - getPaddingBottom();
            }else if((mGravity & Gravity.BOTTOM) != 0){
                // 居下: 布局高度 - 子 View 高度 + 处理 padding margin
                childTop = height - measuredHeight - lp.bottomMargin - getPaddingBottom();
            }else if((mGravity & Gravity.CENTER_VERTICAL) != 0 || (mGravity & Gravity.CENTER) != 0){
                // 垂直居中: 布局高度的一半 - 子 View 高度的一半 + 处理 padding margin
                childTop = height / 2 - measuredHeight / 2 + lp.topMargin - lp.bottomMargin
                        + getPaddingTop() - getPaddingBottom();
            }
            childView.layout(childLeft, childTop, childLeft + measuredWidth, childTop + measuredHeight);
            childLeft += measuredWidth + lp.rightMargin;
        }
    }

    private LayoutParams getChildLayoutParams(View child){
        return (LayoutParams) child.getLayoutParams();
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /**
     * 子 View 布局信息. (继承 MarginLayoutParams, 需要处理子 View 的 margin 属性,
     * 另外额外增加 gravity, 用于约束子 View 的方向)
     */
    public static class LayoutParams extends MarginLayoutParams{

        public int gravity;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.SimpleLinearLayout_Layout);
            gravity = a.getInt(R.styleable.SimpleLinearLayout_Layout_layout_gravity, 0);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
