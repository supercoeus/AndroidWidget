package com.github.airsaid.androidwidget.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.github.airsaid.androidwidget.R;
import com.github.airsaid.androidwidget.widget.CountdownView;

/**
 * @author airsaid
 */
public class CountdownViewFragment extends BaseFragment {

    private static final String TAG = CountdownViewFragment.class.getSimpleName();

    private CountdownView mCountdownView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_countdownview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCountdownView = view.findViewById(R.id.countdownView);
        mCountdownView.setOnCountdownListener((v, time) -> {
            Log.d(TAG, "onCountdown time: " + time);

            if (time == 0 && isVisible()) {
                Toast.makeText(getContext(), "Countdown complete", Toast.LENGTH_SHORT).show();
            }
        });
        mCountdownView.setOnClickListener(v -> {
            mCountdownView.startCountdown(3);
        });

        mCountdownView.startCountdown(3);
    }

}
