package com.example.fp.androidapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;


interface MyOnDateSetListener{
    void onDateSet(int year, int month , int day);
}

public class MyDatePicker extends EditText implements MyOnDateSetListener {
    int year = 2017;
    int month = 0;
    int day = 1;

    public MyDatePicker(Context context) {
        super(context);
        setInputType(0);
    }
    public MyDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInputType(0);
    }

    public MyDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setInputType(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            Log.d("TAG","event.getAction() == MotionEvent.ACTION_DOWN");
            MyDatePickerDialog dpd =  MyDatePickerDialog.newInstance(getId() , year ,month , day);
            //tpd.listener = this;
            dpd.show(((Activity)getContext()).getFragmentManager(),"TAG");
            return true;
        }
        return true;
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        int today_day = c.get(Calendar.DAY_OF_MONTH);
        int today_month = c.get(Calendar.MONTH);
        int today_year = c.get(Calendar.YEAR);
        if(today_year < year || (year == today_year && month > today_month) || (year == today_year && month == today_month && day > today_day)){
            new AlertDialog.Builder(((Activity)getContext()))
                    .setTitle("Wrong date")
                    .setMessage("the birth date can't be in the future")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else {
            if (day < 10 && (month + 1) < 10)
                setText("0" + day + "/" + "0" + (month + 1) + "/" + year);
            else if (day < 10)
                setText("0" + day + "/" + (month + 1) + "/" + year);
            else if ((month + 1) < 10)
                setText(day + "/" + "0" + (month + 1) + "/" + year);
            else
                setText(day + "/" + (month + 1) + "/" + year);
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }

    public static class MyDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private static final String ARG_CONTAINER_EDIT_TEXT_VIEW = "edit_text_container";
        MyOnDateSetListener listener;

        public static MyDatePickerDialog newInstance(int tag , int year , int month , int day) {
            MyDatePickerDialog datePickerDialog = new MyDatePickerDialog();
            Bundle args = new Bundle();
            args.putInt(ARG_CONTAINER_EDIT_TEXT_VIEW, tag);
            args.putInt("year" , year);
            args.putInt("month" , month);
            args.putInt("day" , day);
            datePickerDialog.setArguments(args);
            return datePickerDialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);
            int year = 1990;
            int month = 1;
            int day = 1;
            if (getArguments() != null) {
                year = getArguments().getInt("year");
                month = getArguments().getInt("month");
                day = getArguments().getInt("day");
            }
            //Dialog timePicker = new TimePickerDialog(getActivity(),this,hour,min,false);
            Dialog datePicker = new DatePickerDialog(getActivity() , this , year , month , day);
            if (getArguments() != null) {
                int tag = getArguments().getInt(ARG_CONTAINER_EDIT_TEXT_VIEW);
                listener = (MyOnDateSetListener) getActivity().findViewById(tag);
            }

            return datePicker;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Log.d("TAG","onDateSet " + dayOfMonth +" / " + month + " / " + year);
            listener.onDateSet(year,month ,dayOfMonth);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.d("TAG", "dialog destroyed");
        }
    }
}



