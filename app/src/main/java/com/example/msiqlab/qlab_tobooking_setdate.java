package com.example.msiqlab;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class qlab_tobooking_setdate extends AppCompatActivity {
    private String getdate;
    private String gettime;
    private String nowTime;
    private String setdate;

    private final static int TIME_PICKER_INTERVAL = 30;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlab_tobooking_setdate);

        Bundle bundle = this.getIntent().getExtras();
        setdate = bundle.getString("bookingdate");
        final int SorE = bundle.getInt("SorE");

        DatePicker datePicker = findViewById(R.id.datepicker);
        TimePicker timePicker = findViewById(R.id.timepicker);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss a"); //讓日期顯示為設定格式

        int setyear = 0;
        int setmon = 0;
        int setday = 0;
        if (SorE == 1) {
            setyear = Integer.valueOf(setdate.substring(0, setdate.indexOf("-")));
            setmon = Integer.valueOf(setdate.substring(setdate.indexOf("-") + 1, setdate.lastIndexOf("-")));
            setday = Integer.valueOf(setdate.substring(setdate.lastIndexOf("-") + 1));
            timePicker.setCurrentHour(8);
            timePicker.setCurrentMinute(30);
            calendar.set(Calendar.HOUR_OF_DAY, 8);// for 6 hour
            calendar.set(Calendar.MINUTE, 30);// for 0 min
            calendar.set(Calendar.SECOND, 0);
            nowTime = String.valueOf(sdfTime.format(calendar.getTime()));
        } else if (SorE == 2) {
            setyear = Integer.valueOf(setdate.substring(0, setdate.indexOf("-")));
            setmon = Integer.valueOf(setdate.substring(setdate.indexOf("-") + 1, setdate.lastIndexOf("-")));
            setday = Integer.valueOf(setdate.substring(setdate.lastIndexOf("-") + 1));
            timePicker.setCurrentHour(18);
            timePicker.setCurrentMinute(30);
            calendar.set(Calendar.HOUR_OF_DAY, 18);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);
            nowTime = String.valueOf(sdfTime.format(calendar.getTime()));
        } else if (SorE == 3) {
            setyear = Integer.valueOf(setdate.substring(0, setdate.indexOf("-")));
            setmon = Integer.valueOf(setdate.substring(setdate.indexOf("-") + 1, setdate.lastIndexOf("-")));
            setday = Integer.valueOf(setdate.substring(setdate.lastIndexOf("-") + 1))+3;
            timePicker.setCurrentHour(8);
            timePicker.setCurrentMinute(30);
            calendar.set(Calendar.HOUR_OF_DAY, 8);// for 6 hour
            calendar.set(Calendar.MINUTE, 30);// for 0 min
            calendar.set(Calendar.SECOND, 0);
            nowTime = String.valueOf(sdfTime.format(calendar.getTime()));
        }

        datePicker.init(setyear, setmon - 1, setday, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String monten = "";
                String dayten = "";
                monthOfYear++;
                if (monthOfYear < 10) {
                    monten = "0";
                }
                if (dayOfMonth < 10) {
                    dayten = "0";
                }
                getdate = String.valueOf(year + "-" + monten + monthOfYear + "-" + dayten + dayOfMonth);
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String AM_PM;
                String HOFDay;
                String Min;
                if (hourOfDay < 10) {
                    AM_PM = "上午";
                    HOFDay = "0" + String.valueOf(hourOfDay);
                } else {
                    AM_PM = "下午";
                    HOFDay = String.valueOf(hourOfDay);
                }
                if (minute < 10) {
                    Min = "0" + minute;
                } else {
                    Min = "" + minute;
                }
                gettime = String.valueOf(HOFDay + ":" + Min + ":00 " + AM_PM);
            }
        });
        TextView setdate_okbut_tv = findViewById(R.id.setdate_okbut_tv);
        TextView setdate_cancelbut_tv = findViewById(R.id.setdate_cancelbut_tv);
        setdate_okbut_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getdate == null) {
                    getdate = setdate;
                }
                if (gettime == null) {
                    gettime = nowTime;
                }
                Bundle bundle = new Bundle();
                bundle.putString("value", String.valueOf(getdate + " " + gettime));
                bundle.putString("Booking_Check", "0");
                qlab_tobooking_setdate.this.setResult(RESULT_OK, qlab_tobooking_setdate.this.getIntent().putExtras(bundle));
                finish();


            }
        });
        setdate_cancelbut_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");

            Field field = classForid.getField("minute");

            NumberPicker minuteSpinner = (NumberPicker) timePicker
                    .findViewById(field.getInt(null));
            minuteSpinner.setMinValue(0);
            minuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%d", i));
            }
            minuteSpinner.setDisplayedValues(displayedValues
                    .toArray(new String[displayedValues.size()]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

