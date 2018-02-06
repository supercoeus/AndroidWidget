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
import com.github.airsaid.androidwidget.ui.FlowLayoutFragment;
import com.github.airsaid.androidwidget.ui.SimpleLinearLayoutFragment;
import com.github.airsaid.androidwidget.ui.SuperTextViewFragment;
import com.github.airsaid.androidwidget.widget.AmountView;
import com.github.airsaid.androidwidget.widget.FlowLayout;
import com.github.airsaid.androidwidget.widget.SimpleLinearLayout;
import com.github.airsaid.androidwidget.widget.SuperTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author airsaid
 */
public class MainFragment extends Fragment{

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
        mItemAdapter.setOnItemClickCallback(new ItemAdapter.OnItemClickCallback() {
            @Override
            public void onItemClickListener(View v, int position) {
                mOnItemClickCallback.onItemClickListener(mItemAdapter.getData().get(position));
            }
        });
        recyclerView.setAdapter(mItemAdapter);
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewItems.add(new Item(FlowLayout.class.getSimpleName(),
                "自定义流布局",
                FlowLayoutFragment.class));

        mHasViewItems.add(new Item(SuperTextView.class.getSimpleName(),
                "增强版 TextView",
                SuperTextViewFragment.class));

        mLayoutItems.add(new Item(SimpleLinearLayout.class.getSimpleName(),
                "简易版 LinearLayout",
                SimpleLinearLayoutFragment.class));

        mHasLayoutItems.add(new Item(AmountView.class.getSimpleName(),
                "数量加减控件",
                AmountViewFragment.class));

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
         * @param item item 数据.
         */
        void onItemClickListener(Item item);
    }

    /**
     * 设置条目点击监听回调.
     * @param callback 回调接口.
     */
    public void setOnItemClickCallback(OnItemClickCallback callback){
        this.mOnItemClickCallback = callback;
    }
}
