package com.github.airsaid.androidwidget.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.airsaid.androidwidget.R;
import com.github.airsaid.androidwidget.util.DimenUtils;
import com.github.airsaid.androidwidget.widget.InputCodeLayout;

/**
 * @author airsaid
 */
public class InputCodeLayoutFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    public static final String TAG = InputCodeLayoutFragment.class.getSimpleName();

    private InputCodeLayout mInputCodeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_code_layout, container, false);
    }

    private View findViewById(@IdRes int id) {
        return getView().findViewById(id);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViewById(R.id.btn_normal).setOnClickListener(this);
        findViewById(R.id.btn_password).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        ((SeekBar) findViewById(R.id.sbr_number)).setOnSeekBarChangeListener(this);
        ((SeekBar) findViewById(R.id.sbr_divide_width)).setOnSeekBarChangeListener(this);
        ((SeekBar) findViewById(R.id.sbr_size)).setOnSeekBarChangeListener(this);

        mInputCodeLayout = (InputCodeLayout) findViewById(R.id.inputCodeLayout);
        mInputCodeLayout.setOnInputCompleteListener(new InputCodeLayout.OnInputCompleteCallback() {
            @Override
            public void onInputCompleteListener(String code) {
                Log.d(TAG, "输入的验证码为：" + code);
                Toast.makeText(getContext(), "输入的验证码为：" + code, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_normal:
                mInputCodeLayout.setShowMode(InputCodeLayout.NORMAL);
                break;
            case R.id.btn_password:
                mInputCodeLayout.setShowMode(InputCodeLayout.PASSWORD);
                break;
            case R.id.btn_clear:
                mInputCodeLayout.clear();
                break;
            default:
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sbr_number:
                mInputCodeLayout.setNumber(progress);
                ((TextView) findViewById(R.id.txt_number)).setText("修改输入框数量(" + progress + ")");
                break;
            case R.id.sbr_divide_width:
                mInputCodeLayout.setDivideWidth(DimenUtils.dp2px(getContext(), progress));
                ((TextView) findViewById(R.id.txt_divide_width)).setText("修改输入框间距(" + progress + ")");
                break;
            case R.id.sbr_size:
                mInputCodeLayout.setWidth(DimenUtils.dp2px(getContext(), progress));
                mInputCodeLayout.setHeight(DimenUtils.dp2px(getContext(), progress));
                ((TextView) findViewById(R.id.txt_size)).setText("修改输入框大小(" + progress + ")");
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

}
