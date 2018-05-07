package com.github.airsaid.androidwidget.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.airsaid.androidwidget.R;
import com.github.airsaid.androidwidget.widget.BounceView;

/**
 * @author airsaid
 */
public class BounceViewFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener,
        CompoundButton.OnCheckedChangeListener,
        View.OnClickListener {

    private BounceView mBounceView;
    private TextView mTxtCount;
    private TextView mTxtTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bounce_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBounceView = view.findViewById(R.id.bv_ball);
        mBounceView.setBounceParams(4, 2000);
        mTxtCount = view.findViewById(R.id.txt_count);
        mTxtTime = view.findViewById(R.id.txt_time);

        view.findViewById(R.id.btn_start).setOnClickListener(this);
        ((AppCompatSeekBar) view.findViewById(R.id.sb_count)).setOnSeekBarChangeListener(this);
        ((AppCompatSeekBar) view.findViewById(R.id.sb_time)).setOnSeekBarChangeListener(this);
        ((SwitchCompat) view.findViewById(R.id.sc_debug)).setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        mBounceView.startBounce();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb_count:
                mTxtCount.setText("bounceCount(" + progress + ")");
                mBounceView.setBounceParams(progress, mBounceView.getBounceTime());
                break;
            case R.id.sb_time:
                mTxtTime.setText("animTime(" + progress + ")");
                mBounceView.setBounceParams(mBounceView.getBounceCount(), progress * 1000);
                break;
            default:
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mBounceView.setDebug(isChecked);
        if (isChecked) {
            mBounceView.setBackgroundResource(R.color.colorAccent);
        } else {
            mBounceView.setBackgroundResource(0);
        }
    }
}
