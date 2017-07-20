package com.example.fp.androidapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.fp.androidapp.model.Model;
import com.example.fp.androidapp.model.Student;


public class RestaurantDetailsFragment extends Fragment {

    TextView stu_name , stu_phone , stu_address ,stu_id , stu_bt , stu_bd;
    CheckBox stu_cb;
    Student st;
    private static final String ARG_PARAM1 = "param1";
    private String stId;

    public static RestaurantDetailsFragment newInstance(String param1){
        RestaurantDetailsFragment fragment = new RestaurantDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1 , param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            stId = getArguments().getString(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_restaurant_details, container, false);

        Log.d("TAG","stid = " + stId);
        stu_id = (TextView) contentView.findViewById(R.id.stu_id);
        stu_id.setText("Food Name : " + stId);

        st = Model.instace.getStudent(stId);
        Log.d("TAG","got student name: " + st.name);

        stu_name = (TextView) contentView.findViewById(R.id.stu_name);
        stu_name.setText("Restaurant Name : " + st.name);

        stu_phone = (TextView) contentView.findViewById(R.id.stu_phone);
        stu_phone.setText("User Name : " + st.phone);

        stu_address = (TextView) contentView.findViewById(R.id.stu_address);
        stu_address.setText("Restaurant Address : " + st.address);

        stu_bt = (TextView) contentView.findViewById(R.id.stu_bt);
        stu_bt.setText("Order time : "+st.birthTime);

        stu_bd = (TextView) contentView.findViewById(R.id.stu_bd);
        stu_bd.setText("Order date : "+st.birthDate);

        stu_cb = (CheckBox) contentView.findViewById(R.id.stu_cb);
        stu_cb.setChecked(st.checked);
        Log.d("Mife","state:"+st.checked);

        return contentView;
    }
}
