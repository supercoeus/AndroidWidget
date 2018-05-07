package com.github.airsaid.androidwidget.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.airsaid.androidwidget.R;
import com.github.airsaid.androidwidget.widget.ProgressBarView;
import com.github.airsaid.androidwidget.widget.StaffView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author airsaid
 */
public class ProgressBarViewFragment extends BaseFragment {

    private ProgressBarView mProgressBar;
    private Random mRandom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progressbar_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRandom = new Random();
        mProgressBar = view.findViewById(R.id.progressBar);
        view.findViewById(R.id.start).setOnClickListener(v -> {
            int progress = mRandom.nextInt(100);
            mProgressBar.setProgress(progress, true);
        });
    }

}
