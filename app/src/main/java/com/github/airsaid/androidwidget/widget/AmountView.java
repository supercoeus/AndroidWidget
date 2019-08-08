package com.github.airsaid.androidwidget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.github.airsaid.androidwidget.R;

/**
 * @author airsaid
 *
 * 数量选择自定义组合控件.
 */
public class AmountView extends FrameLayout implements View.OnClickListener {

    /** 文字大小 */
    private float mTextSize;
    /** 字体颜色 */
    private int mTextColor;
    /** 左侧按钮背景 */
    private int mLeftBackground;
    /** 中间输入框背景 */
    private int mMiddleBackground;
    /** 中间输入框宽度 */
    private float mMiddleWidth;
    /** 右侧按钮背景 */
    private int mRightBackground;
    /** 文字输入框是否可编辑 */
    private boolean mEditor;
    /** 最小数量 */
    private int mMin;
    /** 最大数量 */
    private int mMax;
    /** 步长 */
    private int mStepSize;
    /** 数量 */
    private int mAmount;

    private Button      mBtnLeftSub;
    private EditText    mEdtAmount;
    private Button      mBtnRightAdd;
    private OnAmountChangedListener mListener;

    public interface OnAmountChangedListener{
        /**
         * 数量改变监听.
         * @param isAdd        是否是增加.
         * @param changeAmount 改变的数量.
         */
        void onAmountChanged(boolean isAdd, int changeAmount);
    }

    public AmountView(@NonNull Context context) {
        this(context, null);
    }

    public AmountView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AmountView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
        initAttrs(attrs);
    }

    private void initLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_amount, this, true);
        mBtnLeftSub  = findViewById(R.id.btn_left_sub);
        mEdtAmount   = findViewById(R.id.edt_amount);
        mBtnRightAdd = findViewById(R.id.btn_right_add);
        mBtnLeftSub.setOnClickListener(this);
        mBtnRightAdd.setOnClickListener(this);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AmountView);

        float btnWidth = a.getDimension(R.styleable.AmountView_av_btmWidth, dp2px(26f));
        float btnHeight = a.getDimension(R.styleable.AmountView_av_btmHeight, dp2px(27f));
        setButtonSize(btnWidth, btnHeight);

        float middleWidth = a.getDimension(R.styleable.AmountView_av_middleWidth, -1);
        if (middleWidth != -1) {
            setMiddleWidth(middleWidth);
        }

        int leftBackground = a.getResourceId(R.styleable.AmountView_av_leftBackground, -1);
        if (leftBackground != -1) {
            setLeftBackground(leftBackground);
        }

        int middleBackground = a.getResourceId(R.styleable.AmountView_av_middleBackground, -1);
        if (middleBackground != -1) {
            setMiddleBackground(middleBackground);
        }

        int rightBackground = a.getResourceId(R.styleable.AmountView_av_rightBackground, -1);
        if (rightBackground != -1) {
            setRightBackground(rightBackground);
        }

        float textSize = a.getDimension(R.styleable.AmountView_av_textSize, sp2px(14));
        setTextSize(textSize);

        int textColor = a.getColor(R.styleable.AmountView_av_textColor, Color.BLACK);
        setTextColor(textColor);

        boolean isEditor = a.getBoolean(R.styleable.AmountView_av_isEditor, false);
        setEditor(isEditor);

        int amount = a.getInteger(R.styleable.AmountView_av_amount, 1);
        setAmount(amount);

        int min = a.getInteger(R.styleable.AmountView_av_min, 0);
        setMin(min);

        int max = a.getInteger(R.styleable.AmountView_av_max, Integer.MAX_VALUE);
        setMax(max);

        int stepSize = a.getInteger(R.styleable.AmountView_av_stepSize, 1);
        setStepSize(stepSize);

        a.recycle();
    }

    /**
     * 设置按钮大小.
     * @param btnWidth  按钮宽度。
     * @param btnHeight 按钮高度.
     */
    public void setButtonSize(float btnWidth, float btnHeight) {
        ViewGroup.LayoutParams lpl = mBtnLeftSub.getLayoutParams();
        ViewGroup.LayoutParams lpr = mBtnRightAdd.getLayoutParams();
        lpl.width  = lpr.width = (int) btnWidth;
        lpl.height = lpr.height = (int) btnHeight;
        mBtnLeftSub.setLayoutParams(lpl);
        mBtnRightAdd.setLayoutParams(lpr);
    }

    /**
     * 设置中心输入框宽度.
     * @param middleWidth 宽度.
     */
    public void setMiddleWidth(float middleWidth) {
        if(mMiddleWidth != middleWidth){
            mMiddleWidth = middleWidth;
            ViewGroup.LayoutParams lp = mEdtAmount.getLayoutParams();
            lp.width = (int) middleWidth;
            mEdtAmount.setLayoutParams(lp);
        }
    }

    /**
     * 设置左侧按钮背景.
     * @param drawable drawable res id.
     */
    public void setLeftBackground(@DrawableRes int drawable){
        if(mLeftBackground != drawable){
            mLeftBackground = drawable;
            mBtnLeftSub.setBackgroundResource(mLeftBackground);
        }
    }

    /**
     * 设置中间输入框背景.
     * @param drawable drawable res id.
     */
    public void setMiddleBackground(@DrawableRes int drawable){
        if(mMiddleBackground != drawable){
            mMiddleBackground = drawable;
            mEdtAmount.setBackgroundResource(mMiddleBackground);
        }
    }

    /**
     * 设置右侧按钮背景.
     * @param drawable drawable res id.
     */
    public void setRightBackground(@DrawableRes int drawable){
        if(mRightBackground != drawable){
            mRightBackground = drawable;
            mBtnRightAdd.setBackgroundResource(mRightBackground);
        }
    }

    /**
     * 设置文字大小.
     * @param textSize 文字大小 (px).
     */
    public void setTextSize(float textSize){
        if(mTextSize != textSize){
            mTextSize = textSize;
            mEdtAmount.getPaint().setTextSize(mTextSize);
        }
    }

    /**
     * 设置文字颜色.
     * @param textColor 文字颜色.
     */
    public void setTextColor(@ColorInt int textColor){
        if(mTextColor != textColor){
            mTextColor = textColor;
            mEdtAmount.setTextColor(mTextColor);
        }
    }


    /**
     * 设置是否可编辑.
     * @param editor true 可编辑, false 不可编辑.
     */
    public void setEditor(boolean editor) {
        mEditor = editor;
        mEdtAmount.setFocusable(mEditor);
        mEdtAmount.setLongClickable(mEditor);
    }

    /**
     * 获取当前是否可编辑.
     * @return true 可编辑, false 不可编辑.
     */
    public boolean isEditor(){
        return mEditor;
    }

    /**
     * 设置数量.
     * @param amount 数量.
     */
    public void setAmount(int amount){
        mAmount = amount;
        mEdtAmount.setText(String.valueOf(mAmount));
    }

    /**
     * 获取数量.
     * @return 数量.
     */
    public int getAmount(){
        return mAmount;
    }

    /**
     * 设置最小数量.
     * @param min 最小数量.
     */
    public void setMin(int min) {
        mMin = min;
        if(mAmount < mMin){
            setAmount(mMin);
        }
    }

    /**
     * 获取最小数量.
     * @return 最小数量.
     */
    public int getMin(){
        return mMin;
    }

    /**
     * 设置最大数量.
     * @param max 最大数量.
     */
    public void setMax(int max) {
        mMax = max;
        if(mAmount > mMax){
            setAmount(mMax);
        }
    }

    /**
     * 获取最大数量.
     * @return 最大数量.
     */
    public int getMax(){
        return mMax;
    }

    /**
     * 设置步长 (即每次加或减的数是多少).
     * @param stepSize 步长.
     */
    public void setStepSize(@IntRange(from = 1, to = Integer.MAX_VALUE) int stepSize) {
        mStepSize = stepSize;
    }

    /**
     * 获取步长.
     * @return 步长.
     */
    public int getStepSize(){
        return mStepSize;
    }

    /**
     * 设置数量改变监听回调.
     * @param listener 监听接口.
     */
    public void setOnAmountChangedListener(OnAmountChangedListener listener){
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        boolean isAdd = false;
        int change    = 0;
        int amount    = mAmount;
        int id        = v.getId();
        if(id == R.id.btn_left_sub){
            isAdd = false;
            if(mMin <= amount - mStepSize){
                amount -= mStepSize;
                change = mAmount - amount;
                setAmount(amount);
            }
        }else if(id == R.id.btn_right_add){
            isAdd = true;
            if(mMax >= amount + mStepSize){
                amount += mStepSize;
                change = amount - mAmount;
                setAmount(amount);
            }
        }
        if(mListener != null){
            mListener.onAmountChanged(isAdd, change);
        }
    }

    private int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getContext().getResources().getDisplayMetrics());
    }

    private int dp2px(float dpValue){
        return (int)(dpValue * (getContext().getResources().getDisplayMetrics().density) + 0.5f);
    }
}