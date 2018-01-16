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
import com.github.airsaid.androidwidget.ui.SuperTextViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author airsaid
 */
public class MainFragment extends Fragment{

    private ItemAdapter mItemAdapter;
    private List<Item> mItems;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mItemAdapter = new ItemAdapter();
        recyclerView.setAdapter(mItemAdapter);
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mItems = new ArrayList<>();

        mItems.add(new Item("AmountView（数量加减控件）", AmountViewFragment.class));
        mItems.add(new Item("SuperTextView（增强版 TextView）", SuperTextViewFragment.class));

        mItemAdapter.setNewData(mItems);
        mItemAdapter.setOnItemClickCallback(new ItemAdapter.OnItemClickCallback() {
            @Override
            public void onItemClickListener(View v, int position) {
                mOnItemClickCallback.onItemClickListener(mItems.get(position));
            }
        });
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
