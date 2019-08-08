package com.github.airsaid.androidwidget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.ArrayRes;
import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.github.airsaid.androidwidget.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author airsaid
 *
 * 增加版 TextView. (目前只根据项目需求来进行扩展的, 后续还会进行增加更多功能)
 */
public class SuperTextView extends AppCompatTextView {

    @IntDef({START, END})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TagGravity {}

    public static final int START = 1;
    public static final int END   = 2;

    /** 背景 (与原有背景不同的是, 它会忽略掉文字的行间距) */
    private Drawable mSuperBackground;
    /** 标签背景 */
    private Drawable mTagBackground;
    /** 标签文字大小 */
    private int mTagTextSize;
    /** 标签文字颜色 */
    private int mTagTextColor;
    /** 标签距左侧间距 */
    private int mTagMarginStart;
    /** 标签距右侧间距 */
    private int mTagMarginEnd;
    /** 标签位置 */
    private int mTagPosition;
    /** 标签的显示位置 */
    private int mTagGravity;
    /** 标签数据 */
    private String[] mTags;

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

        mTagBackground = a.getDrawable(R.styleable.SuperTextView_stv_tagBackground);

        setTagTextSize(a.getDimensionPixelSize(R.styleable.SuperTextView_stv_tagTextSize, 14));

        setTagTextColor(a.getColor(R.styleable.SuperTextView_stv_tagTextColor, Color.BLACK));

        setTagMarginStart((int) a.getDimension(R.styleable.SuperTextView_stv_tagMarginStart, 0));

        setTagMarginEnd((int) a.getDimension(R.styleable.SuperTextView_stv_tagMarginEnd, 0));

        setTagPosition(a.getInt(R.styleable.SuperTextView_stv_tagPosition, -1));

        mTagGravity = a.getInt(R.styleable.SuperTextView_stv_tagGravity, START);

        setTags(a.getResourceId(R.styleable.SuperTextView_stv_tags, -1));

        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawSuperBackground(canvas);
        super.onDraw(canvas);
    }

    /**
     * 绘制 super text view 背景, 该背景去掉了 {@link #getLineSpacingExtra()} 所指定的行间距.
     * @param canvas 画布
     */
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
     * @param drawable 背景 drawable
     */
    public void setSuperBackground(@Nullable Drawable drawable){
        if(drawable != mSuperBackground){
            mSuperBackground = drawable;
            invalidate();
        }
    }

    /**
     * 获取 super 背景.
     * @return 背景 drawable
     */
    @Nullable
    public Drawable getSuperBackground(){
        return mSuperBackground;
    }

    /**
     * 根据指定的字符串数组资源 id 来设置标签.
     * @param resId 字符串资源 id
     */
    public void setTags(@ArrayRes int resId){
        if(resId != -1){
            mTags = getResources().getStringArray(resId);
            canvasTags();
        }
    }

    /**
     * 设置标签数组.
     * @param tags 标签数组
     */
    public void setTags(String[] tags){
        if(tags != null){
            mTags = tags;
            canvasTags();
        }
    }

    /**
     * 获取标签数组.
     * @return 标签数组, 没有时返回 NULL
     */
    @Nullable
    public String[] getTags(){
        return mTags;
    }

    /**
     * 设置标签背景.
     * @param drawable 背景 drawable
     */
    public void setTagBackground(@Nullable Drawable drawable){
        if(drawable != mTagBackground){
            mTagBackground = drawable;
            invalidate();
        }
    }

    /**
     * 获取标签背景.
     * @return 背景 drawable
     */
    @Nullable
    public Drawable getTagBackground(){
        return mTagBackground;
    }

    /**
     * 设置标签文字大小.
     * @param textSize 标签文字大小
     */
    public void setTagTextSize(@Px int textSize) {
        if(mTagTextSize != textSize){
            mTagTextSize = textSize;
        }
    }

    /**
     * 获取标签文字大小.
     * @return 标签文字大小
     */
    public int getTagTextSize(){
        return mTagTextSize;
    }

    /**
     * 设置标签文字颜色.
     * @param textColor 文字颜色.
     */
    public void setTagTextColor(@ColorInt int textColor) {
        if(mTagTextColor != textColor){
            mTagTextColor = textColor;
        }
    }

    /**
     * 获取标签文字颜色.
     * @return 文字颜色
     */
    @ColorInt
    public int getTagTextColor(){
        return mTagTextColor;
    }

    /**
     * 设置标签距离左侧间距.
     * @param margin 间距
     */
    public void setTagMarginStart(int margin){
        this.mTagMarginStart = margin;
    }

    /**
     * 返回标签距离左侧的间距.
     * @return 间距
     */
    public int getTagMarginStart(){
        return mTagMarginStart;
    }

    /**
     * 设置标签距离右侧间距.
     * @param margin 间距
     */
    public void setTagMarginEnd(int margin){
        this.mTagMarginEnd = margin;
    }

    /**
     * 返回标签距离右侧的间距.
     * @return 间距
     */
    public int getTagMarginEnd(){
        return mTagMarginEnd;
    }

    /**
     * 设置标签的显示位置.
     * @param position 标签显示位置, 不可超过文本的长度.
     */
    public void setTagPosition(int position) {
        mTagPosition = position;
    }

    /**
     * 获取标签的显示位置.
     * @return 位置.
     */
    public int getTagPosition(){
        return mTagPosition;
    }

    /**
     * 设置标签的显示位置.
     * @param gravity 设置为文本的开头: {@link #START}, 文本的结尾处: {@link #END},
     * 如果要设置在文本的中间, 则可以通过 {@link #setTagPosition(int)} 来设置.
     */
    public void setTagGravity(@TagGravity int gravity) {
        if(mTagGravity != gravity){
            mTagGravity = gravity;
        }
    }

    /**
     * 返回当前标签的显示位置.
     * @return {@link #START} or {@link #END}
     */
    @TagGravity
    public int getTagGravity(){
        return mTagGravity;
    }

    private void canvasTags(){
        if(mTags == null){
            return;
        }

        int start = 0, end;
        String content = getText().toString();
        String tagStr = getTagStr();
        if(content.contains(tagStr)){
            content = content.replace(tagStr, "");
        }
        StringBuilder sb = new StringBuilder();
        if(mTagPosition > 0){
            sb.append(content.substring(0, mTagPosition));
            sb.append(tagStr);
            sb.append(content.substring(mTagPosition, content.length()));
            start = mTagPosition;
        }else if(mTagGravity == START){
            sb.append(tagStr);
            sb.append(content);
        }else if(mTagGravity == END){
            start = content.length();
            sb.append(content);
            sb.append(tagStr);
        }
        SpannableString ss = new SpannableString(sb.toString());
        for (int i = 0; i < mTags.length; i++) {
            String tag = mTags[i];
            end = start + tag.length();

            LinearLayout container = new LinearLayout(getContext());
            container.setPadding(mTagMarginStart, 0, mTagMarginEnd, 0);
            AppCompatTextView tagView = new AppCompatTextView(getContext());
            tagView.setText(tag);
            tagView.setTextColor(mTagTextColor);
            tagView.getPaint().setTextSize(mTagTextSize);
            tagView.setBackgroundDrawable(mTagBackground);
            container.addView(tagView);

            Bitmap tagBitmap = view2Bitmap(container);
            Drawable drawable = new BitmapDrawable(tagBitmap);
            drawable.setBounds(0, 0, container.getWidth(), container.getHeight());
            CenterImageSpan span = new CenterImageSpan(drawable);
            ss.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            start = end;
        }
        setText(ss);
    }

    private String getTagStr(){
        StringBuilder tagSb = new StringBuilder(mTags.length);
        for (String tag : mTags) {
            tagSb.append(tag);
        }
        return tagSb.toString();
    }

    private static Bitmap view2Bitmap(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }
}
