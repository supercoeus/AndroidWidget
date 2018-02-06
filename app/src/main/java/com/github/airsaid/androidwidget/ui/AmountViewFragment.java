package com.github.airsaid.androidwidget.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.airsaid.androidwidget.MainActivity;
import com.github.airsaid.androidwidget.R;
import com.github.airsaid.androidwidget.util.DimenUtils;
import com.github.airsaid.androidwidget.widget.AmountView;

/**
 * @author airsaid
 *
 * {@link AmountView} 演示.
 */
public class AmountViewFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private AmountView mAmountView;
    private TextView mTxtWidth;
    private TextView mTxtSize;
    private TextView mTxtMin;
    private TextView mTxtMax;
    private TextView mTxtStepSize;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_amount_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAmountView = view.findViewById(R.id.amountView);
        mAmountView.setOnAmountChangedListener(new AmountView.OnAmountChangedListener() {
            @Override
            public void onAmountChanged(boolean isAdd, int changeAmount) {
                if(changeAmount == 0){
                    Toast.makeText(getContext(),
                            "不可以再" + (isAdd ? "增加" : "减少") + "了!", Toast.LENGTH_SHORT).show();
                }

                int amount = mAmountView.getAmount();
                Log.d(TAG, "amount: " + amount);
                Log.d(TAG, "isAdd: " + isAdd);
                Log.d(TAG, "changeAmount: " + changeAmount);
            }
        });

        mTxtWidth = view.findViewById(R.id.txt_width);
        ((SeekBar) view.findViewById(R.id.sb_width)).setOnSeekBarChangeListener(this);

        mTxtSize = view.findViewById(R.id.txt_size);
        ((SeekBar) view.findViewById(R.id.sb_size)).setOnSeekBarChangeListener(this);

        mTxtMin = view.findViewById(R.id.txt_min);
        ((SeekBar) view.findViewById(R.id.sb_min)).setOnSeekBarChangeListener(this);

        mTxtMax = view.findViewById(R.id.txt_max);
        ((SeekBar) view.findViewById(R.id.sb_max)).setOnSeekBarChangeListener(this);

        mTxtStepSize = view.findViewById(R.id.txt_step_size);
        ((SeekBar) view.findViewById(R.id.sb_step_size)).setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int id = seekBar.getId();
        switch (id){
            case R.id.sb_width:
                mAmountView.setMiddleWidth(DimenUtils.dp2px(getContext(), progress));
                mTxtWidth.setText("设置中间宽度：" + progress + "dp");
                break;
            case R.id.sb_size:
                mAmountView.setButtonSize(DimenUtils.dp2px(getContext(), progress),
                        DimenUtils.dp2px(getContext(), progress));
                mTxtSize.setText("设置两侧大小：" + progress + "dp");
                break;
            case R.id.sb_min:
                mAmountView.setMin(progress);
                mTxtMin.setText("设置最小数量：" + progress);
                break;
            case R.id.sb_max:
                mAmountView.setMax(progress);
                mTxtMax.setText("设置最大数量：" + progress);
                break;
            case R.id.sb_step_size:
                if(progress < 1){
                    progress = 1;
                }
                mAmountView.setStepSize(progress);
                mTxtStepSize.setText("设置步长：" + progress + "（每次加减多少）");
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
