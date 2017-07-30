package com.example.fp.androidapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;


public class RestaurantMainFragment extends Fragment {

    ImageView imageView;
    Bitmap imageBitmap;
    ProgressBar progressBar;
    public static RestaurantMainFragment newInstance(){
        RestaurantMainFragment fragment = new RestaurantMainFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    interface RestaurantMainFragmentListener{
        void onSave(EditText nameEt , EditText foodNameEt , EditText addressEt , CheckBox cbEt , MyTimePicker ot , MyDatePicker od , final ProgressBar progressBar , Bitmap imageBitmap);
        void onCancel();
    }

    RestaurantMainFragmentListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof RestaurantMainFragmentListener){
            listener = (RestaurantMainFragmentListener) activity;
        }else{
            throw new RuntimeException(activity.toString() + " must implement RestaurantMainFragmentListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof RestaurantMainFragmentListener){
            listener = (RestaurantMainFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement RestaurantMainFragmentListener");
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
        View contentView = inflater.inflate(R.layout.fragment_restaurant_main, container, false);
        final EditText nameEt = (EditText) contentView.findViewById(R.id.mainNameTv);
        final EditText foodNameEt= (EditText) contentView.findViewById(R.id.mainFoodNameTv);
        final EditText addressEt= (EditText) contentView.findViewById(R.id.mainAddressTv);
        final CheckBox cbEt= (CheckBox) contentView.findViewById(R.id.mainCbTv);
        final MyTimePicker ot = (MyTimePicker) contentView.findViewById(R.id.mainOrderTimeTv);
        final MyDatePicker od = (MyDatePicker) contentView.findViewById(R.id.mainOrderDateTv);

        Button saveBtn = (Button) contentView.findViewById(R.id.mainSaveBtn);
        Button cancelBtn = (Button) contentView.findViewById(R.id.mainCancelBtn);

        progressBar = (ProgressBar) contentView.findViewById(R.id.mainProgressBar);
        progressBar.setVisibility(GONE);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onSave(nameEt,foodNameEt,addressEt,cbEt , ot , od , progressBar , imageBitmap);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onCancel();
            }
        });
        imageView = (ImageView) contentView.findViewById(R.id.mainImageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        return contentView;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(MyApplication.getMyContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
}
