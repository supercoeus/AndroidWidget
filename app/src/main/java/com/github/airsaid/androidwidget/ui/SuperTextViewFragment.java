package com.github.airsaid.androidwidget.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.airsaid.androidwidget.R;
import com.github.airsaid.androidwidget.widget.SuperTextView;

/**
 * @author airsaid
 *
 * {@link com.github.airsaid.androidwidget.widget.SuperTextView} 演示.
 */
public class SuperTextViewFragment extends Fragment implements SeekBar.OnSeekBarChangeListener,
        RadioGroup.OnCheckedChangeListener {

    private SuperTextView mSuperTextView;
    private TextView mTxtTagGravity;
    private TextView mTxtTagPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_super_text_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSuperTextView = view.findViewById(R.id.superTextView2);
        mTxtTagGravity  = view.findViewById(R.id.txt_tag_gravity);
        mTxtTagPosition = view.findViewById(R.id.txt_tag_position);

        ((RadioGroup) view.findViewById(R.id.rg_tag_gravity)).setOnCheckedChangeListener(this);
        ((SeekBar) view.findViewById(R.id.sb_tag_position)).setOnSeekBarChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if(checkedId == R.id.rb_tag_start){
            mSuperTextView.setTagGravity(SuperTextView.START);
            mTxtTagGravity.setText("setTagGravity(SuperTextView.START)");
        }else if(checkedId == R.id.rb_tag_end){
            mSuperTextView.setTagGravity(SuperTextView.END);
            mTxtTagGravity.setText("setTagGravity(SuperTextView.END)");
        }
        mSuperTextView.setTags(mSuperTextView.getTags());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int id = seekBar.getId();
        switch (id){
            case R.id.sb_tag_position:
                mSuperTextView.setTagPosition(progress);
                mTxtTagPosition.setText("setTagPosition(" + mSuperTextView.getTagPosition() + ")");
                mSuperTextView.setTags(mSuperTextView.getTags());
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
