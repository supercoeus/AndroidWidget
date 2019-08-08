package com.github.airsaid.androidwidget.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.airsaid.androidwidget.R;
import com.github.airsaid.androidwidget.widget.StaffView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author airsaid
 */
public class StaffViewFragment extends BaseFragment implements View.OnClickListener {

    private static final long TIME = 4000;

    private StaffView mStaffView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_staff_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_start).setOnClickListener(this);
        view.findViewById(R.id.btn_pause).setOnClickListener(this);
        view.findViewById(R.id.btn_resume).setOnClickListener(this);
        view.findViewById(R.id.btn_stop).setOnClickListener(this);
        mStaffView = view.findViewById(R.id.staffView);
        mStaffView.setOnAnimatorListener(new StaffView.OnAnimatorListener() {
            @Override
            public void onStart() {
                Toast.makeText(getContext(), "Listener onStart", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPause() {
                Toast.makeText(getContext(), "Listener onPause", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResume() {
                Toast.makeText(getContext(), "Listener onResume", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStop() {
                Toast.makeText(getContext(), "Listener onStop", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEnd() {
                Toast.makeText(getContext(), "Listener onEnd", Toast.LENGTH_SHORT).show();
            }
        });
        mStaffView.setData(getData());
    }

    private List<StaffView.Model> getData() {
        List<StaffView.Model> lists = new ArrayList<>();
        for (double i = 0.1; i <= 0.9; i += 0.1) {
            StaffView.Model model = new StaffView.Model();
            model.setTime(i);
            model.setLabel(i % 2 == 0 ? "L" : "R");
            model.setExtra(i % 5 == 0 ? "E" : null);
            lists.add(model);
        }
        return lists;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                mStaffView.start(TIME);
                break;
            case R.id.btn_pause:
                mStaffView.pause();
                break;
            case R.id.btn_resume:
                mStaffView.resume();
                break;
            case R.id.btn_stop:
                mStaffView.stop();
                break;
            default:
        }
    }

    @Override
    public void onDestroyView() {
        mStaffView.stop();
        super.onDestroyView();
    }

}
