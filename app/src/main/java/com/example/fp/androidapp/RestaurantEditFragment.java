package com.example.fp.androidapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.fp.androidapp.model.Model;
import com.example.fp.androidapp.model.Student;


public class RestaurantEditFragment extends Fragment {

    Student st_edit;

    private static final String ARG_PARAM1 = "param1";
    private String stId;

    public static RestaurantEditFragment newInstance(String param1){
        RestaurantEditFragment fragment = new RestaurantEditFragment();
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
    interface StudentEditFragmentListener{
        void onSave(EditText idEt , EditText nameEt , EditText phoneEt , EditText addressEt , CheckBox cbEt , MyTimePicker bt , MyDatePicker bd);
        void onCancel();
        void onDelete();
    }

    StudentEditFragmentListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof StudentEditFragmentListener){
            listener = (StudentEditFragmentListener) activity;
        }else{
            throw new RuntimeException(activity.toString() + " must implement StudentEditFragmentListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof StudentEditFragmentListener){
            listener = (StudentEditFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement StudentEditFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_restaurant_edit, container, false);

        Log.d("TAG","stId : " + stId);
        st_edit = Model.instace.getStudent(stId);

        final EditText nameEt = (EditText) contentView.findViewById(R.id.editNameTv);
        nameEt.setText(st_edit.name);

        final EditText idEt= (EditText) contentView.findViewById(R.id.editIdTv);
        idEt.setText(stId);

        final EditText phoneEt= (EditText) contentView.findViewById(R.id.editPhoneTv);
        phoneEt.setText(st_edit.phone);

        final EditText addressEt= (EditText) contentView.findViewById(R.id.editAddressTv);
        addressEt.setText(st_edit.address);

        final MyTimePicker bt= (MyTimePicker) contentView.findViewById(R.id.editBirthTimeTv);
        bt.onTimeSet(Integer.valueOf(st_edit.birthTime.substring(0,st_edit.birthTime.indexOf(":"))),Integer.valueOf(st_edit.birthTime.substring(st_edit.birthTime.indexOf(":")+1)));


        final MyDatePicker bd = (MyDatePicker) contentView.findViewById(R.id.editBirthDateTv);
        bd.onDateSet(Integer.valueOf(st_edit.birthDate.substring(st_edit.birthDate.lastIndexOf("/")+1)) , Integer.valueOf(st_edit.birthDate.substring(st_edit.birthDate.indexOf("/")+1,st_edit.birthDate.lastIndexOf("/")))-1 , Integer.valueOf(st_edit.birthDate.substring(0,st_edit.birthDate.indexOf("/"))));

        Log.d("Mife" , Integer.valueOf(st_edit.birthTime.substring(0,st_edit.birthTime.indexOf(":"))) + " , " + Integer.valueOf(st_edit.birthTime.substring(st_edit.birthTime.indexOf(":")+1)) + " , " +
                Integer.valueOf(st_edit.birthDate.substring(0,st_edit.birthDate.indexOf("/"))) + " , " + Integer.valueOf(st_edit.birthDate.substring(st_edit.birthDate.indexOf("/")+1,st_edit.birthDate.lastIndexOf("/")))
        + " , " + Integer.valueOf(st_edit.birthDate.substring(st_edit.birthDate.lastIndexOf("/")+1)));
        final CheckBox cbEt= (CheckBox) contentView.findViewById(R.id.editCbTv);
        cbEt.setChecked(st_edit.checked);

        Button saveBtn = (Button) contentView.findViewById(R.id.editSaveBtn);
        Button cancelBtn = (Button) contentView.findViewById(R.id.editCancelBtn);
        final Button deleteBtn = (Button) contentView.findViewById(R.id.editDeleteBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onSave(idEt ,nameEt , phoneEt ,addressEt ,cbEt , bt , bd);

            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onDelete();

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onCancel();

            }
        });

        return contentView;
    }
}
