package com.github.airsaid.androidwidget.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.airsaid.androidwidget.R;
import com.github.airsaid.androidwidget.data.DataFactory;
import com.github.airsaid.androidwidget.widget.FlowLayout;

import java.util.Random;

/**
 * @author airsaid
 */
public class FlowLayoutFragment extends Fragment implements View.OnClickListener {

    private FlowLayout mFlowLayout;
    private Random mRandom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flowo_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRandom = new Random();
        mFlowLayout = view.findViewById(R.id.flowLayout);
        view.findViewById(R.id.btn_add).setOnClickListener(this);
        view.findViewById(R.id.btn_delete).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                addView();
                break;
            case R.id.btn_delete:
                if(mFlowLayout.getChildCount() > 0){
                    mFlowLayout.removeViewAt(mFlowLayout.getChildCount() - 1);
                }
                break;
            default:
        }
    }

    @SuppressWarnings("all")
    private void addView() {
        TextView textView = new TextView(getContext());
        textView.setTextSize(20f);
        textView.setTextColor(Color.WHITE);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.color.colorPrimary);
        textView.setText(DataFactory.ITEMS[mRandom.nextInt(DataFactory.ITEMS.length - 1)]);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT
        );
        lp.topMargin = lp.rightMargin = lp.bottomMargin = 10;
        mFlowLayout.addView(textView, lp);
    }

}
