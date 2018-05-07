package com.github.airsaid.androidwidget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.airsaid.androidwidget.adapter.ItemAdapter;
import com.github.airsaid.androidwidget.data.Item;
import com.github.airsaid.androidwidget.ui.AmountViewFragment;
import com.github.airsaid.androidwidget.ui.BounceViewFragment;
import com.github.airsaid.androidwidget.ui.CalendarViewFragment;
import com.github.airsaid.androidwidget.ui.FlowLayoutFragment;
import com.github.airsaid.androidwidget.ui.InputCodeLayoutFragment;
import com.github.airsaid.androidwidget.ui.ProgressBarViewFragment;
import com.github.airsaid.androidwidget.ui.SimpleLinearLayoutFragment;
import com.github.airsaid.androidwidget.ui.StaffViewFragment;
import com.github.airsaid.androidwidget.ui.SuperTextViewFragment;
import com.github.airsaid.androidwidget.widget.AmountView;
import com.github.airsaid.androidwidget.widget.BounceView;
import com.github.airsaid.androidwidget.widget.FlowLayout;
import com.github.airsaid.androidwidget.widget.InputCodeLayout;
import com.github.airsaid.androidwidget.widget.ProgressBarView;
import com.github.airsaid.androidwidget.widget.SimpleLinearLayout;
import com.github.airsaid.androidwidget.widget.StaffView;
import com.github.airsaid.androidwidget.widget.SuperTextView;
import com.github.airsaid.calendarview.widget.CalendarView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author airsaid
 */
public class MainFragment extends Fragment {

    private ItemAdapter mItemAdapter;
    private List<Item> mViewItems       = new ArrayList<>();
    private List<Item> mHasViewItems    = new ArrayList<>();
    private List<Item> mLayoutItems     = new ArrayList<>();
    private List<Item> mHasLayoutItems  = new ArrayList<>();

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mItemAdapter = new ItemAdapter();
        mItemAdapter.setOnItemClickCallback((v, position) ->
                mOnItemClickCallback.onItemClickListener(mItemAdapter.getData().get(position)));
        recyclerView.setAdapter(mItemAdapter);
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewItems.add(new Item(CalendarView.class.getSimpleName(),
                "自定义可多选日历控件",
                CalendarViewFragment.class));

        mViewItems.add(new Item(BounceView.class.getSimpleName(),
                "贝塞尔曲线实现的弹跳 View",
                BounceViewFragment.class));

        mViewItems.add(new Item(StaffView.class.getSimpleName(),
                "自定义乐谱 View",
                StaffViewFragment.class));

        mViewItems.add(new Item(ProgressBarView.class.getSimpleName(),
                "自定义进度条 View",
                ProgressBarViewFragment.class));

        mHasViewItems.add(new Item(SuperTextView.class.getSimpleName(),
                "增强版 TextView",
                SuperTextViewFragment.class));

        mLayoutItems.add(new Item(SimpleLinearLayout.class.getSimpleName(),
                "简易版 LinearLayout",
                SimpleLinearLayoutFragment.class));

        mLayoutItems.add(new Item(FlowLayout.class.getSimpleName(),
                "自定义流布局",
                FlowLayoutFragment.class));

        mHasLayoutItems.add(new Item(AmountView.class.getSimpleName(),
                "数量加减控件",
                AmountViewFragment.class));

        mHasLayoutItems.add(new Item(InputCodeLayout.class.getSimpleName(),
                "输入验证码布局",
                InputCodeLayoutFragment.class));

        setType(0);
    }

    public void setType(int type){
        switch (type){
            case 1:
                mItemAdapter.setNewData(mHasViewItems);
                break;
            case 2:
                mItemAdapter.setNewData(mLayoutItems);
                break;
            case 3:
                mItemAdapter.setNewData(mHasLayoutItems);
                break;
            default:
                mItemAdapter.setNewData(mViewItems);
        }
    }

    private OnItemClickCallback mOnItemClickCallback;

    interface OnItemClickCallback{
        /**
         * 点击了条目.
         *
         * @param item item 数据.
         */
        void onItemClickListener(Item item);
    }

    /**
     * 设置条目点击监听回调.
     *
     * @param callback 回调接口.
     */
    public void setOnItemClickCallback(OnItemClickCallback callback){
        this.mOnItemClickCallback = callback;
    }
}
