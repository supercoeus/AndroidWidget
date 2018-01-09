package com.github.airsaid.androidwidget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.airsaid.androidwidget.data.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * @author airsaid
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{

    private List<Item> mList;

    public ItemAdapter(){
        mList = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.textView.setText(mList.get(position).getItem());
        final int adapterPosition = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickCallback != null){
                    mOnItemClickCallback.onItemClickListener(v, adapterPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ItemViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(android.R.id.text1);
        }
    }

    /**
     * 设置新的 Item 数据.
     * @param list item data.
     */
    public void setNewData(List<Item> list){
        mList = list;
        notifyDataSetChanged();
    }

    private OnItemClickCallback mOnItemClickCallback;

    public interface OnItemClickCallback{
        /**
         * 点击了条目.
         * @param v itemView.
         * @param position item 位置.
         */
        void onItemClickListener(View v, int position);
    }

    /**
     * 设置条目点击监听回调.
     * @param callback 回调接口.
     */
    public void setOnItemClickCallback(OnItemClickCallback callback){
        this.mOnItemClickCallback = callback;
    }
}