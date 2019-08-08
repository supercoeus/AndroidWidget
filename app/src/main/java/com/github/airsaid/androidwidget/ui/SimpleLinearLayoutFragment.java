package com.github.airsaid.androidwidget.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.github.airsaid.androidwidget.R;
import com.github.airsaid.androidwidget.widget.Gravity;
import com.github.airsaid.androidwidget.widget.SimpleLinearLayout;

/**
 * @author airsaid
 */
public class SimpleLinearLayoutFragment extends BaseFragment implements
        CompoundButton.OnCheckedChangeListener,
        RadioGroup.OnCheckedChangeListener,
        View.OnClickListener {

    private SimpleLinearLayout mSimpleLinearLayout;
    private LinearLayout mLinearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple_linearlayout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSimpleLinearLayout = view.findViewById(R.id.simpleLinearLayout);
        mLinearLayout = view.findViewById(R.id.linearLayout);
        view.findViewById(R.id.btn_add).setOnClickListener(this);
        view.findViewById(R.id.btn_delete).setOnClickListener(this);
        ((RadioGroup) view.findViewById(R.id.rg_orientation))
                .setOnCheckedChangeListener(this);
        ((CheckBox) view.findViewById(R.id.cbx_left)).setOnCheckedChangeListener(this);
        ((CheckBox) view.findViewById(R.id.cbx_top)).setOnCheckedChangeListener(this);
        ((CheckBox) view.findViewById(R.id.cbx_right)).setOnCheckedChangeListener(this);
        ((CheckBox) view.findViewById(R.id.cbx_bottom)).setOnCheckedChangeListener(this);
        ((CheckBox) view.findViewById(R.id.cbx_center_v)).setOnCheckedChangeListener(this);
        ((CheckBox) view.findViewById(R.id.cbx_center_h)).setOnCheckedChangeListener(this);
        ((CheckBox) view.findViewById(R.id.cbx_center)).setOnCheckedChangeListener(this);
        ((CheckBox) view.findViewById(R.id.cbx_left2)).setOnCheckedChangeListener(this);
        ((CheckBox) view.findViewById(R.id.cbx_top2)).setOnCheckedChangeListener(this);
        ((CheckBox) view.findViewById(R.id.cbx_right2)).setOnCheckedChangeListener(this);
        ((CheckBox) view.findViewById(R.id.cbx_bottom2)).setOnCheckedChangeListener(this);
        ((CheckBox) view.findViewById(R.id.cbx_center_v2)).setOnCheckedChangeListener(this);
        ((CheckBox) view.findViewById(R.id.cbx_center_h2)).setOnCheckedChangeListener(this);
        ((CheckBox) view.findViewById(R.id.cbx_center2)).setOnCheckedChangeListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.simple_linearlayout_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_rlv_test) {
            startActivity(new Intent(getContext(), SimpleLinearLayoutRecyclerViewTestActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                add();
                break;
            case R.id.btn_delete:
                delete();
                break;
            default:
        }
    }

    private void add() {
        addView(mLinearLayout);
        addView(mSimpleLinearLayout);
    }

    private void addView(ViewGroup layout) {
        Button button = new Button(getContext());
        button.setText("button".concat(String.valueOf(layout.getChildCount() + 1)));
        layout.addView(button);
    }

    private void delete() {
        deleteView(mLinearLayout);
        deleteView(mSimpleLinearLayout);
    }

    private void deleteView(ViewGroup layout) {
        if (layout.getChildCount() > 0) {
            layout.removeViewAt(layout.getChildCount() - 1);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_vertical:
                setOrientation(1);
                break;
            case R.id.rb_horizontal:
                setOrientation(0);
                break;
            default:
        }
    }

    private void setOrientation(int orientation) {
        mLinearLayout.setOrientation(orientation);
        mSimpleLinearLayout.setOrientation(orientation);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int gravity = android.view.Gravity.LEFT | android.view.Gravity.TOP;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            gravity = mLinearLayout.getGravity();
        }
        int simpleGravity = mSimpleLinearLayout.getGravity();

        int gravity2 = android.view.Gravity.LEFT | android.view.Gravity.TOP;
        if (mLinearLayout.getChildCount() > 0) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mLinearLayout.getChildAt(0).getLayoutParams();
            gravity2 = lp.gravity;
        }
        int simpleGravity2 = 0;
        if (mSimpleLinearLayout.getChildCount() > 0) {
            SimpleLinearLayout.LayoutParams lp = (SimpleLinearLayout.LayoutParams) mSimpleLinearLayout.getChildAt(0).getLayoutParams();
            simpleGravity2 = lp.gravity;
        }
        switch (buttonView.getId()) {
            case R.id.cbx_left:
                gravity = isChecked ? gravity | android.view.Gravity.LEFT : gravity ^ android.view.Gravity.LEFT;
                simpleGravity = isChecked ? simpleGravity | Gravity.LEFT : simpleGravity ^ Gravity.LEFT;
                break;
            case R.id.cbx_top:
                gravity = isChecked ? gravity | android.view.Gravity.TOP : gravity ^ android.view.Gravity.TOP;
                simpleGravity = isChecked ? simpleGravity | Gravity.TOP : simpleGravity ^ Gravity.TOP;
                break;
            case R.id.cbx_right:
                gravity = isChecked ? gravity | android.view.Gravity.RIGHT : gravity ^ android.view.Gravity.RIGHT;
                simpleGravity = isChecked ? simpleGravity | Gravity.RIGHT : simpleGravity ^ Gravity.RIGHT;
                break;
            case R.id.cbx_bottom:
                gravity = isChecked ? gravity | android.view.Gravity.BOTTOM : gravity ^ android.view.Gravity.BOTTOM;
                simpleGravity = isChecked ? simpleGravity | Gravity.BOTTOM : simpleGravity ^ Gravity.BOTTOM;
                break;
            case R.id.cbx_center_v:
                gravity = isChecked ? gravity | android.view.Gravity.CENTER_VERTICAL : gravity ^ android.view.Gravity.CENTER_VERTICAL;
                simpleGravity = isChecked ? simpleGravity | Gravity.CENTER_VERTICAL : simpleGravity ^ Gravity.CENTER_VERTICAL;
                break;
            case R.id.cbx_center_h:
                gravity = isChecked ? gravity | android.view.Gravity.CENTER_HORIZONTAL : gravity ^ android.view.Gravity.CENTER_HORIZONTAL;
                simpleGravity = isChecked ? simpleGravity | Gravity.CENTER_HORIZONTAL : simpleGravity ^ Gravity.CENTER_HORIZONTAL;
                break;
            case R.id.cbx_center:
                gravity = isChecked ? gravity | android.view.Gravity.CENTER : gravity ^ android.view.Gravity.CENTER;
                simpleGravity = isChecked ? simpleGravity | Gravity.CENTER : simpleGravity ^ Gravity.CENTER;
                break;
            case R.id.cbx_left2:
                gravity2 = isChecked ? gravity2 | android.view.Gravity.LEFT : gravity2 ^ android.view.Gravity.LEFT;
                simpleGravity2 = isChecked ? simpleGravity2 | Gravity.LEFT : simpleGravity2 ^ Gravity.LEFT;
                break;
            case R.id.cbx_top2:
                gravity2 = isChecked ? gravity2 | android.view.Gravity.TOP : gravity2 ^ android.view.Gravity.TOP;
                simpleGravity2 = isChecked ? simpleGravity2 | Gravity.TOP : simpleGravity2 ^ Gravity.TOP;
                break;
            case R.id.cbx_right2:
                gravity2 = isChecked ? gravity2 | android.view.Gravity.RIGHT : gravity2 ^ android.view.Gravity.RIGHT;
                simpleGravity2 = isChecked ? simpleGravity2 | Gravity.RIGHT : simpleGravity2 ^ Gravity.RIGHT;
                break;
            case R.id.cbx_bottom2:
                gravity2 = isChecked ? gravity2 | android.view.Gravity.BOTTOM : gravity2 ^ android.view.Gravity.BOTTOM;
                simpleGravity2 = isChecked ? simpleGravity2 | Gravity.BOTTOM : simpleGravity2 ^ Gravity.BOTTOM;
                break;
            case R.id.cbx_center_v2:
                gravity2 = isChecked ? gravity2 | android.view.Gravity.CENTER_VERTICAL : gravity2 ^ android.view.Gravity.CENTER_VERTICAL;
                simpleGravity2 = isChecked ? simpleGravity2 | Gravity.CENTER_VERTICAL : simpleGravity2 ^ Gravity.CENTER_VERTICAL;
                break;
            case R.id.cbx_center_h2:
                gravity2 = isChecked ? gravity2 | android.view.Gravity.CENTER_HORIZONTAL : gravity2 ^ android.view.Gravity.CENTER_HORIZONTAL;
                simpleGravity2 = isChecked ? simpleGravity2 | Gravity.CENTER_HORIZONTAL : simpleGravity2 ^ Gravity.CENTER_HORIZONTAL;
                break;
            case R.id.cbx_center2:
                gravity2 = isChecked ? gravity2 | android.view.Gravity.CENTER : gravity2 ^ android.view.Gravity.CENTER;
                simpleGravity2 = isChecked ? simpleGravity2 | Gravity.CENTER : simpleGravity2 ^ Gravity.CENTER;
                break;
            default:
        }
        setGravity(gravity, simpleGravity);
        setLayoutGravity(gravity2, simpleGravity2);
    }

    private void setGravity(int gravity, int simpleGravity) {
        mLinearLayout.setGravity(gravity);
        mSimpleLinearLayout.setGravity(simpleGravity);
    }

    private void setLayoutGravity(int gravity, int simpleGravity) {
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            View childView = mLinearLayout.getChildAt(i);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) childView.getLayoutParams();
            lp.gravity = gravity;
            childView.setLayoutParams(lp);
        }

        for (int i = 0; i < mSimpleLinearLayout.getChildCount(); i++) {
            View childView = mSimpleLinearLayout.getChildAt(i);
            SimpleLinearLayout.LayoutParams lp = (SimpleLinearLayout.LayoutParams) childView.getLayoutParams();
            lp.gravity = simpleGravity;
            childView.setLayoutParams(lp);
        }
    }
}
