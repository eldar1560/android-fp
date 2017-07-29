package com.example.fp.androidapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
        void onSave(EditText foodNameEt , EditText nameEt , EditText userNameEt , EditText addressEt , CheckBox cbEt , MyTimePicker ot , MyDatePicker od);
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
        final EditText nameEt = (EditText) contentView.findViewById(R.id.editNameTv);
        final EditText foodNameEt= (EditText) contentView.findViewById(R.id.editFoodNameTv);
        final EditText userNameEt= (EditText) contentView.findViewById(R.id.editUserNameTv);
        final EditText addressEt= (EditText) contentView.findViewById(R.id.editAddressTv);
        final MyTimePicker ot= (MyTimePicker) contentView.findViewById(R.id.editOrderTimeTv);
        final MyDatePicker od = (MyDatePicker) contentView.findViewById(R.id.editOrderDateTv);
        final CheckBox cbEt= (CheckBox) contentView.findViewById(R.id.editCbTv);
        final ImageView imageView = (ImageView) contentView.findViewById(R.id.stu_edit_image);
        final ProgressBar progressBar = (ProgressBar) contentView.findViewById(R.id.stu_edit_progressBar);

        Model.instace.getStudent(stId, new Model.GetStudentCallback() {
            @Override
            public void onComplete(Student student) {
                RestaurantEditFragment.this.st_edit = student;
                Log.d("TAG","got student name: " + student.name);
                nameEt.setText(st_edit.name);
                foodNameEt.setText(st_edit.foodName);
                userNameEt.setText(st_edit.userName);
                addressEt.setText(st_edit.address);
                ot.onTimeSet(Integer.valueOf(st_edit.orderTime.substring(0,st_edit.orderTime.indexOf(":"))),Integer.valueOf(st_edit.orderTime.substring(st_edit.orderTime.indexOf(":")+1)));
                od.onDateSet(Integer.valueOf(st_edit.orderDate.substring(st_edit.orderDate.lastIndexOf("/")+1)) , Integer.valueOf(st_edit.orderDate.substring(st_edit.orderDate.indexOf("/")+1,st_edit.orderDate.lastIndexOf("/")))-1 , Integer.valueOf(st_edit.orderDate.substring(0,st_edit.orderDate.indexOf("/"))));
                cbEt.setChecked(st_edit.checked);
                if (st_edit.imageUrl != null && !st_edit.imageUrl.isEmpty() && !st_edit.imageUrl.equals("")){
                    progressBar.setVisibility(View.VISIBLE);
                    Model.instace.getImage(st_edit.imageUrl, new Model.GetImageListener() {
                        @Override
                        public void onSuccess(Bitmap image) {
                            imageView.setImageBitmap(image);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFail() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onCancel() {
                Log.d("TAG","get student cancell" );

            }
        });



        Button saveBtn = (Button) contentView.findViewById(R.id.editSaveBtn);
        Button cancelBtn = (Button) contentView.findViewById(R.id.editCancelBtn);
        final Button deleteBtn = (Button) contentView.findViewById(R.id.editDeleteBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onSave(foodNameEt ,nameEt , userNameEt ,addressEt ,cbEt , ot , od);

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
