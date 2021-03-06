package com.example.fp.androidapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;


interface MyOnTimeSetListener{
    void onTimeSet(int hour, int min);
}

public class MyTimePicker extends EditText implements MyOnTimeSetListener {
    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    int min = Calendar.getInstance().get(Calendar.MINUTE);

    public MyTimePicker(Context context) {
        super(context);
        setInputType(0);
    }
    public MyTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInputType(0);
    }

    public MyTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setInputType(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            Log.d("TAG","event.getAction() == MotionEvent.ACTION_DOWN");
            MyTimePickerDialog tpd =  MyTimePickerDialog.newInstance(getId() , hour ,min);
            //tpd.listener = this;
            tpd.show(((Activity)getContext()).getFragmentManager(),"TAG");
            return true;
        }
        return true;
    }

    @Override
    public void onTimeSet(int hour, int min) {
        if(min < 10 && hour < 10)
            setText("0" + hour + ":0" + min);
        else if(min < 10)
            setText("" + hour + ":0" + min);
        else if(hour < 10)
            setText("0" + hour + ":" + min);
        else
            setText("" + hour + ":" + min);
        this.hour = hour;
        this.min = min;
    }


    public static class MyTimePickerDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        private static final String ARG_CONTAINER_EDIT_TEXT_VIEW = "edit_text_container";
        MyOnTimeSetListener listener;

        public static MyTimePickerDialog newInstance(int tag , int hour , int min) {
            MyTimePickerDialog timePickerDialog = new MyTimePickerDialog();
            Bundle args = new Bundle();
            args.putInt(ARG_CONTAINER_EDIT_TEXT_VIEW, tag);
            args.putInt("hour" , hour);
            args.putInt("min" , min);
            timePickerDialog.setArguments(args);
            return timePickerDialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);
            int hour = 0;
            int min = 0;
            if (getArguments() != null) {
                hour = getArguments().getInt("hour");
                min = getArguments().getInt("min");
            }
            Dialog timePicker = new TimePickerDialog(getActivity(),this,hour,min,false);

            if (getArguments() != null) {
                int tag = getArguments().getInt(ARG_CONTAINER_EDIT_TEXT_VIEW);
                listener = (MyOnTimeSetListener) getActivity().findViewById(tag);
            }

            return timePicker;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Log.d("TAG","onTimeSet " + hourOfDay +":" + minute);
            listener.onTimeSet(hourOfDay,minute);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.d("TAG", "dialog destroyed");
        }
    }
}



