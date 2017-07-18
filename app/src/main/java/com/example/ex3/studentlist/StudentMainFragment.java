package com.example.ex3.studentlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.ex3.studentlist.model.Model;
import com.example.ex3.studentlist.model.Student;


public class StudentMainFragment extends Fragment {


    public static StudentMainFragment newInstance(){
        StudentMainFragment fragment = new StudentMainFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    interface StudentMainFragmentListener{
        void onSave(EditText nameEt , EditText idEt , EditText phoneEt , EditText addressEt , CheckBox cbEt , MyTimePicker bt , MyDatePicker bd);
        void onCancel();
    }

    StudentMainFragmentListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof StudentMainFragmentListener){
            listener = (StudentMainFragmentListener) activity;
        }else{
            throw new RuntimeException(activity.toString() + " must implement StudentMainFragmentListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof StudentMainFragmentListener){
            listener = (StudentMainFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement StudentMainFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_student_main, container, false);
        final EditText nameEt = (EditText) contentView.findViewById(R.id.mainNameTv);
        final EditText idEt= (EditText) contentView.findViewById(R.id.mainIdTv);
        final EditText phoneEt= (EditText) contentView.findViewById(R.id.mainPhoneTv);
        final EditText addressEt= (EditText) contentView.findViewById(R.id.mainAddressTv);
        final CheckBox cbEt= (CheckBox) contentView.findViewById(R.id.mainCbTv);
        final MyTimePicker bt = (MyTimePicker) contentView.findViewById(R.id.mainBirthTimeTv);
        final MyDatePicker bd = (MyDatePicker) contentView.findViewById(R.id.mainBirthDateTv);

        Button saveBtn = (Button) contentView.findViewById(R.id.mainSaveBtn);
        Button cancelBtn = (Button) contentView.findViewById(R.id.mainCancelBtn);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onSave(nameEt,idEt,phoneEt,addressEt,cbEt , bt , bd);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onCancel();
            }
        });
        return contentView;
    }
}
