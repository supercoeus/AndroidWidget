package com.github.airsaid.androidwidget.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.airsaid.androidwidget.R;
import com.github.airsaid.androidwidget.widget.SimpleLinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author airsaid
 */
public class SimpleLinearLayoutRecyclerViewTestActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_test_simple_linearlayout);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TestAdapter());
    }

    class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder>{

        private List<String> mData;

        public TestAdapter(){
            mData = new ArrayList<>();
            mData.addAll(Arrays.asList(getResources().getStringArray(android.R.array.phoneTypes)));
            mData.addAll(Arrays.asList(getResources().getStringArray(android.R.array.imProtocols)));
            mData.addAll(Arrays.asList(getResources().getStringArray(android.R.array.organizationTypes)));
            mData.addAll(Arrays.asList(getResources().getStringArray(android.R.array.emailAddressTypes)));
            mData.addAll(Arrays.asList(getResources().getStringArray(android.R.array.postalAddressTypes)));
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_simple_linearlayout, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text1.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            private final TextView text1;

            private ViewHolder(View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
            }
        }
    }

}
