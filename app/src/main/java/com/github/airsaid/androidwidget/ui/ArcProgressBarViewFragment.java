package com.github.airsaid.androidwidget.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.github.airsaid.androidwidget.R;
import com.github.airsaid.androidwidget.widget.ArcProgressBarView;

import java.util.Random;

/**
 * @author airsaid
 */
public class ArcProgressBarViewFragment extends BaseFragment {

    private ArcProgressBarView mProgressBar;
    private Random             mRandom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_arcprogressbar_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRandom = new Random();
        mProgressBar = view.findViewById(R.id.progressBar);
        AppCompatSeekBar seek = view.findViewById(R.id.seek);

        mProgressBar.setOnClickListener(v -> {
            int progress = mRandom.nextInt(mProgressBar.getMax());
            seek.setProgress(progress);
        });

        TextView mValue = view.findViewById(R.id.value);
        mValue.setText(String.valueOf(mProgressBar.getMin()));
        mProgressBar.setOnProgressBarChangeListener((progressBar, progress) ->
                mValue.setText(String.valueOf(progress)));

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgressBar.setProgress(progress, true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

}
