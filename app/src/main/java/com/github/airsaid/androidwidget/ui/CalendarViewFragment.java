package com.github.airsaid.androidwidget.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.airsaid.androidwidget.MainActivity;
import com.github.airsaid.androidwidget.R;
import com.github.airsaid.calendarview.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author airsaid
 */
public class CalendarViewFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private CalendarView mCalendarView;
    private TextView mTxtDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTxtDate = view.findViewById(R.id.txt_date);
        mCalendarView = view.findViewById(R.id.calendarView);
        view.findViewById(R.id.btn_last_month).setOnClickListener(this);
        view.findViewById(R.id.btn_next_month).setOnClickListener(this);

        // 设置已选的日期
        mCalendarView.setSelectDate(initData());
        // 设置字体
        mCalendarView.setTypeface(Typeface.SERIF);

        // 设置日期状态改变监听
        mCalendarView.setOnDateChangeListener((v, select, year, month, day) -> {
            Log.e(TAG, "select: " + select);
            Log.e(TAG, "year: " + year);
            Log.e(TAG, "month,: " + (month + 1));
            Log.e(TAG, "day: " + day);

            if (select) {
                Toast.makeText(getContext()
                        , "选中了：" + year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext()
                        , "取消选中了：" + year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
            }
        });
        // 设置是否能够改变日期状态
        mCalendarView.setChangeDateStatus(true);

        // 设置日期点击监听
        mCalendarView.setOnDataClickListener((v, year, month, day) -> {
            Log.e(TAG, "year: " + year);
            Log.e(TAG, "month,: " + (month + 1));
            Log.e(TAG, "day: " + day);
        });
        // 设置是否能够点击
        mCalendarView.setClickable(true);

        setCurDate();
    }

    private List<String> initData() {
        List<String> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        SimpleDateFormat sdf = new SimpleDateFormat(mCalendarView.getDateFormatPattern(), Locale.CHINA);
        sdf.format(calendar.getTime());
        dates.add(sdf.format(calendar.getTime()));
        return dates;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_last_month:
                mCalendarView.lastMonth();
                break;
            case R.id.btn_next_month:
                mCalendarView.nextMonth();
                break;
            default:
        }
        setCurDate();
    }

    private void setCurDate() {
        mTxtDate.setText(mCalendarView.getYear() + "年" + (mCalendarView.getMonth() + 1) + "月");
    }
}
