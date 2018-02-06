package com.github.airsaid.androidwidget.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.airsaid.androidwidget.R;
import com.github.airsaid.androidwidget.widget.FlowLayout;

import java.util.Random;

/**
 * @author airsaid
 *
 * {@link FlowLayout} 演示.
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
                Button button = new Button(getContext());
                StringBuilder sb = new StringBuilder();
                for (int i = -1; i < mRandom.nextInt(5); i++) {
                    sb.append("button");
                }
                button.setText(sb.toString().concat(String.valueOf(mFlowLayout.getChildCount())));
                mFlowLayout.addView(button);
                break;
            case R.id.btn_delete:
                if(mFlowLayout.getChildCount() > 0){
                    mFlowLayout.removeViewAt(mFlowLayout.getChildCount() - 1);
                }
                break;
            default:
        }
    }

}
