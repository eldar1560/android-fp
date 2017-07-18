package com.example.ex3.studentlist;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.ex3.studentlist.model.Model;
import com.example.ex3.studentlist.model.Student;


public class StudentDetailsFragment extends Fragment {

    TextView stu_name , stu_phone , stu_address ,stu_id , stu_bt , stu_bd;
    CheckBox stu_cb;
    Student st;
    private static final String ARG_PARAM1 = "param1";
    private String stId;

    public static StudentDetailsFragment newInstance(String param1){
        StudentDetailsFragment fragment = new StudentDetailsFragment();
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
        View contentView = inflater.inflate(R.layout.fragment_student_details, container, false);

        Log.d("TAG","stid = " + stId);
        stu_id = (TextView) contentView.findViewById(R.id.stu_id);
        stu_id.setText("ID : " + stId);

        st = Model.instace.getStudent(stId);
        Log.d("TAG","got student name: " + st.name);

        stu_name = (TextView) contentView.findViewById(R.id.stu_name);
        stu_name.setText("Name : " + st.name);

        stu_phone = (TextView) contentView.findViewById(R.id.stu_phone);
        stu_phone.setText("Phone : " + st.phone);

        stu_address = (TextView) contentView.findViewById(R.id.stu_address);
        stu_address.setText("Address : " + st.address);

        stu_bt = (TextView) contentView.findViewById(R.id.stu_bt);
        stu_bt.setText("Birth time : "+st.birthTime);

        stu_bd = (TextView) contentView.findViewById(R.id.stu_bd);
        stu_bd.setText("Birth date : "+st.birthDate);

        stu_cb = (CheckBox) contentView.findViewById(R.id.stu_cb);
        stu_cb.setChecked(st.checked);

        return contentView;
    }
}
